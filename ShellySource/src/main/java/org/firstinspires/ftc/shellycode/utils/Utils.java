package org.firstinspires.ftc.shellycode.utils;

import android.graphics.drawable.shapes.Shape;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.qualcomm.robotcore.util.RobotLog;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Utils {
    public static boolean inTolerantRange(double value0, double value1, double tolerance) {
        return (value0 >= value1 - tolerance && value0 <= value1 + tolerance);
    }

    public static boolean inRange(double value, double rangeLow, double rangeHigh) {
        return (value >= rangeLow && value <= rangeHigh);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String[] getFileLines(Path filePath)  {
        List<String> lines = null;
        try {
            lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            RobotLog.e(e.toString());
        }
        String[] arr = lines.toArray(new String[lines.size()]);
        return arr;
    }

    public static boolean collidesWith(Shape shape, Shape other) {
        return false; //shape.getBounds2D().intersects(other.getBounds2D());
    }
}
