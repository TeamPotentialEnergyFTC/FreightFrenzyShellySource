package org.firstinspires.ftc.shellycode.utils;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XYZ;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XZY;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.EXTRINSIC;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.Parameters;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.shellycode.Consts;

import java.util.ArrayList;
import java.util.List;

public class VuHelper {
    public VuforiaLocalizer vulo;
    public Parameters params;

    public VuforiaTrackables targets = null;

    public VuHelper(HardwareMap hm) {
        Parameters params = new Parameters();

        params.vuforiaLicenseKey = Consts.VUFORIA_LIC;
        params.useExtendedTracking = false; // turns off tracking beyond the target
        params.cameraName = hm.get(WebcamName.class, "cam");

        vulo = ClassFactory.getInstance().createVuforia(params); // init
    }

    public void loadWallTargets() {
        targets = vulo.loadTrackablesFromAsset("FreightFrenzyDB"); // load the data sets for the trackable objects from db

        // targets [⌖]
        // trucks
        identifyTarget(0,"Red Storage", -Consts.HALF_FIELD, -Consts.ONE_AND_HALF_TILE, Consts.MM_TARGET_HEIGHT, 90, 0, 90);
        // truck
        identifyTarget(1,"Red Alliance Wall", Consts.HALF_TILE, -Consts.HALF_FIELD, Consts.MM_TARGET_HEIGHT, 90, 0, 180);
        // boat
        identifyTarget(2,"Blue Storage", -Consts.HALF_FIELD, Consts.ONE_AND_HALF_TILE, Consts.MM_TARGET_HEIGHT, 90, 0, 90);
        // airplane
        identifyTarget(3,"Blue Alliance Wall", Consts.HALF_TILE, -Consts.HALF_FIELD, Consts.MM_TARGET_HEIGHT, 90, 0, 0);

        OpenGLMatrix cameraLocationOnRobot = OpenGLMatrix
                .translation(Consts.CAMERA_FORWARD_DISPLACEMENT, Consts.CAMERA_LEFT_DISPLACEMENT, Consts.CAMERA_VERTICAL_DISPLACEMENT)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, XZY, DEGREES, 90, 90, 0));

        // lets the trackables know where the webcam is
        for (VuforiaTrackable trackable : targets) {
            ((VuforiaTrackableDefaultListener) trackable.getListener()).setCameraLocationOnRobot(params.cameraName, cameraLocationOnRobot);
        }
    }

    public void identifyTarget(int aTargetIdx, String targetName, float dx, float dy, float dz, float rx, float ry, float rz) {
        VuforiaTrackable aTarget = targets.get(aTargetIdx);
        aTarget.setName(targetName);
        aTarget.setLocation(OpenGLMatrix.translation(dx, dy, dz).multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, rx, ry, rz)));
    }
}
