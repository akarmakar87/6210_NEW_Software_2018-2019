package org.firstinspires.ftc.teamcode.SeasonMaterials.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;


import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.teamcode.SeasonMaterials.OldMecanumLinearOpMode;

@Autonomous(name="TFMecanumAutoCrater", group = "auto")
@Disabled

public class TFMecanumAutoCrater extends OldMecanumLinearOpMode {

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

        //START DETECTION

        findGold(2); //GET GOLD POSITION
        tfod.deactivate();
        int gold = retPos();
        sleep(1000);
        telemetry.addData("Gold is at", gold);
        telemetry.update();
        driveDistance(-0.3,4); //MOVE FORWARD OUT OF LANDER ZONE

        dist = pushGold(gold,true);

        driveDistance(0.5, dist); //MOVE TOWARD WALL
        sleep(500);
        rotate(1, 10, false, 2);
        driveTime(0.3, 1);
        sleep(500);
        driveTime(-0.3, .25);
        strafeDistance(0.8, 40,true);   //STRAFE TOWARD DEPOT
        marker.setPosition(0.41);   //DEPLOY MARKER
        sleep(1000);
        strafeDistance(0.8, 72,false); //STRAFE INTO CRATER
        marker.setPosition(0.2);    //RETRACT MARKER DEPLOYMENT*/
        telemetry.addData("Status ", " auto done");
    }
}