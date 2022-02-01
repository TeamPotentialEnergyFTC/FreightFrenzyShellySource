package org.firstinspires.ftc.shellycode.auto;

import org.firstinspires.ftc.shellycode.utils.Utils;

public abstract class Motion implements Runnable {

    public abstract boolean isEnd();
    public abstract void init();
    public abstract void run();
    public abstract void cleanup();
}
