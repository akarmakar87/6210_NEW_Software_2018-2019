package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;


import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.teamcode.MecanumLinearOpMode;
import org.firstinspires.ftc.teamcode.OldMecanumLinearOpMode;

@Autonomous(name="NewTFMecanumAutoCrater", group = "auto")
//@Disabled
public class NewTFMecanumAutoCrater extends MecanumLinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        double offset = 45;

        init(hardwareMap, true);

        // SET UP DETECTOR

        initVuforia();
        if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
            initTfod();
        }

        telemetry.addData("Mode", "setting up detector...");
        telemetry.update();

        telemetry.addData("detector", "enabled");
        telemetry.update();

        double dist = 0;
        waitForStart();
        unlatch();
        rotateS(45,2);
        findGold(1.5); //GET GOLD POSITION
        int gold = retPos();

        //START DETECTION

        driveDistance(0.7,2.5); //MOVE FORWARD OUT OF LANDER ZONE
        sleep(500);
        telemetry.addData("Gold is at", gold);
        telemetry.update();

        dist = pushGold(gold,true, offset); //

        driveDistance(-0.5, dist-5); //MOVE TOWARD WALL
        sleep(250);
        rotate(0, 2); //TURN TOWARD WALL
        strafeDistance(1,10,false);
        sleep(250);
        driveDistance(-0.75, 15); //MOVE TOWARD WALL
        //driveTime(0.3, 0.15);    //MOVE BACK FROM WALL (COULD GET RID OF THIS BECAUSE THE TURN WILL PUSH US OFF THE WALL
        //strafeDistance(1, 30, true);
        rotate(-90, 2); //TURN TOWARD WALL
        driveDistance(-0.2, 2);
        marker.setPosition(0.41);   //DEPLOY MARKER
        strafeDistance(1,6,false);
        sleep(1000);
        /*driveTime(-0.3, 0.5);    //MOVE TOWARD WALL
        driveTime(.1,0.25);     // BACK UP FROM WALL
        strafeDistance(1, 52,false); //STRAFE INTO CRATER (VALUE FOR DISTANCE IS 72)
        driveTime(-0.3, 1);    //MOVE TOWARD WALL
        strafeDistance(1, 20,false);
        marker.setPosition(0.2);    //RETRACT MARKER DEPLOYMENT*/
        rotate(0, 2); //TURN TOWARD WALL
        strafeDistance(1, 8,false);
        marker.setPosition(0.2);    //RETRACT MARKER DEPLOYMENT
        driveDistance(0.75, 25);
        sleep(250);
        strafeDistance(1, 3, false);
        driveDistance(0.4, 5);
        telemetry.addData("Status ", " auto done");
    }
}