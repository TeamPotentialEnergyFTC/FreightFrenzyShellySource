package org.firstinspires.ftc.shellycode.archive;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.shellycode.Consts;
import org.firstinspires.ftc.shellycode.utils.ButtonState;
import org.firstinspires.ftc.shellycode.utils.Motors;

@TeleOp(name="Test Op 0", group="Test")
@Disabled
public class TestOp0 extends OpMode {
    // put whatever test stuff you want here
    private Motors motors;

    private ElapsedTime runtime;

    private double xdir;
    private double ydir;
    private double turn;

    private double turboGain;
    private double snailGain;

    private int clawCoefficient = 1;

    ButtonState b;
    ButtonState x;

    @Override
    public void init() {
        motors = new Motors(hardwareMap);

        b = new ButtonState();
        x = new ButtonState();
    }

    @Override
    public void loop() {
        b.update(gamepad2.b);
        x.update(gamepad2.x);

        turboGain = org.firstinspires.ftc.shellycode.Consts.DEF_SPED + Range.clip(gamepad1.left_trigger, 0, 1);
        snailGain = Range.clip(org.firstinspires.ftc.shellycode.Consts.DEF_SPED - Range.clip(gamepad1.right_trigger, 0, org.firstinspires.ftc.shellycode.Consts.DEF_SPED), org.firstinspires.ftc.shellycode.Consts.MIN_SPED, 1);

        turn = gamepad1.right_stick_x * snailGain * turboGain * 0.5;
        xdir = gamepad1.left_stick_x * snailGain * turboGain;
        ydir = gamepad1.left_stick_y * snailGain * turboGain;

        motors.lbd.setPower(ydir - turn);
        motors.rfd.setPower(ydir + turn);
        motors.lfd.setPower(xdir + turn);
        motors.rbd.setPower(xdir - turn);

        telemetry.addData("Turbo/Snail Telem", "turbo (%.2f), snail (%.2f)", turboGain, snailGain);
        telemetry.addData("Drive Telem", "xdir (%.2f), ydir (%.2f), turn (%.2f), gains (%.2f)", xdir, ydir, turn, snailGain * turboGain);

        // ---
        // quackapult + arm + claw
        // ---
        motors.quackapult.setPower(gamepad2.right_stick_x);

        if (gamepad2.left_stick_y == 0) {
            motors.arm.setTargetPosition(motors.arm.getCurrentPosition());
            motors.arm.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
            motors.arm.setVelocity(org.firstinspires.ftc.shellycode.Consts.ARM_VEL);
        }
        else {
            telemetry.addData("setting power", "(%.2f)", gamepad2.left_stick_y);
            motors.arm.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
            motors.arm.setPower(gamepad2.left_stick_y * org.firstinspires.ftc.shellycode.Consts.ARM_GAIN);
        }

        motors.claw.setPosition(Range.clip(gamepad2.right_trigger, org.firstinspires.ftc.shellycode.Consts.CLAW_MIN, org.firstinspires.ftc.shellycode.Consts.CLAW_MAX)); // min and max to not grind servo

        clawCoefficient = x.buttonState ? 1 : -1;
        motors.spinny.setPower(b.buttonState ? 0 : clawCoefficient * Range.clip(Consts.CLAW_MAX - gamepad2.right_trigger, 0, 1)); // speed is controlled by the claw

        telemetry.addData("spinny power",  "%.2f", motors.spinny.getPower());

        telemetry.addData("Arm Encoder Pos, Arm Target Pos",  "%d, %d", motors.arm.getCurrentPosition(), motors.arm.getTargetPosition());
    }
}
