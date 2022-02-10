package org.firstinspires.ftc.shellycode.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.shellycode.Consts;
import org.firstinspires.ftc.shellycode.Shelly;

import java.util.ArrayList;
import java.util.List;

@Autonomous(name = "Framework Victory Lap")
@Disabled
public class FrameworkAutoGenTest extends OpMode {
    private Shelly shelly;

    private ArrayList<Motion> motions = new ArrayList<>();
    private int stage = 0;

    @Override
    public void init() {
        shelly = new Shelly(hardwareMap, telemetry);
        shelly.assignHardware();

        motions.add(new Motion() { // name me!
            @Override
            public boolean isEnd() {
                return !shelly.lbd.isBusy();
            }

            @Override
            public void init()
            {
                shelly.turnDeg(-85.51078006072521, 1000);
            }

            @Override
            public void run() {}
            @Override
            public void cleanup() {}
        });

        motions.add(new Motion() { // name me!
            @Override
            public boolean isEnd() {
                return !shelly.lbd.isBusy();
            }

            @Override
            public void init()
            {
                shelly.driveInches(0, 49.53973915893206, 1000);
            }

            @Override
            public void run() {}
            @Override
            public void cleanup() {}
        });

        motions.add(new Motion() { // name me!
            @Override
            public boolean isEnd() {
                return !shelly.lbd.isBusy();
            }

            @Override
            public void init()
            {
                shelly.turnDeg(-48.50825353252884, 1000);
            }

            @Override
            public void run() {}
            @Override
            public void cleanup() {}
        });

        motions.add(new Motion() { // name me!
            @Override
            public boolean isEnd() {
                return !shelly.lbd.isBusy();
            }

            @Override
            public void init()
            {
                shelly.driveInches(0, 35.26540060455975, 1000);
            }

            @Override
            public void run() {}
            @Override
            public void cleanup() {}
        });

        motions.add(new Motion() { // name me!
            @Override
            public boolean isEnd() {
                return !shelly.lbd.isBusy();
            }

            @Override
            public void init()
            {
                shelly.turnDeg(33.32656474619829, 1000);
            }

            @Override
            public void run() {}
            @Override
            public void cleanup() {}
        });

        motions.add(new Motion() { // name me!
            @Override
            public boolean isEnd() {
                return !shelly.lbd.isBusy();
            }

            @Override
            public void init()
            {
                shelly.driveInches(0, 73.21185020481539, 1000);
            }

            @Override
            public void run() {}
            @Override
            public void cleanup() {}
        });

        motions.add(new Motion() { // name me!
            @Override
            public boolean isEnd() {
                return !shelly.lbd.isBusy();
            }

            @Override
            public void init()
            {
                shelly.turnDeg(35.734169258173935, 1000);
            }

            @Override
            public void run() {}
            @Override
            public void cleanup() {}
        });

        motions.add(new Motion() { // name me!
            @Override
            public boolean isEnd() {
                return !shelly.lbd.isBusy();
            }

            @Override
            public void init()
            {
                shelly.driveInches(0, 34.074299589936885, 1000);
            }

            @Override
            public void run() {}
            @Override
            public void cleanup() {}
        });

        motions.add(new Motion() { // name me!
            @Override
            public boolean isEnd() {
                return !shelly.lbd.isBusy();
            }

            @Override
            public void init()
            {
                shelly.turnDeg(52.32557354875661, 1000);
            }

            @Override
            public void run() {}
            @Override
            public void cleanup() {}
        });

        motions.add(new Motion() { // name me!
            @Override
            public boolean isEnd() {
                return !shelly.lbd.isBusy();
            }

            @Override
            public void init()
            {
                shelly.driveInches(0, 74.11563845363712, 1000);
            }

            @Override
            public void run() {}
            @Override
            public void cleanup() {}
        });

        motions.add(new Motion() { // name me!
            @Override
            public boolean isEnd() {
                return !shelly.lbd.isBusy();
            }

            @Override
            public void init()
            {
                shelly.turnDeg(-87.3691217008604, 1000);
            }

            @Override
            public void run() {}
            @Override
            public void cleanup() {}
        });

        motions.add(new Motion() { // name me!
            @Override
            public boolean isEnd() {
                return !shelly.lbd.isBusy();
            }

            @Override
            public void init()
            {
                shelly.driveInches(0, 26.12563756049838, 1000);
            }

            @Override
            public void run() {}
            @Override
            public void cleanup() {}
        });

        motions.add(new Motion() { // name me!
            @Override
            public boolean isEnd() {
                return !shelly.lbd.isBusy();
            }

            @Override
            public void init()
            {
                shelly.turnDeg(84.17771634213997, 1000);
            }

            @Override
            public void run() {}
            @Override
            public void cleanup() {}
        });

        motions.add(new Motion() { // name me!
            @Override
            public boolean isEnd() {
                return !shelly.lbd.isBusy();
            }

            @Override
            public void init()
            {
                shelly.driveInches(0, 23.761272780127154, 1000);
            }

            @Override
            public void run() {}
            @Override
            public void cleanup() {}
        });

        motions.add(new Motion() { // name me!
            @Override
            public boolean isEnd() {
                return !shelly.lbd.isBusy();
            }

            @Override
            public void init()
            {
                shelly.turnDeg(76.23107549215908, 1000);
            }

            @Override
            public void run() {}
            @Override
            public void cleanup() {}
        });

        motions.add(new Motion() { // name me!
            @Override
            public boolean isEnd() {
                return !shelly.lbd.isBusy();
            }

            @Override
            public void init()
            {
                shelly.driveInches(0, 9.294120438151591, 1000);
            }

            @Override
            public void run() {}
            @Override
            public void cleanup() {}
        });

        motions.add(new Motion() { // name me!
            @Override
            public boolean isEnd() {
                return !shelly.lbd.isBusy();
            }

            @Override
            public void init()
            {
                shelly.turnDeg(7.611726112422332, 1000);
            }

            @Override
            public void run() {}
            @Override
            public void cleanup() {}
        });

        motions.add(new Motion() { // name me!
            @Override
            public boolean isEnd() {
                return !shelly.lbd.isBusy();
            }

            @Override
            public void init()
            {
                shelly.driveInches(0, 66.54594790073621, 1000);
            }

            @Override
            public void run() {}
            @Override
            public void cleanup() {}
        });

        motions.add(new Motion() { // name me!
            @Override
            public boolean isEnd() {
                return !shelly.lbd.isBusy();
            }

            @Override
            public void init()
            {
                shelly.turnDeg(-92.13637221566034, 1000);
            }

            @Override
            public void run() {}
            @Override
            public void cleanup() {}
        });

        motions.add(new Motion() { // name me!
            @Override
            public boolean isEnd() {
                return !shelly.lbd.isBusy();
            }

            @Override
            public void init()
            {
                shelly.driveInches(0, 27.80307978110433, 1000);
            }

            @Override
            public void run() {}
            @Override
            public void cleanup() {}
        });

        motions.add(new Motion() { // name me!
            @Override
            public boolean isEnd() {
                return !shelly.lbd.isBusy();
            }

            @Override
            public void init()
            {
                shelly.turnDeg(-90.59624486622971, 1000);
            }

            @Override
            public void run() {}
            @Override
            public void cleanup() {}
        });

        motions.add(new Motion() { // name me!
            @Override
            public boolean isEnd() {
                return !shelly.lbd.isBusy();
            }

            @Override
            public void init()
            {
                shelly.driveInches(0, 25.335729882185294, 1000);
            }

            @Override
            public void run() {}
            @Override
            public void cleanup() {}
        });

        motions.add(new Motion() { // name me!
            @Override
            public boolean isEnd() {
                return !shelly.lbd.isBusy();
            }

            @Override
            public void init()
            {
                shelly.turnDeg(-88.69858491718574, 1000);
            }

            @Override
            public void run() {}
            @Override
            public void cleanup() {}
        });

        motions.add(new Motion() { // name me!
            @Override
            public boolean isEnd() {
                return !shelly.lbd.isBusy();
            }

            @Override
            public void init()
            {
                shelly.driveInches(0, 23.885399024242236, 1000);
            }

            @Override
            public void run() {}
            @Override
            public void cleanup() {}
        });

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
