package org.firstinspires.ftc.shellycode.concept;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.shellycode.utils.TFLiteHelper;
import org.firstinspires.ftc.shellycode.utils.VuHelper;

import java.util.List;

@Autonomous(name = "Lots of objects detector", group = "Autonomous")
// @Disabled
public class LotsObjectsTF extends OpMode {
    private TFLiteHelper tfLiteHelper;
    private VuHelper vuHelper;

    private static final String[] LABELS = {
            "person",
            "bicycle",
            "car",
            "motorcycle",
            "airplane",
            "bus",
            "train",
            "truck",
            "boat",
            "traffic light",
            "fire hydrant",
            "???",
            "stop sign",
            "parking meter",
            "bench",
            "bird",
            "cat",
            "dog",
            "horse",
            "sheep",
            "cow",
            "elephant",
            "bear",
            "zebra",
            "giraffe",
            "???",
            "backpack",
            "umbrella",
            "???",
            "???",
            "handbag",
            "tie",
            "suitcase",
            "frisbee",
            "skis",
            "snowboard",
            "sports ball",
            "kite",
            "baseball bat",
            "baseball glove",
            "skateboard",
            "surfboard",
            "tennis racket",
            "bottle",
            "???",
            "wine glass",
            "cup",
            "fork",
            "knife",
            "spoon",
            "bowl",
            "banana",
            "apple",
            "sandwich",
            "orange",
            "broccoli",
            "carrot",
            "hot dog",
            "pizza",
            "donut",
            "cake",
            "chair",
            "couch",
            "potted plant",
            "bed",
            "???",
            "dining table",
            "???",
            "???",
            "toilet",
            "???",
            "tv",
            "laptop",
            "mouse",
            "remote",
            "keyboard",
            "cell phone",
            "microwave",
            "oven",
            "toaster",
            "sink",
            "refrigerator",
            "???",
            "book",
            "clock",
            "vase",
            "scissors",
            "teddy bear",
            "hair drier",
            "toothbrush",
    };

    @Override
    public void init() {
        vuHelper = new VuHelper(hardwareMap);

        int tfId = hardwareMap.appContext.getResources().getIdentifier("tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
//        tfLiteHelper = new TFLiteHelper(Consts.TFL_OD_MODEL, Utils.getFileLines(Paths.get(Consts.TFL_OD_LABELS)), 0.65f, 512, tfId, vuHelper.vulo);
        tfLiteHelper = new TFLiteHelper("ssd_mobilenet_v1_1_metadata_1.tflite", LABELS, 0.65f, 512, 1, tfId, vuHelper.vulo);
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
