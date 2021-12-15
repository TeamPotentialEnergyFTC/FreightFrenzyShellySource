package org.firstinspires.ftc.shellycode;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.RobotLog;
import com.vuforia.Image;
import com.vuforia.PIXEL_FORMAT;
import com.vuforia.Vuforia;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.util.Locale;

@TeleOp(name = "Test Op 1 ", group = "Test")
// @Disabled
public class TestOp1 extends OpMode {
    private VuforiaLocalizer vuforia;

    @Override
    public void init() {
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = Consts.VUFORIA_LIC;
        parameters.cameraName = hardwareMap.get(WebcamName.class, "cam");
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        vuforia.setFrameQueueCapacity(1);
        vuforia.enableConvertFrameToBitmap();

        try {
            VuforiaLocalizer.CloseableFrame frame = vuforia.getFrameQueue().take();
            Bitmap bmp = vuforia.convertFrameToBitmap(frame);

            File file = new File(Consts.CAPTURE_DIR, "epic-bmp.jpg");
            try (FileOutputStream outputStream = new FileOutputStream(file)) {
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            }
            frame.close();
        } catch (InterruptedException | IOException e) {
            telemetry.log().add("nope", e);
        }
    }

    @Override
    public void loop() {

    }
}
