package org.firstinspires.ftc.shellycode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Motors {
    public DcMotor lfd;
    public DcMotor rfd;
    public DcMotor lbd;
    public DcMotor rbd;
    public DcMotor quackapult;
    public DcMotorEx arm;
    public Servo claw;
    public CRServo spinny;

    private HardwareMap hm;

    public Motors(HardwareMap hardwareMap) {
        hm = hardwareMap;

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
        arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        claw.setDirection(Servo.Direction.FORWARD);
        spinny.setDirection(CRServo.Direction.FORWARD);
    }

}
