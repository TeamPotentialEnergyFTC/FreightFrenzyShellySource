package org.firstinspires.ftc.shellycode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

public class Shelly {
    private HardwareMap hm;
    private Telemetry telem;

    public final Consts consts = new Consts();

    public DcMotorEx lfd, rfd, lbd, rbd, arm; // l: left, r: right, f: front, b: back | d: drive
    public DcMotor quackapult;
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
        lfd = hm.get(DcMotorEx.class, "lfd"); // left front
        rfd = hm.get(DcMotorEx.class, "rfd"); // right front
        lbd = hm.get(DcMotorEx.class, "lbd"); // left back
        rbd = hm.get(DcMotorEx.class, "rbd"); // right back

        arm = hm.get(DcMotorEx.class, "arm"); // arm
        quackapult = hm.get(DcMotor.class, "quackapult"); // quackapult (duck deliverer)
        claw = hm.get(Servo.class, "claw"); // arm's claw
        spinny = hm.get(CRServo.class, "spinny"); // intake

        lfd.setDirection(DcMotor.Direction.REVERSE);
        rbd.setDirection(DcMotor.Direction.FORWARD);
        rfd.setDirection(DcMotor.Direction.REVERSE);
        lbd.setDirection(CRServo.Direction.FORWARD);
        lfd.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER); // no encoder initially
        rbd.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER); // no encoder initially
        rfd.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER); // no encoder initially
        lbd.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER); // no encoder initially

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

    public VuforiaLocalizer getVulo() {
        VuforiaLocalizer.Parameters params = new VuforiaLocalizer.Parameters();

        params.vuforiaLicenseKey = Consts.VUFORIA_LIC;
        params.useExtendedTracking = false; // turns off tracking beyond the target
        params.cameraName = hm.get(WebcamName.class, "cam");

        return ClassFactory.getInstance().createVuforia(params); // init
    }

    public void holdArm(int pos) { // pos in ticks
        arm.setTargetPosition(pos);
        arm.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        arm.setVelocity(Consts.ARM_VEL);
    }

    public void drivePower(double pwrX, double pwrY, double turnPwr) {
        if (lbd.getMode() != DcMotor.RunMode.RUN_WITHOUT_ENCODER) {
            lbd.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            rfd.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            lfd.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            rbd.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }

        lbd.setPower(pwrY - turnPwr);
        rfd.setPower(pwrY + turnPwr);
        lfd.setPower(pwrX + turnPwr);
        rbd.setPower(pwrX - turnPwr);
    }

    public void driveTicks(int tpsX, int tpsY, int turnTps) {
        if (lbd.getMode() != DcMotor.RunMode.RUN_USING_ENCODER) { // one should mean all
            lbd.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rfd.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            lfd.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rbd.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }

        lbd.setVelocity(tpsX - turnTps);
        rfd.setVelocity(tpsX + turnTps);
        lfd.setVelocity(tpsY + turnTps);
        rbd.setVelocity(tpsY - turnTps);
    }

    private void trySwitchRunPosition() {
        // copy-🍝 pain
        if (lbd.getMode() != DcMotor.RunMode.RUN_TO_POSITION) { // one should mean all
            lbd.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rfd.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            lfd.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rbd.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }

        lbd.setVelocity(Consts.DRIVE_POS_DEF_TPS);
        rfd.setVelocity(Consts.DRIVE_POS_DEF_TPS);
        lfd.setVelocity(Consts.DRIVE_POS_DEF_TPS);
        rbd.setVelocity(Consts.DRIVE_POS_DEF_TPS);
    }

    public void driveInches(double xInches, double yInches) {
        int xTicks = (int)(xInches * 100);
        int yTicks = (int)(yInches * 100);
        // it's about 1in per 100 ticks which is nice
        lbd.setTargetPosition(xTicks);
        rfd.setTargetPosition(xTicks);
        lfd.setTargetPosition(yTicks);
        rbd.setTargetPosition(yTicks);
        trySwitchRunPosition();
    }

    public void turnDeg(double deg) {
        double rawTurnInches = deg / 360 * 2 * Consts.PI * Consts.R;
        int realTurnTicks = (int)(rawTurnInches / Consts.MAGIK_NUM * Consts.TICKS_PER_INCH);

        lbd.setTargetPosition(-realTurnTicks);
        rfd.setTargetPosition(realTurnTicks);
        lfd.setTargetPosition(realTurnTicks);
        rbd.setTargetPosition(-realTurnTicks);
        trySwitchRunPosition();
    }

    public void findPosition(double coordX, double coordY) {
        // math shenanigans go here ＼（〇_ｏ）／(⊙_⊙)？
        // there is no going back in calling this function

    }


}
