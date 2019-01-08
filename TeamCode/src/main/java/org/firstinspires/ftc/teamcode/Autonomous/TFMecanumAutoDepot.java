package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.teamcode.MecanumLinearOpMode;

@Autonomous(name="TFMecanumAutoDepot", group = "auto")
//@Disabled
public class TFMecanumAutoDepot extends MecanumLinearOpMode {

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

        findGold(1.25); //GET GOLD POSITION
        tfod.deactivate();
        int gold = retPos();
        sleep(1000);
        telemetry.addData("Gold is at", gold);
        telemetry.update();
        driveDistance(-0.3,4); //MOVE FORWARD OUT OF LANDER ZONE

        dist = pushGold(gold,false);

        driveDistance(-0.5, dist-10); //MOVE TOWARD WALL
        sleep(500);
        rotate(1, 20, false, 2);
        driveTime(-0.3, 2);
        sleep(500);
        driveTime(0.3, .25);
        strafeDistance(0.8, 40,true);   //STRAFE TOWARD DEPOT
        marker.setPosition(0.41);   //DEPLOY MARKER
        sleep(500);
        strafeDistance(0.8, 55,false); //STRAFE INTO CRATER
        driveTime(-0.3, 1);
        strafeDistance(0.8, 20,false); //STRAFE INTO CRATER
        marker.setPosition(0.3);    //RETRACT MARKER DEPLOYMENT*/
        telemetry.addData("Status ", " auto done");
    }
}

