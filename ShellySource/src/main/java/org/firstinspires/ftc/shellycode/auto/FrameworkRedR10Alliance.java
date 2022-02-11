package org.firstinspires.ftc.shellycode.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.shellycode.Consts;
import org.firstinspires.ftc.shellycode.Shelly;

import java.util.ArrayList;
import java.util.List;

@Autonomous(name = "Framework Red R10 Alliance")
public class FrameworkRedR10Alliance extends OpMode {
    private Shelly shelly;
    private TFObjectDetector tfod;

    private ArrayList<Motion> motions = new ArrayList<>();
    private int stage = 0;

    private double cameraCenter = 0;
    private int barcodePos = 0;

    @Override
    public void init() {
        shelly = new Shelly(hardwareMap, telemetry);
        shelly.assignHardware();

        telemetry.addData(Consts.TELEM_LOG_LEVELS[1], "Start around left side of barcode");

        VuforiaLocalizer vulo = shelly.getVulo();
        int tfId = hardwareMap.appContext.getResources().getIdentifier("tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        tfod = shelly.getTflite(Consts.TFL_OD_MODEL, Consts.TFL_OD_LABELS, 0.68f, 512, 1, tfId, vulo);

        cameraCenter = vulo.getCameraCalibration().getSize().getWidth() / 2.0;

        shelly.claw.setPosition(Consts.CLAW_MAX);

        motions.add(new Motion() {
            @Override
            public boolean isEnd() {
                return runtime.seconds() > 10;
            }

            @Override
            public void init() {
                runtime.reset();
                telemetry.addData("Waiting...", "hello there");
            }

            @Override
            public void run() { }

            @Override
            public void cleanup() { }
        });

        motions.add(new Motion() { // drive diagonal towards, drop, and go back
            @Override
            public boolean isEnd() {
                return !shelly.left.isBusy();
            }

            @Override
            public void init()
            {
                shelly.holdArm(Consts.ARM_LEVELS[barcodePos]);
                shelly.driveInches(-12, 19 + barcodePos, 650);
            }

            @Override
            public void run() {}
            @Override
            public void cleanup() {  }
        });

        motions.add(new Motion() {

            @Override
            public boolean isEnd() {
                return runtime.seconds() > 1;
            }

            @Override
            public void init() {
                runtime.reset();
                shelly.claw.setPosition(Consts.CLAW_MIN);
            }

            @Override
            public void run() { }

            @Override
            public void cleanup() { shelly.holdArm((Consts.ARM_LEVELS[2])); }
        });


        motions.add(new Motion() { // go back
            @Override
            public boolean isEnd() {
                return !shelly.left.isBusy();
            }

            @Override
            public void init()
            {
                shelly.driveInches(0, -3, 800);
            }

            @Override
            public void run() {}
            @Override
            public void cleanup() { }
        });

        motions.add(new Motion() { // go back
            @Override
            public boolean isEnd() {
                return !shelly.front.isBusy();
            }

            @Override
            public void init()
            {
                shelly.driveInches(48, -23 - barcodePos, 1500);
            }

            @Override
            public void run() {}
            @Override
            public void cleanup() { }
        });
    }

    @Override
    public void init_loop() {
        telemetry.addData("Distance Back", shelly.backds.getDistance(DistanceUnit.INCH));

        if (tfod == null) { return; }

        List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
        if (updatedRecognitions != null) {
            telemetry.addData("Objects detected", updatedRecognitions.size());

            for (Recognition recognition : updatedRecognitions) {
                telemetry.addData("Label", recognition.getLabel());
                telemetry.addData("Confidence", recognition.getConfidence());
                telemetry.addData("Top Left","(%.2f, %.2f)", recognition.getLeft(), recognition.getTop());
                telemetry.addData("Bottom Right", "(%.2f, %.2f)", recognition.getRight(), recognition.getBottom());
                telemetry.addData("-------", "-------");

                double boxAverage = (recognition.getLeft() + recognition.getRight()) / 2;

                barcodePos = (boxAverage < cameraCenter - Consts.AUTO_CAM_BARCODE_OFFSETS) ? 0 : (boxAverage > cameraCenter + Consts.AUTO_CAM_BARCODE_OFFSETS ? 2 : 1);
                telemetry.addData("Barcode pos", barcodePos);
            }
        }
    }

    @Override
    public void start() {
        motions.get(0).init();
    }

    @Override
    public void loop() {
        motions.get(stage).run();

        if (motions.get(stage).isEnd()) {
            motions.get(stage).cleanup();
            if (stage < motions.size() - 1) motions.get(++stage).init();
            else stop();
        }
    }
}
