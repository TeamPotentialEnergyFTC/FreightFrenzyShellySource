package org.firstinspires.ftc.shellycode.auto;

import com.qualcomm.robotcore.util.ElapsedTime;

import java.lang.Runnable;

public abstract class Auto implements Runnable {
    private ElapsedTime runtime;

    private int startMS;
    private int endMS;

    public Auto(int startMS, int endMS) {
        this.startMS = startMS;
        this.endMS = endMS;
    }

    public abstract void run();

    public void update() {
        double ms = runtime.milliseconds();
        if (ms >= startMS && ms <= endMS) {
            run();
        }
    }
}
