package org.firstinspires.ftc.shellycode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Shelly {
    private HardwareMap hm;
    private Telemetry telem;

    public final Consts consts = new Consts();

    public DcMotor lfd, rfd, lbd, rbd, quackapult; // l: left, r: right, f: front, b: back | d: drive
    public DcMotorEx arm;
    public Servo claw;
    public CRServo spinny;

    public TouchSensor limit;
    public DistanceSensor dsl, bds, rds; // l: left, b: back, r: right | d: distance, s: sensor

    public Shelly(HardwareMap hm, Telemetry telem) {
        this.hm = hm;
        this.telem = telem;

        telem.addData(Consts.TELEM_LOG_LEVELS[1], "Shelly Instance Created");
    }

    // the trouble to seperate this is not worth how nice it is to write two lines over this every single time
    public void assignHardware() {
        // where l: left, f: front, d: drive, r: right, b: back (less writing on annoying Driver Hub keyboard)
        lfd = hm.get(DcMotor.class, "lfd"); // left front
        rfd = hm.get(DcMotor.class, "rfd"); // right front
        lbd = hm.get(DcMotor.class, "lbd"); // left back
        rbd = hm.get(DcMotor.class, "rbd"); // right back

        arm = hm.get(DcMotorEx.class, "arm"); // arm
        quackapult = hm.get(DcMotor.class, "quackapult"); // quackapult (duck deliverer)
        claw = hm.get(Servo.class, "claw"); // arm's claw
        spinny = hm.get(CRServo.class, "spinny"); // intake

        lfd.setDirection(DcMotor.Direction.REVERSE);
        rbd.setDirection(DcMotor.Direction.FORWARD);
        rfd.setDirection(DcMotor.Direction.REVERSE);
        lbd.setDirection(CRServo.Direction.FORWARD);

        quackapult.setDirection(DcMotor.Direction.FORWARD);
        arm.setDirection(DcMotor.Direction.FORWARD);
        arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER); // no encoder initially
        claw.setDirection(Servo.Direction.FORWARD);
        spinny.setDirection(CRServo.Direction.FORWARD);

        limit = hm.get(TouchSensor.class, "limit");
        dsl = hm.get(DistanceSensor.class, "lds");
        bds = hm.get(DistanceSensor.class, "bds");
        rds = hm.get(DistanceSensor.class, "rds");

        telem.addData(Consts.TELEM_LOG_LEVELS[1], "Hardware Assigned");
    }

    public void holdArm(int pos) { // pos in ticks
        arm.setTargetPosition(pos);
        arm.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        arm.setVelocity(Consts.ARM_VEL);
    }

    public void drivePower(double dirX, double dirY, double turn) {
        lbd.setPower(dirY - turn);
        rfd.setPower(dirY + turn);
        lfd.setPower(dirX + turn);
        rbd.setPower(dirX - turn);
    }

    public void drivePosition(double coordX, double coordY) {
        // math shenanigans go here ＼（〇_ｏ）／(⊙_⊙)？


    }



}
