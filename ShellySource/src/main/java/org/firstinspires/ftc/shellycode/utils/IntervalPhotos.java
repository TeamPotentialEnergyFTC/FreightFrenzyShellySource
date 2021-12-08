package org.firstinspires.ftc.shellycode.utils;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class IntervalPhotos {
    private final Camera cam;
    private ElapsedTime runtime;

    private final int captureDelay;

    public IntervalPhotos(HardwareMap hm, Telemetry telem, int captureDelay) {
        cam = new Camera(hm, telem);
        this.captureDelay = captureDelay;
    }

    public void update() {
        if (runtime.seconds() >= captureDelay) {
            runtime.reset();
            cam.capture();
        }
    }
}

