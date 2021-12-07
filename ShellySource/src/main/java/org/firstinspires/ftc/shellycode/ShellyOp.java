package org.firstinspires.ftc.shellycode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="ShellyOp", group="TellyOp")
public class ShellyOp extends OpMode {
    private Motors motors;
    private IntervalPhotos intervalPhotos;

    private ElapsedTime elapsed;

    private double xdir;
    private double ydir;
    private double turn;

    private double turboGain;
    private double snailGain;

    private boolean bDownLastUpdate = false;
    private boolean spinnyOptout = false;

    private int armPos;

    @Override
    public void init() {
        motors = new Motors(hardwareMap);
        intervalPhotos = new IntervalPhotos(hardwareMap, telemetry, 5);
    }

    @Override
    public void start() { elapsed.reset(); }

    @Override
    public void loop() {
        intervalPhotos.update();

        // ---
        // motor controls
        // ---

        // toggle spinny opt out
        if (gamepad2.b && !bDownLastUpdate) {
            spinnyOptout = !spinnyOptout;
            bDownLastUpdate = true;
        }
        else if (!gamepad2.b && bDownLastUpdate) {
            bDownLastUpdate = false;
        }

        // ---
        // drive
        // ---

        turboGain = Consts.DEF_SPED + Range.clip(gamepad1.left_trigger, 0, 1);
        snailGain = Range.clip(Consts.DEF_SPED - Range.clip(gamepad1.right_trigger, 0, Consts.DEF_SPED), Consts.MIN_SPED, 1);

        turn = gamepad1.right_stick_x * snailGain * turboGain * 0.5;
        xdir = gamepad1.left_stick_x * snailGain * turboGain;
        ydir = gamepad1.left_stick_y * snailGain * turboGain;

        motors.lbd.setPower(ydir - turn);
        motors.rfd.setPower(ydir + turn);
        motors.lfd.setPower(xdir + turn);
        motors.rbd.setPower(xdir - turn);

        telemetry.addData("Turbo/Snail Telem: ", "turbo (%.2f), snail", turboGain, snailGain);
        telemetry.addData("Drive Telem: ", "xdir (%.2f), ydir (%.2f), turn (%.2f), gains (%.2f)", xdir, ydir, turn, snailGain * turboGain);

        // ---
        // quackapult + arm + claw
        // ---
        motors.quackapult.setPower(gamepad2.right_stick_x);

        armPos += Math.round(gamepad2.left_stick_y * 100); // should return a %-like value which is actually adding that many ticks to move
        motors.arm.setTargetPosition(armPos);
        motors.arm.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        motors.arm.setVelocity(Consts.ARM_VEL);

        motors.claw.setPosition(Range.clip(gamepad2.right_trigger, Consts.CLAW_MIN, Consts.CLAW_MAX)); // min and max to not grind servo
        motors.spinny.setPower(spinnyOptout ? 0 : Range.clip(Consts.CLAW_MAX - gamepad2.right_trigger, 0, 1)); // speed is controlled by the claw

        telemetry.addData("Attachment Telem", "Right Stick [x] (%.2f), Left Stick [y] (%.2f), Claw Pos (%.2f)", gamepad2.right_stick_x, gamepad2.left_stick_y, motors.claw.getPosition());
        telemetry.addData("Arm Encoder Pos, Arm Target Pos",  "(%.2f), (%.2f)", motors.arm.getCurrentPosition(), motors.arm.getTargetPosition());

        telemetry.addData("Elapsed Time",  "(%.2f)", elapsed.toString());
    }
}
