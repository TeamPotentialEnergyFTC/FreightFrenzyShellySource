package org.firstinspires.ftc.shellycode.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Const;
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

@Autonomous(name = "Block Dropoff", group = "Autonomous")
public class AutonoOpBlockTF extends OpMode {
    private TFLiteHelper tfLiteHelper;

    private VuHelper vuHelper;

    private OpenGLMatrix curTarget = null;
    private double targetX;
    private double targetY;
    private double targetRange;
    private double targetBearing;
    private double drive;
    private double turn;

    private Motors motors;
    private ElapsedTime runtime = new ElapsedTime();

    private int barcodePos = 1;
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

        cameraCenter = vuHelper.vulo.getCameraCalibration().getSize().getWidth() / 2;

        motors = new Motors(hardwareMap);
    }

    @Override
    public void init_loop() {
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

        motors.claw.setPosition(Consts.CLAW_MAX); // grab
        motors.hold(motors.arm, Consts.ARM_LEVELS[barcodePos]); // hold

        // smol forward
        motors.lbd.setPower(Consts.SMOL_SPED);
        motors.rfd.setPower(Consts.SMOL_SPED);

        // right
        motors.lfd.setPower(Consts.AUTO_DEF_SPED);
        motors.rbd.setPower(Consts.AUTO_DEF_SPED);
    }

    @Override
    public void loop() {
        double ms = runtime.milliseconds();

        if (Utils.inTolerantRange(ms, 1500 + (barcodePos * Consts.LEVEL_RIGHT_OFFSET), Consts.AUTO_MS_TOLERANCE)) {
            // diagonal
            motors.lfd.setPower(Consts.AUTO_DEF_SPED);
            motors.rbd.setPower(Consts.AUTO_DEF_SPED);
            motors.lbd.setPower(-Consts.AUTO_DEF_SPED - Consts.SMOL_SPED);
            motors.rfd.setPower(-Consts.AUTO_DEF_SPED + Consts.SMOL_SPED);
        }
        else if (Utils.inTolerantRange(ms, 3800 + (barcodePos == 2 ? 100 : 0), Consts.AUTO_MS_TOLERANCE)) {
            motors.stopAll();
            motors.lfd.setPower(-Consts.AUTO_DEF_SPED);
            motors.rbd.setPower(-Consts.AUTO_DEF_SPED);
            motors.lbd.setPower(Consts.AUTO_DEF_SPED);
            motors.rfd.setPower(Consts.AUTO_DEF_SPED);
//            motors.lbd.setPower(Consts.AUTO_DEF_SPED);
//            motors.rfd.setPower(Consts.AUTO_DEF_SPED);
        }
        else if (Utils.inTolerantRange(ms, 5200, Consts.AUTO_MS_TOLERANCE)) {
            motors.stopAll();
            motors.spin(-Consts.AUTO_DEF_SPED);
        }
        else if (ms > 5600) {
            motors.stopAll();
            telemetry.addData("vu", "drive to target");
            telemetry.addData("vu targets", vuHelper.targets);

            for (VuforiaTrackable trackable : vuHelper.targets) {
                if (((VuforiaTrackableDefaultListener)trackable.getListener()).isVisible()) {
                    telemetry.addData("Visible Target", trackable.getName());

                    curTarget = ((VuforiaTrackableDefaultListener)trackable.getListener()).getVuforiaCameraFromTarget(); // gets the raw of the trackable

                    if (curTarget != null) {
                        VectorF trans = curTarget.getTranslation();
                        telemetry.addData("Translation", trans);

                        // extract the X & Y components of the offset of the target relative to the robot
                        targetX = trans.get(0) / Consts.MM_PER_INCH; // target X axis
                        targetY = trans.get(2) / Consts.MM_PER_INCH; // target Y axis

                        targetRange = Math.hypot(targetX, targetY);
                        // target bearing is based on angle formed between the X axis and the target range line
                        targetBearing = -Math.toDegrees(Math.asin(targetX / targetRange));

                        double rangeError = (targetRange - Consts.DIST_FRM_TARGET);
                        double headingError = targetBearing;

                        // use the speed and turn gains to calculate robot movement
                        drive = rangeError * Consts.AUTO_DEF_SPED;
                        turn = headingError * Consts.AUTO_DEF_SPED;

                        motors.drive(drive, 0 , turn);

                        break;
                    }
                }
            }
        }

    }

}
