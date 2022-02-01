package org.firstinspires.ftc.shellycode.archive;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.shellycode.Consts;
import org.firstinspires.ftc.shellycode.utils.Motors;
import org.firstinspires.ftc.shellycode.utils.TFLiteHelper;
import org.firstinspires.ftc.shellycode.utils.Utils;
import org.firstinspires.ftc.shellycode.utils.VuHelper;

import java.util.List;

@Autonomous(name = "Block Dropoff Red + FWD", group = "Autonomous")
public class AutonoOpBlockTFFWD extends OpMode {
    private TFLiteHelper tfLiteHelper;

    private VuHelper vuHelper;

    private Motors motors;
    private final ElapsedTime runtime = new ElapsedTime();

    private int barcodePos = 0;
    private double cameraCenter = 0;
    private double boxAverage = 0;

    private static final String[] LABELS = {
            "tse"
    };

    @Override
    public void init() {
        vuHelper = new VuHelper(hardwareMap);
        vuHelper.loadWallTargets();

        int tfId = hardwareMap.appContext.getResources().getIdentifier("tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        tfLiteHelper = new TFLiteHelper(Consts.TFL_OD_MODEL, LABELS, 0.65f, 512, 1, tfId, vuHelper.vulo);

        cameraCenter = vuHelper.vulo.getCameraCalibration().getSize().getWidth() / 2.0;

        motors = new Motors(hardwareMap);
    }

    @Override
    public void init_loop() {
        motors.claw.setPosition(Consts.CLAW_MAX); // grab

        if (tfLiteHelper.tfod == null) { return; }

        // align right wheel of intake to tile line :D
        List<Recognition> updatedRecognitions = tfLiteHelper.tfod.getUpdatedRecognitions();
        if (updatedRecognitions != null) {
            telemetry.addData("Objects detected", updatedRecognitions.size());

            for (Recognition recognition : updatedRecognitions) {
                telemetry.addData("Label", recognition.getLabel());
                telemetry.addData("Confidence", recognition.getConfidence());
                telemetry.addData("Top Left","(%.2f, %.2f)", recognition.getLeft(), recognition.getTop());
                telemetry.addData("Bottom Right", "(%.2f, %.2f)", recognition.getRight(), recognition.getBottom());
                telemetry.addData("-------", "-------");

                boxAverage = (recognition.getLeft() + recognition.getRight()) / 2;

                barcodePos = (boxAverage < cameraCenter - Consts.AUTO_CAM_BARCODE_OFFSETS) ? 0 : (boxAverage > cameraCenter + Consts.AUTO_CAM_BARCODE_OFFSETS ? 2 : 1);
                telemetry.addData("Barcode pos", barcodePos);
            }
        }
    }

    @Override
    public void start() {
        runtime.reset();

        motors.hold(motors.arm, Consts.ARM_LEVELS[barcodePos]); // arm

        // forward
        motors.rfd.setPower(-Consts.AUTO_DEF_SPED);
        motors.lbd.setPower(-Consts.AUTO_DEF_SPED);
    }

    @Override
    public void loop() {
        double ms = runtime.milliseconds();
//        if (Utils.inTolerantRange(ms, 500, Consts.AUTO_MS_TOLERANCE)) motors.hold(motors.arm, Consts.ARM_LEVELS[barcodePos]); // arm
        if (Utils.inTolerantRange(ms, 1400, Consts.AUTO_MS_TOLERANCE)) {

            motors.rfd.setPower(0);
            motors.lbd.setPower(0);

            // left
            motors.lfd.setPower(-Consts.AUTO_DEF_SPED);
            motors.rbd.setPower(-Consts.AUTO_DEF_SPED);
        }
        else if (Utils.inTolerantRange(ms, 2600, Consts.AUTO_MS_TOLERANCE)) {
            motors.lfd.setPower(0);
            motors.rbd.setPower(0);

            // forward
            motors.rfd.setPower(-Consts.AUTO_DEF_SPED);
            motors.lbd.setPower(-Consts.AUTO_DEF_SPED);
        }
        else if (Utils.inTolerantRange(ms, 3400, Consts.AUTO_MS_TOLERANCE)) {
            motors.rfd.setPower(0);
            motors.lbd.setPower(0);

            motors.claw.setPosition(Consts.CLAW_MIN); // drop
        }
        else if (Utils.inTolerantRange(ms, 4500, Consts.AUTO_MS_TOLERANCE)) {
            // backwards
            motors.rfd.setPower(Consts.AUTO_DEF_SPED);
            motors.lbd.setPower(Consts.AUTO_DEF_SPED);
        }
        else if (Utils.inTolerantRange(ms, 5200, Consts.AUTO_MS_TOLERANCE)) {
            // right
            motors.lfd.setPower(Consts.AUTO_DEF_SPED);
            motors.rbd.setPower(Consts.AUTO_DEF_SPED);
        }
        else if (Utils.inTolerantRange(ms, 8000, Consts.AUTO_MS_TOLERANCE)) {
            // less back
            motors.rfd.setPower(Consts.AUTO_DEF_SPED / 3);
            motors.lbd.setPower(Consts.AUTO_DEF_SPED / 3);
        }
        else if (Utils.inTolerantRange(ms, 11000, Consts.AUTO_MS_TOLERANCE)) {
            // forward!
            motors.rfd.setPower(-Consts.AUTO_DEF_SPED);
            motors.lbd.setPower(-Consts.AUTO_DEF_SPED);
        }
        else if (Utils.inTolerantRange(ms, 14000, Consts.AUTO_MS_TOLERANCE)) {
            motors.stopAll();
        }
    }
}
