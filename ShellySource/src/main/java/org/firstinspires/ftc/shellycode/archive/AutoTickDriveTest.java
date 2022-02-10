package org.firstinspires.ftc.shellycode.archive;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.shellycode.Shelly;
import org.firstinspires.ftc.shellycode.utils.ButtonState;

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
        shelly.lbd.setTargetPosition(400);
        shelly.rfd.setTargetPosition(400);

        // Switch to RUN_TO_POSITION mode
        shelly.lbd.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        shelly.rfd.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // Start the motor moving by setting the max velocity to 200 ticks per second
        shelly.lbd.setVelocity(200);
        shelly.rfd.setVelocity(200);
    }

    @Override
    public void loop() {
        telemetry.addData("Operation in proress", shelly.lbd.isBusy());
    }
}
