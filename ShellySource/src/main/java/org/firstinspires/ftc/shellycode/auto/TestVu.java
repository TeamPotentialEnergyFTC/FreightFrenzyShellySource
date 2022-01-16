package org.firstinspires.ftc.shellycode.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.shellycode.BuildConfig;
import org.firstinspires.ftc.shellycode.Consts;
import org.firstinspires.ftc.shellycode.utils.Motors;
import org.firstinspires.ftc.shellycode.utils.VuHelper;

@Autonomous(name = "Test Vu", group = "VU")
public class TestVu extends OpMode {
    private Motors motors;

    private VuHelper vuHelper;

    @Override
    public void init() {
        motors = new Motors(hardwareMap);

        vuHelper = new VuHelper(hardwareMap);
        vuHelper.loadWallTargets();
    }

    @Override
    public void init_loop() {
        telemetry.addData("vu targets", vuHelper.targets);
    }

    @Override
    public void loop() {

        for (VuforiaTrackable trackable : vuHelper.targets) {
            if (((VuforiaTrackableDefaultListener)trackable.getListener()).isVisible()) {
                telemetry.addData("Visible Target", trackable.getName());

                OpenGLMatrix curTarget = ((VuforiaTrackableDefaultListener) trackable.getListener()).getVuforiaCameraFromTarget(); // gets the raw of the trackable

                if (curTarget != null) {
                    VectorF trans = curTarget.getTranslation();
                    telemetry.addData("Translation", trans);

                    // extract the X & Y components of the offset of the target relative to the robot
                    float targetX = trans.get(0) / Consts.MM_PER_INCH; // target X axis
                    float targetY = trans.get(2) / Consts.MM_PER_INCH; // target Y axis

                    double targetRange = Math.hypot(targetX, targetY);
                    // target bearing is based on angle formed between the X axis and the target range line
                    double targetBearing = -Math.toDegrees(Math.asin(targetX / targetRange));

                    double rangeError = (targetRange - Consts.DIST_FRM_TARGET);
                    double headingError = targetBearing;

                    // use the speed and turn gains to calculate robot movement
                    double drive = rangeError * Consts.AUTO_DEF_SPED;
                    double turn = headingError * Consts.AUTO_DEF_SPED;

                    motors.drive(drive, 0 , turn);

                    break;
                }
            }
        }
    }
}
