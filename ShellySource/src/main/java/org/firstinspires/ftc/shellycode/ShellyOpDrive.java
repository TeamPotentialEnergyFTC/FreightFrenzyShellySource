package org.firstinspires.ftc.shellycode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.shellycode.utils.ButtonState;

@TeleOp(name = "ShellyOp OmniDrive")
public class ShellyOpDrive extends OpMode {
    private Shelly shelly;

    private final ButtonState b = new ButtonState();
    private final ButtonState x = new ButtonState();
    private final ButtonState rb = new ButtonState();

    private int targetArmPosition, currentTargetArmPosition;
    private int clawCoefficient;

    @Override
    public void init() {
        shelly = new Shelly(hardwareMap, telemetry);
        shelly.assignHardware();
    }

    @Override
    public void loop() {
        // ---
        // motor controls
        // ---

        b.update(gamepad2.b);
        x.update(gamepad2.x);
        rb.update(gamepad2.right_bumper);

        // ---
        // drive
        // ---

        double turboGain = Consts.DEF_SPED + Range.clip(gamepad1.left_trigger, 0, 1);
        double snailGain = Range.clip(Consts.DEF_SPED - Range.clip(gamepad1.right_trigger, 0, Consts.DEF_SPED), Consts.MIN_SPED, 1);

        double xdir = gamepad1.left_stick_x * snailGain * turboGain;
        double ydir = gamepad1.left_stick_y * snailGain * turboGain;
        double turn = gamepad1.right_stick_x * snailGain * turboGain * 0.5;

        shelly.driveTicks((int)(xdir * Consts.TICKS_PER_POWER), -(int)(ydir * Consts.TICKS_PER_POWER), (int)(turn * Consts.TICKS_PER_POWER));

        telemetry.addData("Turbo/Snail Telem", "turbo (%.2f), snail (%.2f)", turboGain, snailGain);
        telemetry.addData("Drive Telem", "xdir (%.2f), ydir (%.2f), turn (%.2f), gains (%.2f)", xdir, ydir, turn, snailGain * turboGain);

        // ---
        // quackapult + arm + claw
        // ---
        shelly.quackapult.setPower(gamepad2.right_stick_x);

        if (gamepad2.dpad_down) {
            targetArmPosition = Consts.ARM_LEVELS[0];
        } else if (gamepad2.dpad_left) {
            targetArmPosition = Consts.ARM_LEVELS[1];
        } else if (gamepad2.dpad_right) {
            targetArmPosition = Consts.ARM_LEVELS[2];
        } else if (gamepad2.dpad_up) {
            targetArmPosition = Consts.ARM_LEVELS[3];
        } else if (gamepad2.left_stick_y == 0 && targetArmPosition == 0) {
            targetArmPosition = shelly.arm.getCurrentPosition();
        }

        // if limit is pressed then left stick y must be less than 0 to go up
        if (gamepad2.left_stick_y != 0 && (!shelly.limit.isPressed() || gamepad2.left_stick_y > 0)) {
            targetArmPosition = 0;
            currentTargetArmPosition = -1;
            telemetry.addData("setting power", "(%.2f)", gamepad2.left_stick_y);
            shelly.arm.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
            shelly.arm.setPower(gamepad2.left_stick_y * Consts.ARM_GAIN);
        }
        else {
            // do not set a new position if it's already being held at the target
            if (targetArmPosition != currentTargetArmPosition) {
                shelly.holdArm(targetArmPosition);
                currentTargetArmPosition = targetArmPosition;
            }
        }

        shelly.claw.setPosition(rb.buttonState ? Consts.CLAW_MAX : Consts.CLAW_MIN);

        clawCoefficient = x.buttonState ? -1 : 1;
        shelly.spinny.setPower(b.buttonState ? clawCoefficient * (rb.buttonState ? 0 : 1) : 0); // speed is controlled by the claw

        telemetry.addData("spinny power", "%.2f", shelly.spinny.getPower());
        telemetry.addData("Arm Encoder Pos, Arm Target Pos", "%d, %d", shelly.arm.getCurrentPosition(), shelly.arm.getTargetPosition());

        if (gamepad2.back && !shelly.limit.isPressed()) {
            telemetry.addData("BACK!", "back");

            // move arm till limit switch here!!!
            shelly.arm.setPower(Consts.AUTO_DEF_SPED);
        }
        else if (shelly.limit.isPressed()) {
            shelly.arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        }

//        telemetry.addData(Consts.TELEM_LOG_LEVELS[0], "LDS: %s BDS: %s RDS: %s", shelly.dsl.getDistance(DistanceUnit.MM), shelly.bds.getDistance(DistanceUnit.MM), shelly.rds.getDistance(DistanceUnit.MM));
    }
}
