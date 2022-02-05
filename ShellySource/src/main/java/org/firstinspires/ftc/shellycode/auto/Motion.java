package org.firstinspires.ftc.shellycode.auto;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.shellycode.utils.Utils;

public abstract class Motion implements Runnable {
    public ElapsedTime runtime = new ElapsedTime();

    public abstract boolean isEnd();
    public abstract void init();
    public abstract void run();
    public abstract void cleanup();
}
