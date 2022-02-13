package org.firstinspires.ftc.shellycode;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

import java.io.File;

public class Consts {
    // java keyword rundown: public means anything can access it, static means one copy of it is stored across all classes, final means it cannot be changed, and type is the type
    public static final double DEF_SPED = 0.75;
    public static final double MIN_SPED = 0.2 / DEF_SPED;
    public static final double CLAW_MIN = 0.33;
    public static final double CLAW_MAX = 0.56;

    public static final int ARM_VEL = 300; // tps
    public static final int TICKS_PER_POWER = 3000; // tps
    public static final double DRIVE_POS_DEF_TPS = 1000; // seems reasonable
    public static final int TICKS_PER_INCH = 103;

    public static final double ARM_GAIN = 0.6;
    public static final double SMOL_SPED = 0.03;

    public static final double PI = 3.14159265358972;
    public static final double R = 4.375;
    public static final double MAGIK_NUM = 1.295;

    public static final int[] ARM_LEVELS = { 150, 310, 500, 690, 900 };

    public static final double AUTO_DEF_SPED = 0.4f;
    public static final double VU_DEF_SPED = 0.04f;
    public static final double DIST_FRM_TARGET = 1;
    public static final double AUTO_MS_TOLERANCE = 200;
    public static final double AUTO_CAM_BARCODE_OFFSETS = 80; // in pixels I assume
    public static final int LEVEL_RIGHT_OFFSET = 30;

    public static final File CAPTURE_DIR = AppUtil.ROBOT_DATA_DIR;

    public static final String VUFORIA_LIC = "AaA6S6z/////AAABmV078v8AB0xvlFA/9/1fGo8zx6NXlIu8aWH27hfz7bV0U1p5+AQ6El5oes2ysLtsVrpoE2GNAhCP1EhiXg0CQ68NhZv5Cp7xqfYmrvS/udZqLk5rb/9OLWexy37awrSeCT24N+bCA5VZtH91f2F3o4cYe+OvEsRYS4pEb1mdMAqnPTQttuvQw64MTQt4MQbuheCiWHfgyw+nBFbrgY1/0gtHj6Fn8naqHz3NjbOfDCntG1WJhwt8FJK3LS1AQnswzBDt2krVwe2tBvZ68EcL76mAc7QXkYM2YB4cvqUwPTzc6ZE2vgRvzkjeZ7YWHpftWrj+LOQFhaXgv5MgQdwuxQajpDBDoQGahFc/SqPTrDqF";
    public static final String TFL_OD_MODEL = "shelly_27.tflite";
//    public static final String TFL_OD_LABELS = "shelly_27_labels.txt";
    public static final String[] TFL_OD_LABELS = new String[] { "TSE" };

    public static final double DSTBACK_OFFSET = 3.8;

    public static final float MM_PER_INCH = 25.4f;
    // for positioning robot on field
    public static final float MM_TARGET_HEIGHT = 6 * Consts.MM_PER_INCH;
    public static final float HALF_FIELD = 72 * Consts.MM_PER_INCH;
    public static final float HALF_TILE = 12 * Consts.MM_PER_INCH;
    public static final float ONE_AND_HALF_TILE = 36 * Consts.MM_PER_INCH;
    // camera displacement for calculations
    public static final float CAMERA_FORWARD_DISPLACEMENT = 3.0f * MM_PER_INCH; // forward distance from the center of the robot to the camera lens
    public static final float CAMERA_VERTICAL_DISPLACEMENT = 6.0f * MM_PER_INCH; // vertical distance of camera lens off the ground
    public static final float CAMERA_LEFT_DISPLACEMENT = -5.0f * MM_PER_INCH; // left distance from the center of the robot to the camera lens ([-] if right)

    public static final String[] TELEM_LOG_LEVELS = { "DEBUG", "INFO", "STATUS", "WARNING", "ERROR" };
}