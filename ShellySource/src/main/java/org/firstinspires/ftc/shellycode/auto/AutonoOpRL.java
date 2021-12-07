package org.firstinspires.ftc.shellycode.auto;

import java.util.TimerTask;
import java.util.Timer;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.shellycode.utils.Consts;
import org.firstinspires.ftc.shellycode.Motors;

@Autonomous(name="Red Left Auto", group="Autonomous")
public class AutonoOpRL extends OpMode {
    private Motors motors = new Motors(hardwareMap);
    private Timer timer = new Timer("Auto Task Timer");

    @Override
    public void init() {

        telemetry.addData("Status", "Initialized");
    }

    @Override
    public void start() {
        motors.lbd.setPower(Consts.AUTO_DEF_SPED);
        motors.rfd.setPower(Consts.AUTO_DEF_SPED);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                motors.lbd.setPower(0);
                motors.rfd.setPower(0);
                motors.quackapult.setPower(-Consts.AUTO_DEF_SPED);
            }
        }, 3000);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                motors.quackapult.setPower(0);
                motors.rbd.setPower(-Consts.AUTO_DEF_SPED);
                motors.lfd.setPower(-Consts.AUTO_DEF_SPED);
            }
        }, 10000);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                motors.rbd.setPower(0);
                motors.lfd.setPower(0);
                motors.lbd.setPower(Consts.AUTO_DEF_SPED);
                motors.rfd.setPower(Consts.AUTO_DEF_SPED);
            }
        }, 14000);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                motors.lbd.setPower(0);
                motors.rfd.setPower(0);
            }
        }, 14000);
    }

    @Override
    public void loop() { }
}
