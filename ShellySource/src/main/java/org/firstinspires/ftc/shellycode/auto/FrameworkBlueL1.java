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

@Autonomous(name = "Framework Blue L1")
public class FrameworkBlueL1 extends OpMode {
    private Shelly shelly;
    private TFObjectDetector tfod;

    private ArrayList<Motion> motions = new ArrayList<>();
    private int stage = 0;

    private double cameraCenter = 0;
    private int barcodePos = 2;

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

        motions.add(new Motion() { // drive forward off wall
            @Override
            public boolean isEnd() {
                return shelly.backds.getDistance(DistanceUnit.INCH) > 6 + Consts.DSTBACK_OFFSET;
            }

            @Override
            public void init()
            {
                shelly.driveTicks(0, 500, 0);
                shelly.holdArm(Consts.ARM_LEVELS[barcodePos]);
            }

            @Override
            public void run() {}
            @Override
            public void cleanup() {}
        });

        motions.add(new Motion() { // drive diagonal into other wall
            @Override
            public boolean isEnd() {
                return !shelly.rbd.isBusy();
            }

            @Override
            public void init()
            {
                shelly.driveInches(40, 4, 1000);
                shelly.holdArm(Consts.ARM_LEVELS[barcodePos]);
            }

            @Override
            public void run() {}
            @Override
            public void cleanup() {}
        });

        motions.add(new Motion() { // back into quackapult
            @Override
            public boolean isEnd() {
                return !shelly.lbd.isBusy();
            }

            @Override
            public void init()
            {
                shelly.driveInches(0, -5, 1000);
            }

            @Override
            public void run() {}
            @Override
            public void cleanup() {}
        });

        motions.add(new Motion() { // spin quackapult
            @Override
            public boolean isEnd() {
                return runtime.seconds() > 4;
            }

            @Override
            public void init() {
                runtime.reset();
                shelly.quackapult.setPower(1.5*Consts.AUTO_DEF_SPED);
            }

            @Override
            public void run() { }

            @Override
            public void cleanup() { shelly.quackapult.setPower(0); }
        });

        motions.add(new Motion() { // drive fwd away from quackapult
            @Override
            public boolean isEnd() { return !shelly.lbd.isBusy(); }

            @Override
            public void init() {
                shelly.driveInches(0, 32, 1000);
            }

            @Override
            public void run() { }

            @Override
            public void cleanup() { }
        });

        motions.add(new Motion() { // left to make space to turn
            @Override
            public boolean isEnd() {
                return !shelly.lbd.isBusy();
            }

            @Override
            public void init() {
                shelly.driveInches(10, 0, 1000);
            }

            @Override
            public void run() {}
            @Override
            public void cleanup() { }
        });

        motions.add(new Motion() { // turn to face towards alliance hub
            @Override
            public boolean isEnd() {
                return !shelly.lbd.isBusy();
            }

            @Override
            public void init() {
                shelly.turnDeg(-90, 1000);
            }

            @Override
            public void run() {}
            @Override
            public void cleanup() { }
        });

        motions.add(new Motion() { // back into wall
            @Override
            public boolean isEnd() {
                return !shelly.lbd.isBusy();
            }

            @Override
            public void init() {
                shelly.driveInches(0, -10, 1000);
            }

            @Override
            public void run() {}
            @Override
            public void cleanup() { }
        });

        motions.add(new Motion() { // left to line up with hub
            @Override
            public boolean isEnd() {
                return !shelly.lbd.isBusy();
            }

            @Override
            public void init() {
                shelly.driveInches(0, 32 + barcodePos, 1000);
            }

            @Override
            public void run() {}
            @Override
            public void cleanup() { shelly.claw.setPosition(Consts.CLAW_MIN); }
        });

        motions.add(new Motion() { // backwards to line up with storage
            @Override
            public boolean isEnd() {
                return !shelly.lbd.isBusy();
            }

            @Override
            public void init() {
                shelly.driveInches(0, -38, 1000);
            }

            @Override
            public void run() {}
            @Override
            public void cleanup() { }
        });

        motions.add(new Motion() { // left to line up with hub
            @Override
            public boolean isEnd() {
                return !shelly.lfd.isBusy();
            }

            @Override
            public void init() {
                shelly.driveInches(-12, 0, 1000);
            }

            @Override
            public void run() {}
            @Override
            public void cleanup() { shelly.arm.setTargetPosition(10); }
        });
    }

    @Override
    public void init_loop() {
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
