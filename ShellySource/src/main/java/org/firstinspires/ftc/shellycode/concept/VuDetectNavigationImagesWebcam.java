//package org.firstinspires.ftc.shellycode.concept;
//
//import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
//import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XYZ;
//import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XZY;
//import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.EXTRINSIC;
//
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
//import com.qualcomm.robotcore.util.RobotLog;
//
//import org.firstinspires.ftc.robotcore.external.ClassFactory;
//import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
//import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
//import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
//import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
//import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
//import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
//import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
//import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@TeleOp(name="Detect Navigation Images Webcam", group="Webcam")
//// @Disabled
//public class VuDetectNavigationImagesWebcam extends LinearOpMode {
//    private VuforiaLocalizer vuforia = null;
//    private VuforiaTrackables targets = null;
//
//    private double targetX;
//    private double targetY;
//    private OpenGLMatrix target = null;
//    private boolean targetVisible = false;
//
//    @Override
//    public void runOpMode() {
//        // shows camera on screen (camera stream from options and tap the preview window to receive a fresh image), use VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(); to not do this
//        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
//        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId); // init params
//        // set params
//        parameters.vuforiaLicenseKey = Consts.VUFORIA_LIC;
//        parameters.cameraName = hardwareMap.get(WebcamName.class, "cam");
//        parameters.useExtendedTracking = false; // turns off tracking beyond the target
//        // load params
//        vuforia = ClassFactory.getInstance().createVuforia(parameters); //  start the Vuforia engine (vroom vroom!)
//
//        targets = this.vuforia.loadTrackablesFromAsset("FreightFrenzyDB"); // load the data sets for the trackable objects from db
//        // for convenience, gather together all the trackable objects in one easily-iterable collection
//        List<VuforiaTrackable> allTrackables = new ArrayList<VuforiaTrackable>();
//        RobotLog.i(getClass().getName(), "allTrackables: " + allTrackables); // log the trackables
//        allTrackables.addAll(targets);
//
//        // targets [⌖]
//        // trucks
//        identifyTarget(0, "Red Storage", -Consts.HALF_FIELD, -Consts.ONE_AND_HALF_TILE, Consts.MM_TARGET_HEIGHT, 90, 0, 90);
//        // truck
//        identifyTarget(1, "Red Alliance Wall", Consts.HALF_TILE, -Consts.HALF_FIELD, Consts.MM_TARGET_HEIGHT, 90, 0, 180);
//        // boat
//        identifyTarget(2, "Blue Storage", -Consts.HALF_FIELD, Consts.ONE_AND_HALF_TILE, Consts.MM_TARGET_HEIGHT, 90, 0, 90);
//        // airplane
//        identifyTarget(3, "Blue Alliance Wall", Consts.HALF_TILE, -Consts.HALF_FIELD, Consts.MM_TARGET_HEIGHT, 90, 0, 0);
//        targets.get(4).setName("Custom Spring");
//
//        /**
//         * A transformation matrix should be created for localization
//         * It's quite simple really: https://en.wikipedia.org/wiki/Transformation_matrix (well not for me, but maybe for the math nerds)
//         * LOOKING AT YOU SANIYAH JAMES AND ANTONIN
//         *
//         * If you are standing in the Red Alliance Station looking towards the center of the field,
//         *     - The X axis runs from your left to the right. (positive from the center to the right)
//         *     - The Y axis runs from the Red Alliance Station towards the other side of the field
//         *       where the Blue Alliance Station is. (Positive is from the center, towards the BlueAlliance station)
//         *     - The Z axis runs from the floor, upwards towards the ceiling.  (Positive is above the floor)
//         *
//         * Before being transformed, each target image is conceptually located at the origin of the field's
//         *  coordinate system (the center of the field), facing up.
//        */
//
//        OpenGLMatrix cameraLocationOnRobot = OpenGLMatrix
//                    .translation(Consts.CAMERA_FORWARD_DISPLACEMENT, Consts.CAMERA_LEFT_DISPLACEMENT, Consts.CAMERA_VERTICAL_DISPLACEMENT)
//                    .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XZY, DEGREES, 90, 90, 0));
//
//        // lets the trackables know where the webcam is
//        for (VuforiaTrackable trackable : allTrackables) {
//            ((VuforiaTrackableDefaultListener) trackable.getListener()).setCameraLocationOnRobot(parameters.cameraName, cameraLocationOnRobot);
//        }
//
//        waitForStart();
//
//        targets.activate();
//        while (opModeIsActive()) {
//            // check all the trackable targets to see which one (if any) is visible
//            targetVisible = false;
//            for (VuforiaTrackable trackable : allTrackables) {
//                if (((VuforiaTrackableDefaultListener)trackable.getListener()).isVisible()) {
//                    telemetry.addData("Visible Target", trackable.getName());
//                    targetVisible = true;
//
//                    target = ((VuforiaTrackableDefaultListener)trackable.getListener()).getVuforiaCameraFromTarget(); // gets the raw of the trackable
//
//                    if (target != null) {
//                        VectorF trans = target.getTranslation();
//                        telemetry.addData("Translation", trans);
//
//                        // extract the X & Y components of the offset of the target relative to the robot
//                        targetX = trans.get(0) / Consts.MM_PER_INCH; // target X axis
//                        targetY = trans.get(2) / Consts.MM_PER_INCH; // target Y axis
//
//                        break;
//                    }
//                }
//            }
//
//            // provide feedback as to where the robot is located (if we know)
//            if (targetVisible) {
//                // express position (translation) of robot in inches
//                telemetry.addData("Pos (inches)", "{X, Y} = %.1f, %.1f", targetX, targetY);
//            }
//            else {
//                telemetry.addData("Visible Target", "none");
//            }
//            telemetry.update();
//        }
//
//        targets.deactivate();
//    }
//
//    // identify a target by naming it, and setting its position and orientation on the field
//    void identifyTarget(int targetIndex, String targetName, float dx, float dy, float dz, float rx, float ry, float rz) {
//        VuforiaTrackable aTarget = targets.get(targetIndex);
//        aTarget.setName(targetName);
//        aTarget.setLocation(OpenGLMatrix.translation(dx, dy, dz).multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, rx, ry, rz)));
//    }
//}
