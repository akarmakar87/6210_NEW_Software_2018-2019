package org.firstinspires.ftc.teamcode.Emergency_Failure_Autos;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.teamcode.MecanumLinearOpMode;

@Autonomous(name="DepotGold", group = "em")
//@Disabled
public class DepotGold extends MecanumLinearOpMode {

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
//
        unlatch();

        //START DETECTION

        findGold(2); //GET GOLD POSITION
        tfod.deactivate();
        int gold = retPos();
        sleep(1000);
        telemetry.addData("Gold is at", gold);
        telemetry.update();
        driveDistance(-0.3,4); //MOVE FORWARD OUT OF LANDER ZONE

        pushGoldEm(gold);

    }
}