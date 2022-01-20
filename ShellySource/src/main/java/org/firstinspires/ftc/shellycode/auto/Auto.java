package org.firstinspires.ftc.shellycode.auto;

import java.lang.Runnable;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.shellycode.utils.Utils;

public abstract class Auto implements Runnable {
    private ElapsedTime runtime;

    private final int startMS;
    private final int endMS;
    private final double tolerance;

    public Auto(int startMS, int endMS, double tolerance) {
        this.startMS = startMS;
        this.endMS = endMS;
        this.tolerance = tolerance;
    }

    public abstract void run();
    public abstract void cleanup();

    public void update() {
        double ms = runtime.milliseconds();

        if (Utils.inTolerantRange(ms, startMS, tolerance)) {
            run();
        }
        else if (Utils.inTolerantRange(ms, endMS, tolerance)) {
            cleanup();
        }
    }



}
