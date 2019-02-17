package org.firstinspires.ftc.teamcode.SeasonMaterials.Emergency_Failure_Autos;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.teamcode.SeasonMaterials.MecanumLinearOpMode;

@Autonomous(name="Delatch", group = "em")
//@Disabled
public class DelatchAuto extends MecanumLinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        init(hardwareMap, true);

        // Set up detector
        initVuforia();
        if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
            initTfod();
        } else {
            telemetry.addData("Sorry!", "This device is not compatible with TFOD");
        }

        telemetry.addData("Mode", "setting up detector...");
        telemetry.update();

        telemetry.addData("detector", "enabled");
        telemetry.update();

        double dist = 0;
        waitForStart();

        unlatch();

        driveDistance(1, 5);
    }
}