package org.firstinspires.ftc.shellycode.utils;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.shellycode.Consts;

public class Da_Aaarm {

    private DcMotorEx arm;

    public Da_Aaarm(DcMotorEx aArm){
        arm = aArm;
    }

    public void Stay(int pos){
        arm.setTargetPosition(pos);
        arm.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        arm.setVelocity(Consts.ARM_VEL);
    }


}
