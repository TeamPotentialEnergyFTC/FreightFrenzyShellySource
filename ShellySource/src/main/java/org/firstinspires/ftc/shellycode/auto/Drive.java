package org.firstinspires.ftc.shellycode.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.shellycode.Shelly;

@Autonomous(name = "Drive")
public class Drive extends OpMode {
    private Shelly shelly;

    @Override
    public void init() {
        shelly = new Shelly(hardwareMap, telemetry);
    }

    @Override
    public void start() {
        shelly.driveInches(0, -10, 0);
    }

    @Override
    public void loop() {

    }
}
