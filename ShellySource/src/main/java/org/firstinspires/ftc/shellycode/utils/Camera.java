package org.firstinspires.ftc.shellycode.utils;

import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.os.Handler;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.android.util.Size;
import org.firstinspires.ftc.robotcore.external.function.Consumer;
import org.firstinspires.ftc.robotcore.external.function.Continuation;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraCaptureRequest;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraCaptureSequenceId;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraCaptureSession;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraCharacteristics;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraException;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraFrame;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraManager;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.internal.collections.EvictingBlockingQueue;
import org.firstinspires.ftc.robotcore.internal.network.CallbackLooper;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.robotcore.internal.system.ContinuationSynchronizer;
import org.firstinspires.ftc.robotcore.internal.system.Deadline;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

public class Camera {
    private final String TAG = "Camera";
    private final Telemetry telem;
    private final File captureDirectory = AppUtil.ROBOT_DATA_DIR;
    private final CameraManager cameraManager;
    private final WebcamName cameraName;
    private org.firstinspires.ftc.robotcore.external.hardware.camera.Camera camera;
    private CameraCaptureSession cameraCaptureSession;
    // bitmap queue where frames are placed when they become available
    private EvictingBlockingQueue<Bitmap> frameQueue;
    // utility that determines where callbacks from the camera are to run
    private final Handler callbackHandler;

    private int captureCounter = 0;

    public Camera(HardwareMap hm, Telemetry telem) {
        this.telem = telem;

        callbackHandler = CallbackLooper.getDefault().getHandler();

        cameraManager = ClassFactory.getInstance().getCameraManager();
        cameraName = hm.get(WebcamName.class, "cam");

        initializeFrameQueue(10000);
        AppUtil.getInstance().ensureDirectoryExists(captureDirectory);

        openCamera();
        if (camera == null) return;

        startCamera();
        if (cameraCaptureSession == null) return;
    }

    public void capture() {
        try {
            Bitmap bmp = frameQueue.poll();
            if (bmp != null) {
                onNewFrame(bmp);
            }
        } finally {
            closeCamera();
        }
    }

    // save frame
    private void onNewFrame(Bitmap frame) {
        saveBitmap(frame);
        frame.recycle();
    }

    private void initializeFrameQueue(int capacity) {
        frameQueue = new EvictingBlockingQueue<Bitmap>(new ArrayBlockingQueue<Bitmap>(capacity));
        frameQueue.setEvictAction(new Consumer<Bitmap>() {
            @Override
            public void accept(Bitmap frame) {
                frame.recycle();
            }
        });
    }

    private void openCamera() {
        if (camera != null) return;

        Deadline deadline = new Deadline(60, TimeUnit.SECONDS);
        camera = cameraManager.requestPermissionAndOpenCamera(deadline, cameraName, null);
        if (camera == null) {
            error("camera not found or permission to use not granted: %s", cameraName);
        }
    }

    private void startCamera() {
        if (cameraCaptureSession != null) return;

        final int imageFormat = ImageFormat.YUY2;

        CameraCharacteristics cameraCharacteristics = cameraName.getCameraCharacteristics();
        if (!contains(cameraCharacteristics.getAndroidFormats(), imageFormat)) {
            error("image format not supported");
            return;
        }
        final Size size = cameraCharacteristics.getDefaultSize(imageFormat);
        final int fps = cameraCharacteristics.getMaxFramesPerSecond(imageFormat, size);

        /** Some of the logic below runs asynchronously on other threads. Use of the synchronizer
         * here allows us to wait in this method until all that asynchrony completes before returning. */
        final ContinuationSynchronizer<CameraCaptureSession> synchronizer = new ContinuationSynchronizer<>();
        try {
            /** Create a session in which requests to capture frames can be made */
            camera.createCaptureSession(Continuation.create(callbackHandler, new CameraCaptureSession.StateCallbackDefault() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    try {
                        // start requesting frames
                        final CameraCaptureRequest captureRequest = camera.createCaptureRequest(imageFormat, size, fps);
                        session.startCapture(captureRequest,
                                new CameraCaptureSession.CaptureCallback() {
                                    @Override
                                    public void onNewFrame(@NonNull CameraCaptureSession session, @NonNull CameraCaptureRequest request, @NonNull CameraFrame cameraFrame) {
                                        // new frame available
                                        Bitmap bmp = captureRequest.createEmptyBitmap();
                                        cameraFrame.copyToBitmap(bmp);
                                        frameQueue.offer(bmp);
                                    }
                                },
                                Continuation.create(callbackHandler, new CameraCaptureSession.StatusCallback() {
                                    @Override
                                    public void onCaptureSequenceCompleted(@NonNull CameraCaptureSession session, CameraCaptureSequenceId cameraCaptureSequenceId, long lastFrameNumber) {
                                        RobotLog.ii(TAG, "capture sequence %s reports completed: lastFrame=%d", cameraCaptureSequenceId, lastFrameNumber);
                                    }
                                })
                        );
                        synchronizer.finish(session);
                    } catch (CameraException | RuntimeException e) {
                        RobotLog.ee(TAG, e, "exception starting capture");
                        error("exception starting capture");
                        session.close();
                        synchronizer.finish(null);
                    }
                }
            }));
        } catch (CameraException | RuntimeException e) {
            RobotLog.ee(TAG, e, "exception starting camera");
            error("exception starting camera");
            synchronizer.finish(null);
        }

        try {
            synchronizer.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        cameraCaptureSession = synchronizer.getValue();
    }

    private void stopCamera() {
        if (cameraCaptureSession != null) {
            cameraCaptureSession.stopCapture();
            cameraCaptureSession.close();
            cameraCaptureSession = null;
        }
    }

    private void closeCamera() {
        stopCamera();
        if (camera != null) {
            camera.close();
            camera = null;
        }
    }

    private void error(String format, Object... args) {
        telem.log().add(format, args);
    }

    private boolean contains(int[] array, int value) {
        for (int i : array) {
            if (i == value) return true;
        }
        return false;
    }

    private void saveBitmap(Bitmap bitmap) {
        File file = new File(captureDirectory, String.format(Locale.getDefault(), "webcam-frame-%d.jpg", captureCounter++));
        try {
            try (FileOutputStream outputStream = new FileOutputStream(file)) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                RobotLog.i("captured %s", file.getName());
            }
        } catch (IOException e) {
            RobotLog.ee(TAG, e, "exception in saveBitmap()");
            error("exception saving %s", file.getName());
        }
    }
}
