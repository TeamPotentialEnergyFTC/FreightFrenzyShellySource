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
import org.firstinspires.ftc.robotcore.internal.android.dx.util.ByteArray;

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

        try {
            VuforiaLocalizer.CloseableFrame frame = vuforia.getFrameQueue().take();

            Image image = frame.getImage(0);
            byte[] pixVals = image.getPixels().array();

            for (byte byt : pixVals) {
                RobotLog.i(String.valueOf(byt));
//                telemetry.log().add(String.valueOf(byt));
            }

            frame.close();
        } catch (InterruptedException e) {
            telemetry.log().add("nope", e);
        }
    }

    @Override
    public void loop() {

    }
}
