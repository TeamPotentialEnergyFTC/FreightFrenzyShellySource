package org.firstinspires.ftc.shellycode.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.shellycode.Consts;
import org.firstinspires.ftc.shellycode.Shelly;

@Autonomous(name = "Framework Red L1")
public class FrameworkRedL1 extends OpMode {
    private Shelly shelly;

    private int stage = 0;

    private Motion[] motions = new Motion[7];

    @Override
    public void init() {
        shelly = new Shelly(hardwareMap, telemetry);
        shelly.assignHardware();

        telemetry.addData(Consts.TELEM_LOG_LEVELS[1], "Start around left side of barcode");

        motions[0] = new Motion() { // drive forward off wall
            @Override
            public boolean isEnd() {
                return !shelly.rfd.isBusy();
            }

            @Override
            public void init()
            {
                shelly.driveInches(0, 6, 600);
            }

            @Override
            public void run() {}
            @Override
            public void cleanup() {}
        };

        motions[1] = new Motion() { // spin for quackapult
            @Override
            public boolean isEnd() {
                return !shelly.lbd.isBusy();
            }

            @Override
            public void init() {
                shelly.turnDeg(90, 600);
            }

            @Override
            public void run() {}
            @Override
            public void cleanup() {}
        };

        motions[2] = new Motion() { // back into quackapult
            @Override
            public boolean isEnd() {
                return !shelly.lbd.isBusy();
            }

            @Override
            public void init()
            {
                shelly.driveInches(0, -14, 1000);
            }

            @Override
            public void run() {}
            @Override
            public void cleanup() {}
        };

        motions[3] = new Motion() { // spin quackapult
            @Override
            public boolean isEnd() {
                return runtime.seconds() > 5;
            }

            @Override
            public void init() {
                runtime.reset();
                shelly.quackapult.setPower(-1.5*Consts.AUTO_DEF_SPED);
            }

            @Override
            public void run() { }

            @Override
            public void cleanup() { shelly.quackapult.setPower(0); }
        };

        motions[4] = new Motion() { // drive left away from quackapult
            @Override
            public boolean isEnd() { return !shelly.lfd.isBusy(); }

            @Override
            public void init() {
                shelly.driveInches(-6, 0, 1000);
            }

            @Override
            public void run() { }

            @Override
            public void cleanup() { }
        };

        motions[5] = new Motion() { // back into audience wall
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
            public void cleanup() {}
        };

        motions[6] = new Motion() { // left to line up with hub
            @Override
            public boolean isEnd() {
                return !shelly.lbd.isBusy();
            }

            @Override
            public void init() {
                shelly.driveInches(-24, 0, 2000);
            }

            @Override
            public void run() {}
            @Override
            public void cleanup() {}
        };
    }

    @Override
    public void start() {
        motions[0].init();
    }

    @Override
    public void loop() {
        motions[stage].run();

        if (motions[stage].isEnd()) {
            motions[stage].cleanup();
            if (stage < motions.length - 1) motions[++stage].init();
            else stop();
        }
    }
}
