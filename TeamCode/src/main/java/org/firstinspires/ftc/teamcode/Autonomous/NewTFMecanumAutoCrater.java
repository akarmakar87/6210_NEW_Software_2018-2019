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
        rotateS(45,32);
        findGold(1.5); //GET GOLD POSITION


        //START DETECTION

        driveDistance(0.7,2.5); //MOVE FORWARD OUT OF LANDER ZONE
        int gold = retPos();
        sleep(500);
        telemetry.addData("Gold is at", gold);
        telemetry.update();

        dist = pushGold(gold,true, offset); //

        driveDistance(-0.7, dist - 3); //MOVE TOWARD WALL
        sleep(250);
        rotate(-90, 2); //TURN TOWARD WALL
        driveTime(-0.3, 0.75); //ALIGN WITH WALL
        sleep(250);
        driveTime(0.3, 0.15);    //MOVE BACK FROM WALL (COULD GET RID OF THIS BECAUSE THE TURN WILL PUSH US OFF THE WALL
        strafeDistance(1, 30, true);
        marker.setPosition(0.41);   //DEPLOY MARKER
        sleep(500);
        driveTime(-0.3, 0.5);    //MOVE TOWARD WALL
        driveTime(.1,0.25);     // BACK UP FROM WALL
        strafeDistance(1, 52,false); //STRAFE INTO CRATER (VALUE FOR DISTANCE IS 72)
        driveTime(-0.3, 1);    //MOVE TOWARD WALL
        strafeDistance(1, 20,false);
        marker.setPosition(0.2);    //RETRACT MARKER DEPLOYMENT
        telemetry.addData("Status ", " auto done");
    }
}