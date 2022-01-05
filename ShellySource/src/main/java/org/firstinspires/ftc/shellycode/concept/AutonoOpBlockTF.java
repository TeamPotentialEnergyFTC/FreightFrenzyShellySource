package org.firstinspires.ftc.shellycode.concept;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.shellycode.Consts;
import org.firstinspires.ftc.shellycode.utils.TFLiteHelper;
import org.firstinspires.ftc.shellycode.utils.Utils;
import org.firstinspires.ftc.shellycode.utils.VuHelper;

import java.nio.file.Paths;
import java.util.List;

@Autonomous(name = "Block Dropoff", group = "Autonomous")
public class AutonoOpBlockTF extends OpMode {
    private TFLiteHelper tfLiteHelper;
    private VuHelper vuHelper;

    private static final String[] LABELS = {
        "tse"
    };

    public void init() {
        vuHelper = new VuHelper(hardwareMap);

        int tfId = hardwareMap.appContext.getResources().getIdentifier("tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        tfLiteHelper = new TFLiteHelper("shelly_27-1.tflite", LABELS, 0.6f, 512, 1, tfId, vuHelper.vulo);
    }

    @Override
    public void loop() {
        if (tfLiteHelper.tfod == null) { return; }

        List<Recognition> updatedRecognitions = tfLiteHelper.tfod.getUpdatedRecognitions();
        if (updatedRecognitions != null) {
            telemetry.addData("Objects detected", updatedRecognitions.size());

            for (Recognition recognition : updatedRecognitions) {
                telemetry.addData("Label", recognition.getLabel());
                telemetry.addData("Top Left","(%.2f, %.2f)", recognition.getLeft(), recognition.getTop());
                telemetry.addData("Bottom Right", "(%.2f, %.2f)", recognition.getRight(), recognition.getBottom());
                telemetry.addData("-------", "-------");
            }
        }
    }

}
