package org.firstinspires.ftc.shellycode.utils;

import android.graphics.Bitmap;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.shellycode.Consts;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Camera {
    private final VuforiaLocalizer vuforia;

    public Camera(HardwareMap hm) {
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();
        parameters.vuforiaLicenseKey = Consts.VUFORIA_LIC;
        parameters.cameraName = hm.get(WebcamName.class, "cam");
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        vuforia.setFrameQueueCapacity(1); // always want most recent
        vuforia.enableConvertFrameToBitmap(); // why do you have to enable this, what would it hurt to have it on by default?
    }

    public Bitmap captureBitmap() {
        try {
            VuforiaLocalizer.CloseableFrame frame = vuforia.getFrameQueue().take();
            Bitmap bmp = vuforia.convertFrameToBitmap(frame);

            frame.close(); // not sure what this does... assuming closeable frame expects you to close it lol

            return bmp;
        } catch (InterruptedException e) {
            RobotLog.e("Bitmap Capture Interrupted:", e);
            return null;
        }
    }

    public static void saveBitmap(Bitmap bmp, String name) {
        RobotLog.i(name);
        File file = new File(Consts.CAPTURE_DIR, name + ".jpg");
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        } catch (FileNotFoundException e) {
            RobotLog.e("File Not Found: ", e);
        } catch (IOException e) {
            RobotLog.e("IO Exception: ", e);
        }
    }

    public void close() {
        vuforia.getCamera().close();
    }
}
