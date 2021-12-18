package org.firstinspires.ftc.shellycode.utils;

public class Utils {
    public static boolean inTolerantRange(double value0, double value1, double tolerance) {
        return (value0 >= value1 - tolerance && value0 <= value1 + tolerance);
    }
}