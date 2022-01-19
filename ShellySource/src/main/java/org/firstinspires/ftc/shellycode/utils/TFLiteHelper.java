package org.firstinspires.ftc.shellycode.utils;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

public class TFLiteHelper {
    public TFObjectDetector tfod;

    public TFLiteHelper(String modelAsset, String[] labels, float minConfidence, int inpSize, float magnification, int tfodMonitorViewId, VuforiaLocalizer vulo) {
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);

        tfodParameters.isModelTensorFlow2 = true;
        tfodParameters.minResultConfidence = minConfidence;
        tfodParameters.inputSize = inpSize;

        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vulo); // tfod uses camera frames from vulo
        tfod.loadModelFromAsset(modelAsset, labels);

        if (tfod != null) {
            tfod.activate();
            // tfod will scale input down so objects farther away will be much harder to detect so focusing in remedies this
            // should be set to the value of the images used to create the tfod model
            tfod.setZoom(magnification, 16.0 / 9.0);
        }
    }
}
