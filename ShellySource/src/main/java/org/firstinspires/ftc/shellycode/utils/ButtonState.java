package org.firstinspires.ftc.shellycode.utils;

public class ButtonState {
    public boolean buttonState = false;
    private boolean downLastUpdate = false;

    public void update(boolean curState) {
        if (curState && !downLastUpdate) {
            buttonState = !buttonState;
            downLastUpdate = true;
        }
        else if (!curState && downLastUpdate) {
            downLastUpdate = false;
        }
    }

}
