package org.firstinspires.ftc.teamcode.Emergency_Failure_Autos;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.teamcode.MecanumLinearOpMode;

@Autonomous(name="DepotPark", group = "auto")
//@Disabled
public class DepotPark extends MecanumLinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        double offset = 0;
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
//      offset =
        unlatch();
        driveDistance(1, 10 );
        rotate(90, 1);
        //driveDistance();  MORE TO DO!!!
    }}