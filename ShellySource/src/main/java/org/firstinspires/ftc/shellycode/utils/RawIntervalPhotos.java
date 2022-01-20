package org.firstinspires.ftc.shellycode.utils;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class RawIntervalPhotos implements Runnable {
    private final RawCamera cam;
    private final Telemetry telem;
    private long startTime;

    private final int captureDelayMS;

    private boolean doStop = false;

    public RawIntervalPhotos(HardwareMap hm, Telemetry telem, int captureDelayMS) {
        cam = new RawCamera(hm);
        this.captureDelayMS = captureDelayMS;

        startTime = System.currentTimeMillis();

        this.telem = telem;
    }

    public synchronized void doStop() {
        cam.closeCamera();
        this.doStop = true;
    }

    private synchronized boolean keepRunning() {
        return this.doStop == false;
    }

    @Override
    public void run() {
        while(keepRunning()) {
            if (System.currentTimeMillis() - startTime >= captureDelayMS) {
                telem.log().add("TAKING PHOTO!!!");
                startTime = System.currentTimeMillis();
                cam.capture();
            }
        }
    }


}