package org.firstinspires.ftc.shellycode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

@Autonomous(name = "ShellyOp Deg Test")
public class ShellyOpDegTest extends OpMode {
    private Shelly shelly;

    @Override
    public void init() {
        shelly = new Shelly(hardwareMap, telemetry);
        shelly.assignHardware();
    }

    @Override
    public void start() {
        shelly.turnDeg(90, 150);
    }

    @Override
    public void loop() {
        if (!shelly.left.isBusy()) shelly.turnDeg(-90, 150);
    }
}
