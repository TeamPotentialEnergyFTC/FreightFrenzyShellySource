package org.firstinspires.ftc.shellycode.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.shellycode.Consts;
import org.firstinspires.ftc.shellycode.utils.Motors;
import org.firstinspires.ftc.shellycode.utils.Utils;

@Autonomous(name="Red Left Auto", group="Autonomous")
public class AutonoOpRL extends OpMode {
    private Motors motors = new Motors(hardwareMap);
    private ElapsedTime runtime;

    @Override
    public void init() { }

    @Override
    public void start() {
        motors.lbd.setPower(Consts.AUTO_DEF_SPED);
        motors.rfd.setPower(Consts.AUTO_DEF_SPED);
    }

    @Override
    public void loop() {
        double ms = runtime.milliseconds();

        if (Utils.inTolerantRange(ms, 3000, Consts.AUTO_MS_TOLERANCE)) {
            motors.lbd.setPower(0);
            motors.rfd.setPower(0);
            motors.quackapult.setPower(-Consts.AUTO_DEF_SPED);
        }
        else if (Utils.inTolerantRange(ms, 10000, Consts.AUTO_MS_TOLERANCE)) {
            motors.quackapult.setPower(0);
            motors.rbd.setPower(-Consts.AUTO_DEF_SPED);
            motors.lfd.setPower(-Consts.AUTO_DEF_SPED);
        }
        else if (Utils.inTolerantRange(ms, 14000, Consts.AUTO_MS_TOLERANCE)) {
            motors.lbd.setPower(Consts.AUTO_DEF_SPED);
            motors.rfd.setPower(Consts.AUTO_DEF_SPED);
        }
        else if (Utils.inTolerantRange(ms, 17500, Consts.AUTO_MS_TOLERANCE)){
            motors.lbd.setPower(0);
            motors.rfd.setPower(0);
        }
    }
}
