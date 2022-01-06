package org.firstinspires.ftc.shellycode;

import android.graphics.Bitmap;
import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.shellycode.utils.ButtonState;
import org.firstinspires.ftc.shellycode.utils.Camera;
import org.firstinspires.ftc.shellycode.utils.Motors;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@TeleOp(name="ShellyOp", group="TellyOp")
public class ShellyOp extends OpMode {
    private Motors motors;
    Camera camera;

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
        camera = new Camera(hardwareMap);

        b = new ButtonState(false);
        x = new ButtonState(false);

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

        b.setState(gamepad2.b);
        x.setState(gamepad2.x);

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

        telemetry.addData("Turbo/Snail Telem", "turbo (%.2f), snail (%.2f)", turboGain, snailGain);
        telemetry.addData("Drive Telem", "xdir (%.2f), ydir (%.2f), turn (%.2f), gains (%.2f)", xdir, ydir, turn, snailGain * turboGain);

        // ---
        // quackapult + arm + claw
        // ---
        motors.quackapult.setPower(gamepad2.right_stick_x);

        if (gamepad2.left_stick_y == 0) {
            motors.arm.setTargetPosition(motors.arm.getCurrentPosition());
            motors.arm.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
            motors.arm.setVelocity(Consts.ARM_VEL);
        }
        else {
            telemetry.addData("setting power", "(%.2f)", gamepad2.left_stick_y);
            motors.arm.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
            motors.arm.setPower(gamepad2.left_stick_y * Consts.ARM_GAIN);
        }

        motors.claw.setPosition(Range.clip(gamepad2.right_trigger, Consts.CLAW_MIN, Consts.CLAW_MAX)); // min and max to not grind servo

        clawCoefficient = x.isPressed() ? 1 : -1;
        motors.spinny.setPower(b.isPressed() ? 0 : clawCoefficient * Range.clip(Consts.CLAW_MAX - gamepad2.right_trigger, 0, 1)); // speed is controlled by the claw

        telemetry.addData("spinny power",  "%.2f", motors.spinny.getPower());
        telemetry.addData("Arm Encoder Pos, Arm Target Pos",  "%d, %d", motors.arm.getCurrentPosition(), motors.arm.getTargetPosition());
    }

    @Override
    public void stop() {
        camera.close();
    }
}
