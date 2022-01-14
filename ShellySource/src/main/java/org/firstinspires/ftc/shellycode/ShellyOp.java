package org.firstinspires.ftc.shellycode;

import android.graphics.Bitmap;
import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.shellycode.utils.ButtonState;
import org.firstinspires.ftc.shellycode.utils.Camera;
import org.firstinspires.ftc.shellycode.utils.Motors;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@TeleOp(name="ShellyOpRadioEdit", group="TellyOp")
public class ShellyOp extends OpMode {
    private Motors motors;
    private Camera camera;

    private double xdir;
    private double ydir;
    private double turn;

    private double turboGain;
    private double snailGain;

    private int clawCoefficient = 1;

    private ButtonState b = new ButtonState();
    private ButtonState x = new ButtonState();
    private ButtonState R1 = new ButtonState();

    private int targetArmPosition = 0;

    @Override
    public void init() {
        motors = new Motors(hardwareMap);
        camera = new Camera(hardwareMap);

        // take a photo every 5 seconds
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss@dd_MM_yyyy");
        Runnable captureRunnable = () -> {
            camera.saveBitmap(camera.captureBitmap(), dtf.format(LocalDateTime.now()));
        };

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(captureRunnable, 0, 15, TimeUnit.SECONDS);
    }

    @Override
    public void loop() {
        // ---
        // motor controls
        // ---

        b.update(gamepad2.b);
        x.update(gamepad2.x);
        R1.update(gamepad2.right_bumper);

        // ---
        // drive
        // ---

        turboGain = Consts.DEF_SPED + Range.clip(gamepad1.left_trigger, 0, 1);
        snailGain = Range.clip(Consts.DEF_SPED - Range.clip(gamepad1.right_trigger, 0, Consts.DEF_SPED), Consts.MIN_SPED, 1);

        turn = gamepad1.right_stick_x * snailGain * turboGain * 0.5;
        xdir = gamepad1.left_stick_x * snailGain * turboGain;
        ydir = gamepad1.left_stick_y * snailGain * turboGain;

//        motors.lbd.setPower(ydir - turn);
//        motors.rfd.setPower(ydir + turn);
//        motors.lfd.setPower(xdir + turn);
//        motors.rbd.setPower(xdir - turn);
        motors.drive(ydir, xdir, turn);

        telemetry.addData("Turbo/Snail Telem", "turbo (%.2f), snail (%.2f)", turboGain, snailGain);
        telemetry.addData("Drive Telem", "xdir (%.2f), ydir (%.2f), turn (%.2f), gains (%.2f)", xdir, ydir, turn, snailGain * turboGain);

        // ---
        // quackapult + arm + claw
        // --- Utils.inTolerantRange(motors.arm.getCurrentPosition(), motors.arm.getTargetPosition(), motors.arm.getTargetPositionTolerance())
        motors.quackapult.setPower(gamepad2.right_stick_x);


        if (gamepad2.dpad_down) {
            targetArmPosition = 159;
        } else if (gamepad2.dpad_left) {
            targetArmPosition = 311;
        } else if (gamepad2.dpad_right) {
            targetArmPosition = 520;
        } else if (gamepad2.dpad_up) {
            targetArmPosition = 703;
        } else if (gamepad2.left_stick_y == 0 && targetArmPosition == 0) {
            targetArmPosition = motors.arm.getCurrentPosition();
        }

        if (gamepad2.left_stick_y != 0) {
            targetArmPosition = 0;
            telemetry.addData("setting power", "(%.2f)", gamepad2.left_stick_y);
            motors.arm.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
            motors.arm.setPower(gamepad2.left_stick_y * Consts.ARM_GAIN);
        }
        else {
            motors.hold(motors.arm, targetArmPosition);
        }

        motors.claw.setPosition(R1.buttonState ? Consts.CLAW_MAX : Consts.CLAW_MIN);

        clawCoefficient = x.buttonState ? 1 : -1;
        motors.spinny.setPower(b.buttonState ? clawCoefficient * (R1.buttonState ? 0 : 1) : 0); // speed is controlled by the claw

        telemetry.addData("spinny power", "%.2f", motors.spinny.getPower());
        telemetry.addData("Arm Encoder Pos, Arm Target Pos", "%d, %d", motors.arm.getCurrentPosition(), motors.arm.getTargetPosition());

        if (gamepad2.back) {
            telemetry.addData("BACK!", "back");
            motors.arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        }
    }


    @Override
    public void stop() {
        camera.close();
    }
}