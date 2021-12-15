package org.firstinspires.ftc.shellycode.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.shellycode.Consts;
import org.firstinspires.ftc.shellycode.utils.Motors;
import org.firstinspires.ftc.shellycode.utils.Utils;

@Autonomous(name = "Red Right Auto", group = "Autonomous")
public class AutonoOpRedRight extends OpMode {
    private Motors motors;
    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void init() {
        motors = new Motors(hardwareMap);
    }

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
        }
    }
}
