package org.firstinspires.ftc.shellycode.auto;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XZY;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.EXTRINSIC;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.shellycode.Consts;

import java.util.ArrayList;
import java.util.List;

@TeleOp(name = "Test Old Vu", group = "Webcam")
// @Disabled
public class TestVuOld extends LinearOpMode {
    // basically controls the speed during auto
    private final double DESIRED_DISTANCE = 4.0; // how close the robot should get to the target
    private final double SPEED_GAIN = 0.08; // speed control, rmp up to 50% power at a 25 inch error (0.50 / 25.0)
    private final double TURN_GAIN = 0.02; // turn control, rmp up to 25% power at a 25 degree error (0.25 / 25.0)

    private OpenGLMatrix target = null;
    private VuforiaLocalizer vuforia = null;
    private VuforiaTrackables imgTargets = null;

    private boolean targetVisible = false;
    private double targetRange = 0; // distance from camera to target (deg)
    private double targetBearing = 0; // robot heading relative to target, positive is right (in)
    private double drive = 0;
    private double turn = 0;

    private DcMotor lfd = null;
    private DcMotor rfd = null;
    private DcMotor lbd = null;
    private DcMotor rbd = null;

    @Override
    public void runOpMode() {
        // shows camera on screen (camera stream from options and tap the preview window to receive a fresh image), use VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(); to not do this
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId); // init params
        // set params
        parameters.vuforiaLicenseKey = Consts.VUFORIA_LIC;
        parameters.cameraName = hardwareMap.get(WebcamName.class, "cam");
        parameters.useExtendedTracking = false; // turns off tracking beyond the target
        // load params
        vuforia = ClassFactory.getInstance().createVuforia(parameters); //  start the Vuforia engine (vroom vroom!)

        imgTargets = this.vuforia.loadTrackablesFromAsset("FreightFrenzyDB"); // load the data sets for the trackable image objects from db

        // for convenience, gather together all the trackable objects in one easily-iterable collection
        List<VuforiaTrackable> allTrackables = new ArrayList<VuforiaTrackable>();
        RobotLog.i(getClass().getName(), "allTrackables: " + allTrackables); // log the trackables
        allTrackables.addAll(imgTargets);

        imgTargets.get(0).setName("Red Storage");
        imgTargets.get(1).setName("Red Alliance Wall");
        imgTargets.get(2).setName("Blue Storage");
        imgTargets.get(3).setName("Red Alliance Wall");
        imgTargets.get(4).setName("Spring Cyl");

        OpenGLMatrix cameraLocationOnRobot = OpenGLMatrix
                .translation(Consts.CAMERA_FORWARD_DISPLACEMENT, Consts.CAMERA_LEFT_DISPLACEMENT, Consts.CAMERA_VERTICAL_DISPLACEMENT)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XZY, DEGREES, 90, 90, 0));

        // lets the trackables know where the webcam is
        for (VuforiaTrackable trackable : allTrackables) {
            ((VuforiaTrackableDefaultListener) trackable.getListener()).setCameraLocationOnRobot(parameters.cameraName, cameraLocationOnRobot);
        }

        // where l: left, f: front, d: drive, r: right, b: back (less writing on annoying Driver Hub keyboard)
        lfd  = hardwareMap.get(DcMotor.class, "lfd"); // left front
        rfd = hardwareMap.get(DcMotor.class, "rfd"); // right front
        lbd  = hardwareMap.get(DcMotor.class, "lbd"); // left back
        rbd = hardwareMap.get(DcMotor.class, "rbd"); // right back

        // reverse the motor that runs backwards when connected directly to the battery
        lfd.setDirection(DcMotor.Direction.FORWARD);
        rfd.setDirection(DcMotor.Direction.REVERSE);
        lbd.setDirection(DcMotor.Direction.FORWARD);
        rbd.setDirection(DcMotor.Direction.REVERSE);

        waitForStart();

        imgTargets.activate();
        while (opModeIsActive()) {
            // check all the trackable targets to see which one (if any) is visible
            targetVisible = false;
            for (VuforiaTrackable trackable : allTrackables) {
                if (((VuforiaTrackableDefaultListener) trackable.getListener()).isVisible()) {
                    telemetry.addData("Visible Target", trackable.getName());
                    targetVisible = true;

                    target = ((VuforiaTrackableDefaultListener)trackable.getListener()).getVuforiaCameraFromTarget(); // gets the raw  of the trackable
                    // if we have a target, process the "" to determine the position of the target relative to the robot
                    if (target != null) {
                        VectorF trans = target.getTranslation();
                        telemetry.addData("Translation", trans);

                        // extract the X & Y components of the offset of the target relative to the robot
                        double targetX = trans.get(0) / Consts.MM_PER_INCH; // image X axis
                        double targetY = trans.get(2) / Consts.MM_PER_INCH; // image Z axis
                        // target range is based on distance from robot position to origin (right triangle).
                        targetRange = Math.hypot(targetX, targetY);
                        // target bearing is based on angle formed between the X axis and the target range line
                        targetBearing = -Math.toDegrees(Math.asin(targetX / targetRange));

                        break;
                    }
                }
            }

            if (targetVisible) {
                telemetry.addData(">", "LB to attempt to go to target");
                telemetry.addData("Range", "%5.1f inches", targetRange);
                telemetry.addData("Bearing", "%3.0f degrees", targetBearing);
            }
            else {
                telemetry.addData("Visible Target", "none");
            }

            // drive to target if LB is pressed
            if (targetVisible && gamepad1.left_bumper) {

                // determine heading and range error to control for automatic robot control
                double rangeError = (targetRange - DESIRED_DISTANCE);
                double headingError = targetBearing;

                // Use the speed and turn gains to calculate robot movement
                drive = rangeError * SPEED_GAIN;
                turn = headingError * TURN_GAIN;

                telemetry.addData("Auto", "Drive %5.2f, Turn %5.2f", drive, turn);
            }
            telemetry.update();

            // calculate wheel powers

            lbd.setPower(drive - turn);
            rfd.setPower(drive + turn);
//            lfd.setPower(drive + turn);
//            rbd.setPower(drive - turn);

            sleep(10);
        }
    }
}
