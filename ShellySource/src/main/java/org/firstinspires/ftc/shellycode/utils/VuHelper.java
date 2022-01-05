package org.firstinspires.ftc.shellycode.utils;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.Parameters;
import org.firstinspires.ftc.shellycode.Consts;

public class VuHelper {
    public VuforiaLocalizer vulo;

    public VuHelper(HardwareMap hm) {
        Parameters parameters = new Parameters();

        parameters.vuforiaLicenseKey = Consts.VUFORIA_LIC;
        parameters.cameraName = hm.get(WebcamName.class, "cam");

        vulo = ClassFactory.getInstance().createVuforia(parameters); // init
    }
}
