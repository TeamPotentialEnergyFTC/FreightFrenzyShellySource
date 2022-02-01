//package org.firstinspires.ftc.shellycode.auto;
//
//import com.qualcomm.robotcore.hardware.HardwareMap;
//import com.qualcomm.robotcore.util.ElapsedTime;
//
//import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
//import org.firstinspires.ftc.shellycode.Consts;
//import org.firstinspires.ftc.shellycode.Shelly;
//import org.firstinspires.ftc.shellycode.utils.Motors;
//import org.firstinspires.ftc.shellycode.utils.TFLiteHelper;
//import org.firstinspires.ftc.shellycode.utils.Utils;
//import org.firstinspires.ftc.shellycode.utils.VuHelper;
//
//import java.util.List;
//
//public class BlockTFLITE {
//    private Shelly shelly;
//
//    private TFLiteHelper tfLiteHelper;
//    private VuHelper vuHelper;
//
//    private int barcodePos = 0;
//    private double cameraCenter = 0;
//    private double boxAverage = 0;
//
//    private static final String[] LABELS = {
//            "tse"
//    };
//
//    // prop drilling ...(*￣０￣)ノ
//    public BlockTFLITE(HardwareMap hm) {
//        vuHelper = new VuHelper(hm);
//        vuHelper.loadWallTargets();
//
//        // what is tfId, why why why
//        int tfId = hm.appContext.getResources().getIdentifier("tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
//        tfLiteHelper = new TFLiteHelper(Consts.TFL_OD_MODEL, LABELS, 0.65f, 512, 1, tfId, vuHelper.vulo);
//
//        cameraCenter = vuHelper.vulo.getCameraCalibration().getSize().getWidth() / 2.0;
//    }
//
//    public void do_loop(boolean isRed) {
//        shelly.claw.setPosition(Consts.CLAW_MAX); // grab
//
//        if (tfLiteHelper.tfod == null) { return; }
//
//        // align right wheel of intake to tile line :D
//        List<Recognition> updatedRecognitions = tfLiteHelper.tfod.getUpdatedRecognitions();
//        if (updatedRecognitions != null) {
//            telemetry.addData("Objects detected", updatedRecognitions.size());
//
//            for (Recognition recognition : updatedRecognitions) {
//                telemetry.addData("Label", recognition.getLabel());
//                telemetry.addData("Confidence", recognition.getConfidence());
//                telemetry.addData("Top Left","(%.2f, %.2f)", recognition.getLeft(), recognition.getTop());
//                telemetry.addData("Bottom Right", "(%.2f, %.2f)", recognition.getRight(), recognition.getBottom());
//                telemetry.addData("-------", "-------");
//
//                boxAverage = (recognition.getLeft() + recognition.getRight()) / 2;
//
//                barcodePos = (boxAverage < cameraCenter - Consts.AUTO_CAM_BARCODE_OFFSETS) ? 0 : (boxAverage > cameraCenter + Consts.AUTO_CAM_BARCODE_OFFSETS ? 2 : 1);
//                telemetry.addData("Barcode pos", barcodePos);
//            }
//        }
//    }
//
//    @Override
//    public void start() {
//        runtime.reset();
//
//        motors.hold(motors.arm, Consts.ARM_LEVELS[barcodePos]); // arm
//
//        // forward
//        motors.rfd.setPower(-Consts.AUTO_DEF_SPED);
//        motors.lbd.setPower(-Consts.AUTO_DEF_SPED);
//    }
//
//    @Override
//    public void loop() {
//        double ms = runtime.milliseconds();
////        if (Utils.inTolerantRange(ms, 500, Consts.AUTO_MS_TOLERANCE)) motors.hold(motors.arm, Consts.ARM_LEVELS[barcodePos]); // arm
//        if (Utils.inTolerantRange(ms, 1400, Consts.AUTO_MS_TOLERANCE)) {
//
//            motors.rfd.setPower(0);
//            motors.lbd.setPower(0);
//
//            // left
//            motors.lfd.setPower(-Consts.AUTO_DEF_SPED);
//            motors.rbd.setPower(-Consts.AUTO_DEF_SPED);
//        }
//        else if (Utils.inTolerantRange(ms, 2600, Consts.AUTO_MS_TOLERANCE)) {
//            motors.lfd.setPower(0);
//            motors.rbd.setPower(0);
//
//            // forward
//            motors.rfd.setPower(-Consts.AUTO_DEF_SPED);
//            motors.lbd.setPower(-Consts.AUTO_DEF_SPED);
//        }
//        else if (Utils.inTolerantRange(ms, 3400, Consts.AUTO_MS_TOLERANCE)) {
//            motors.rfd.setPower(0);
//            motors.lbd.setPower(0);
//
//            motors.claw.setPosition(Consts.CLAW_MIN); // drop
//        }
//        else if (Utils.inTolerantRange(ms, 4500, Consts.AUTO_MS_TOLERANCE)) {
//            // backwards
//            motors.rfd.setPower(Consts.AUTO_DEF_SPED);
//            motors.lbd.setPower(Consts.AUTO_DEF_SPED);
//        }
//        else if (Utils.inTolerantRange(ms, 5200, Consts.AUTO_MS_TOLERANCE)) {
//            // right
//            motors.lfd.setPower(Consts.AUTO_DEF_SPED);
//            motors.rbd.setPower(Consts.AUTO_DEF_SPED);
//        }
//        else if (Utils.inTolerantRange(ms, 8000, Consts.AUTO_MS_TOLERANCE)) {
//            // less back
//            motors.rfd.setPower(Consts.AUTO_DEF_SPED / 3);
//            motors.lbd.setPower(Consts.AUTO_DEF_SPED / 3);
//        }
//        else if (Utils.inTolerantRange(ms, 11000, Consts.AUTO_MS_TOLERANCE)) {
//            motors.stopAll();
//        }
//    }
//}
