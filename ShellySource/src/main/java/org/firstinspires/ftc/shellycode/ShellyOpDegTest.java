package org.firstinspires.ftc.shellycode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.shellycode.utils.ButtonState;

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
        if (!shelly.lbd.isBusy()) shelly.turnDeg(-90, 150);
    }
}
