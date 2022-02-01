package org.firstinspires.ftc.shellycode.archive;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.shellycode.Consts;
import org.firstinspires.ftc.shellycode.utils.Motors;
import org.firstinspires.ftc.shellycode.utils.VuHelper;

@Autonomous(name = "Test Vu", group = "VU")
public class TestVu extends OpMode {
    private Motors motors;

    private VuHelper vuHelper;

    private boolean visibleTarget = false;

    @Override
    public void init() {
        motors = new Motors(hardwareMap);

        vuHelper = new VuHelper(hardwareMap);
    }

    @Override
    public void start() {
        vuHelper.loadWallTargets();
    }

    @Override
    public void loop() {
        vuHelper.targets.activate();

        for (VuforiaTrackable trackable : vuHelper.targets) {
            visibleTarget = false;
            if (((VuforiaTrackableDefaultListener)trackable.getListener()).isVisible()) {
                visibleTarget = true;
                telemetry.addData("Visible Target", trackable.getName());

                OpenGLMatrix curTarget = ((VuforiaTrackableDefaultListener) trackable.getListener()).getVuforiaCameraFromTarget(); // gets the raw of the trackable

                if (curTarget != null) {
                    VectorF trans = curTarget.getTranslation();
                    telemetry.addData("Translation", trans);

                    // extract the X & Y components of the offset of the target relative to the robot
                    float targetX = trans.get(0) / Consts.MM_PER_INCH; // target X axis
                    float targetY = trans.get(2) / Consts.MM_PER_INCH; // target Y axis

                    double targetRange = Math.hypot(targetX, targetY);
                    telemetry.addData("range", targetRange);
                    // target bearing is based on angle formed between the X axis and the target range line
                    double targetBearing = -Math.toDegrees(Math.asin(targetX / targetRange));
                    telemetry.addData("bearing", targetBearing);

                    double rangeError = (targetRange - Consts.DIST_FRM_TARGET);
                    double headingError = targetBearing;

                    // use the speed and turn gains to calculate robot movement
                    double drive = rangeError * 0.1;
                    double turn = -headingError * Consts.VU_DEF_SPED;
                    telemetry.addData("drive", drive);
                    telemetry.addData("turn", turn);

//                    motors.drive(drive, 0 , turn);
                    motors.pushbotDrive(drive, turn);

                    break;
                }
            }
        }

        if (!visibleTarget) {
            motors.stopAll();
        }
    }
}
