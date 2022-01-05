package org.firstinspires.ftc.shellycode.utils;

public class ButtonState {
    private boolean buttonState;
    private boolean downLastUpdate = false;

    public ButtonState(boolean curState) {
        buttonState = curState;
    }

    public void setState(boolean curState) {
        if (curState && !downLastUpdate) {
            buttonState = !buttonState;
            downLastUpdate = true;
        }
        else if (!curState && downLastUpdate) {
            downLastUpdate = false;
        }
    }

    public boolean isPressed() {
        return buttonState;
    }
}
