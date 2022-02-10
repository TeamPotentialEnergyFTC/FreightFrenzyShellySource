package org.firstinspires.ftc.shellycode.archive;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.shellycode.Consts;
import org.firstinspires.ftc.shellycode.utils.Motors;
import org.firstinspires.ftc.shellycode.utils.Utils;

@Autonomous(name = "Red Right Motion", group = "Autonomous")
@Disabled
public class AutonoOpRedRight extends OpMode {
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

        if (Utils.inRange(ms, 3000, 4000)) {
            motors.stopAll();
        }
    }
}
