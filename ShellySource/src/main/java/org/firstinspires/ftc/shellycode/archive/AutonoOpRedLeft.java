package org.firstinspires.ftc.shellycode.archive;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.shellycode.Consts;
import org.firstinspires.ftc.shellycode.utils.Motors;
import org.firstinspires.ftc.shellycode.utils.Utils;

@Autonomous(name = "Red Left Motion", group = "Autonomous")
@Disabled
public class AutonoOpRedLeft extends OpMode {
    private Motors motors;
    private final ElapsedTime runtime = new ElapsedTime();

    @Override
    public void init() {
        motors = new Motors(hardwareMap);
    }

    @Override
    public void start() {
        runtime.reset();
        motors.stopAll();

        motors.lbd.setPower(Consts.AUTO_DEF_SPED);
        motors.rfd.setPower(Consts.AUTO_DEF_SPED);
    }

    @Override
    public void loop() {
        double ms = runtime.milliseconds();

        if (Utils.inRange(ms, 3000, 10000)) {
            motors.stopAll();
            motors.quackapult.setPower(-Consts.AUTO_DEF_SPED);
        } else if (Utils.inRange(ms, 10000, 13000)) {
            motors.stopAll();
            motors.rbd.setPower(-Consts.AUTO_DEF_SPED);
            motors.lfd.setPower(-Consts.AUTO_DEF_SPED);
        } else if (Utils.inRange(ms, 13000, 15500)) {
            motors.stopAll();
            motors.lbd.setPower(Consts.AUTO_DEF_SPED);
            motors.rfd.setPower(Consts.AUTO_DEF_SPED);
        } else if (Utils.inRange(ms, 15500, 16500)) {
            motors.stopAll();
        }
    }
}
