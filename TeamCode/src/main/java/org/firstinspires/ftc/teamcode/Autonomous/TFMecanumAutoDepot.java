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

        double startAng = init(hardwareMap, true);

        double angleOff = 0;

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

        lift.setPower(0.90);    //LIFT PULLS ROBOT UP (releases tension for easy unlock)
        lock.setPosition(1);    //UNLOCK LIFT
        lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT); //LET GRAVITY TAKE THE ROBOT DOWN
        sleep(1500);
        lock.setPosition(0);    //Stop lock movement
        sleep(500);
        int liftTarget = lift.getCurrentPosition()-640; //FIND HOW FAR THE LIFT NEEDS TO RETRACT
        while (!isStopRequested() && lift.getCurrentPosition() > liftTarget){   //RETRACT LIFT
            lift.setPower(-1);
        }
        lift.setPower(0);
        driveDistance(0.3,0.5); //What is this for???

        strafeDistance(-0.3, 5, true); //MOVE A BIT TO TRIGGER CAMERA VIEWING

        //START DETECTION

        findGold(2); //GET GOLD POSITION
        int gold = retPos();
        sleep(1000);
        telemetry.addData("Gold is at", gold);
        telemetry.update();
        driveDistance(-0.3,4); //MOVE FORWARD OUT OF LANDER ZONE

        dist = pushGold(gold);

        driveDistance(0.5, dist); //MOVE TOWARD WALL
        rotate(0.2, 45, false, 5);  //TURN TOWARD WALL
       // driveDistance(0.4, 10);    //ALIGN WITH WALL (by running into it)
        driveDistance(-0.4, 0.5);    //BACK UP FROM WALL (to not get stuck on it)
        strafeDistance(0.7, 35,true);  //STRAFE INTO DEPOT (IMPORTANT LINE NEEDS REVIEWING TO MAKE SURE IT DOESN'T GO TOO FAR INTO DEPOT)
        driveDistance(0.4,5);  //REALIGN WITH WALL (to not hit the mineral)
        marker.setPosition(0.41);  //DEPLOY MARKER
        sleep(1000);
        //marker.setPosition(0);
        strafeDistance(0.7, 36.5,false);    //STRAFE TOWARD CRATER
        driveDistance(0.4,2.5);    //REALIGN WITH WALL (so we don't hit our alliance's mineral)
        strafeDistance(0.8, 35,false);    //STRAFE INTO CRATER
        marker.setPosition(0.2);    //RETRACT MARKER DEPLOYMENT
        telemetry.addData("Status ", " auto done");
    }
}

