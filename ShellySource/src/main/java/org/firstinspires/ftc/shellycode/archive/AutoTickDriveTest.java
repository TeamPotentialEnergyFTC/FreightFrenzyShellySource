package org.firstinspires.ftc.shellycode.archive;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.shellycode.Shelly;

@Autonomous(name = "Motion Tick Drive Test")
@Disabled
public class AutoTickDriveTest extends OpMode {
    private Shelly shelly;

    @Override
    public void init() {
        shelly = new Shelly(hardwareMap, telemetry);
        shelly.assignHardware();
    }

    @Override
    public void start() {
        // Set the motor's target position to 400 ticks | 3 & 7/8 | so basically an inch per 100 ticks
        shelly.left.setTargetPosition(400);
        shelly.right.setTargetPosition(400);

        // Switch to RUN_TO_POSITION mode
        shelly.left.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        shelly.right.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // Start the motor moving by setting the max velocity to 200 ticks per second
        shelly.left.setVelocity(200);
        shelly.right.setVelocity(200);
    }

    @Override
    public void loop() {
        telemetry.addData("Operation in proress", shelly.left.isBusy());
    }
}
