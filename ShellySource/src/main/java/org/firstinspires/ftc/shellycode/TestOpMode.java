package org.firstinspires.ftc.shellycode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.opencv

@TeleOp(name="Test Op Mode", group="TellyOp")
public class TestOpMode extends OpMode {


    @Override
    public void init() {

    }

    @Override public void loop() {
        OpenCVLoader.initDebug();
    }
}
