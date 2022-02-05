package org.firstinspires.ftc.shellycode.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.shellycode.Shelly;

@Autonomous(name = "Test Framework")
public class TestFramework extends OpMode {
    private Shelly shelly;

    private int stage = 0;

    private Motion[] motions = new Motion[2];

    @Override
    public void init() {
        shelly = new Shelly(hardwareMap, telemetry);
        shelly.assignHardware();

        motions[0] = new Motion() {
            @Override
            public boolean isEnd() {
                return !shelly.lbd.isBusy();
            }

            @Override
            public void init() {
                shelly.driveInches(7, 0, 200);
            }

            @Override
            public void run() {}
            @Override
            public void cleanup() {}
        };

        motions[1] = new Motion() {
            @Override
            public boolean isEnd() {
                return shelly.lbd.isBusy();
            }

            @Override
            public void init() {
                shelly.turnDeg(90, 150);
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
