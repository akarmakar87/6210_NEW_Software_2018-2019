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


        //START DETECTION

        findGold(2); //GET GOLD POSITION
        int gold = retPos();
        sleep(500);
        telemetry.addData("Gold is at", gold);
        telemetry.update();
        driveDistance(0.7,4); //MOVE FORWARD OUT OF LANDER ZONE
        rotate(45,3);

        dist = pushGold(gold,true, offset); //

        driveDistance(-0.7, dist); //MOVE TOWARD WALL
        sleep(250);
        rotate(-90, 2); //TURN TOWARD WALL
        driveTime(-0.3, 0.35); //ALIGN WITH WALL
        sleep(250);
        driveTime(0.3, 0.25);    //MOVE BACK FROM WALL (COULD GET RID OF THIS BECAUSE THE TURN WILL PUSH US OFF THE WALL
        strafeDistance(1, 30, true);
        //rotate(-180, 2); //TURN TOWARD DEPOT
        //driveDistance(0.7, 25);   //DRIVE INTO DEPOT
        //markerMove();
        //rotate(-90, 2); //TURN TO DEPLOY MARKER
        /*if (getRange() < 50){
            markerMove();
        }else{
            strafeDistance(0.8, 40,true);   //STRAFE TOWARD DEPOT
        }*/
        marker.setPosition(0.41);   //DEPLOY MARKER
        sleep(500);
        driveTime(-0.3, 0.25);    //MOVE TOWARD WALL
        driveTime(.1,0.25);     // BACK UP FROM WALL
        strafeDistance(1, 72,false); //STRAFE INTO CRATER (VALUE FOR DISTANCE IS 72)
        marker.setPosition(0.2);    //RETRACT MARKER DEPLOYMENT
        telemetry.addData("Status ", " auto done");
    }
}