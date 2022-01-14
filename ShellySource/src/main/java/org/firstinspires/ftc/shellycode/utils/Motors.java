package org.firstinspires.ftc.shellycode.utils;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.shellycode.Consts;

public class Motors {
    public DcMotor lfd;
    public DcMotor rfd;
    public DcMotor lbd;
    public DcMotor rbd;
    public DcMotor quackapult;
    public DcMotorEx arm;
    public Servo claw;
    public CRServo spinny;

    public Motors(HardwareMap hm) {
        // where l: left, f: front, d: drive, r: right, b: back (less writing on annoying Driver Hub keyboard)
        lfd = hm.get(DcMotor.class, "lfd"); // left front
        rfd = hm.get(DcMotor.class, "rfd"); // right front
        lbd = hm.get(DcMotor.class, "lbd"); // left back
        rbd = hm.get(DcMotor.class, "rbd"); // right back

        arm = hm.get(DcMotorEx.class, "arm"); // arm
        quackapult = hm.get(DcMotor.class, "quackapult"); // quackapult
        claw = hm.get(Servo.class, "claw"); // arm's claw
        spinny = hm.get(CRServo.class, "spinny"); // intake

        lfd.setDirection(DcMotor.Direction.REVERSE);
        rbd.setDirection(DcMotor.Direction.FORWARD);
        rfd.setDirection(DcMotor.Direction.REVERSE);
        lbd.setDirection(CRServo.Direction.FORWARD);

        quackapult.setDirection(DcMotor.Direction.FORWARD);
        arm.setDirection(DcMotor.Direction.FORWARD);
        arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        claw.setDirection(Servo.Direction.FORWARD);
        spinny.setDirection(CRServo.Direction.FORWARD);
    }

    public void stopAll() {
        lfd.setPower(0);
        rfd.setPower(0);
        lbd.setPower(0);
        rbd.setPower(0);
        arm.setPower(0);
        quackapult.setPower(0);
        claw.setPosition(0);
        spinny.setPower(0);
    }

    public void hold(DcMotorEx motor, int pos) {
        motor.setTargetPosition(pos);
        motor.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        motor.setVelocity(Consts.ARM_VEL);
    }

    public void spin(double power) {
        lbd.setPower(-power);
        rfd.setPower(power);
        lfd.setPower(power);
        rbd.setPower(-power);
    }

    public void drive(double ydir, double xdir, double turn) {
        lbd.setPower(ydir - turn);
        rfd.setPower(ydir + turn);
        lfd.setPower(xdir + turn);
        rbd.setPower(xdir - turn);
    }

}
