package org.firstinspires.ftc.shellycode.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.shellycode.Shelly;

@Autonomous(name = "Test Framework")
public class TestFramework extends OpMode {
    private Shelly shelly;

    private int stage = 0;

    private Motion[] motions;

    @Override
    public void init() {
        shelly = new Shelly(hardwareMap, telemetry);
        shelly.assignHardware();

        motions[0] = new Motion() {
            @Override
            public boolean isEnd() {
                return true;
            }

            @Override
            public void init() {

            }

            @Override
            public void run() {

            }

            @Override
            public void cleanup() {

            }
        }
    }

    @Override
    public void start() {
    }

    @Override
    public void loop() {
        motions[stage].run();

        if (motions[stage].isEnd()) {
            motions[stage].cleanup();
            if (stage < motions.length) motions[++stage].init();
            else stop();
        }
    }
}
