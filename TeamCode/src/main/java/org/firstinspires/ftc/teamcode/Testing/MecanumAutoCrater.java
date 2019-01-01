package org.firstinspires.ftc.teamcode.Testing;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.MecanumLinearOpMode;

@Autonomous(name="MecanumAutoCrater", group = "auto")
//@Disabled
public class MecanumAutoCrater extends MecanumLinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        /*double startAng = init(hardwareMap, true);

        init(hardwareMap, true);
        double dist = 45;
        waitForStart();
        lift.setPower(0.90);    //LIFT PULLS ROBOT UP (releases tension for easy unlock)
        lock.setPosition(1);    //UNLOCK LIFT
        lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT); //LET GRAVITY TAKE THE ROBOT DOWN
        sleep(2000);
        int liftTarget = lift.getCurrentPosition()-640; //FIND HOW FAR THE LIFT NEEDS TO RETRACT
        while (!isStopRequested() && lift.getCurrentPosition() > liftTarget){   //RETRACT LIFT
            lift.setPower(-1);
        }
        lift.setPower(0);
        driveDistance(0.3,0.5); //What is this for???
        double ang = getYaw();  //Why do we need this???
        // rotate(0.2,-ang,false, 2 );
        strafeDistance(-0.3, 6.5, true); //MOVE A BIT TO TRIGGER CAMERA VIEWING
        //lock.setPosition(0);  //Don't need to relock
        int gold = findGold(3); //GET GOLD POSITION
        sleep(1000);
        int x = 0;
        double angleOff = 0;
        telemetry.addData("gold is at", gold);
        telemetry.addData("align is ", checkAlign());
        telemetry.update();
        driveDistance(-0.3,9); //MOVE FORWARD OUT OF LANDER ZONE
        resetTime();
        switch (gold){
            case 2:
                while (!checkAlign() && !isStopRequested() && getTime() < 4){ //IF GOLD IN CENTER, ADJUST
                    //if (getXpos() < 400){
                        //LF.setPower(0.4);
                        //RF.setPower(-0.4);
                        //LB.setPower(-0.4);
                      //  RB.setPower(0.4);
                    //}else{
                        LF.setPower(-0.4);
                        RF.setPower(0.4);
                        LB.setPower(0.4);
                        RB.setPower(-0.4);
                    //}
                }
                sleep(1000);
                strafeDistance(-0.5, 3, true); // better aligns to not hit other mineral
                sleep(1000);
                driveDistance(-0.4, 5); //PUSH AND BACK UP
                sleep(1000);
                driveDistance(0.3, 5);
                x = 5;
                break;
            case 1:
                while (!checkAlign() && !isStopRequested() && getTime() < 4){ //IF GOLD ON LEFT, MOVE LEFT TIL ALIGNED
                    LF.setPower(0.4);
                    RF.setPower(-0.4);
                    LB.setPower(-0.4);
                    RB.setPower(0.4);
                }
                strafeDistance(-0.25, 3, true); // better aligns to not hit other mineral
                sleep(1000);
                driveDistance(-0.4, 5); //PUSH AND BACK UP
                sleep(1000);
                driveDistance(0.3, 5);
                x = 10;
                break;
            case 3://
                telemetry.addData("status", "about to move right");
                telemetry.update();
                sleep(1000);
                while (!checkAlign() && !isStopRequested() && getTime() < 4){ //IF GOLD ON RIGHT, MOVE RIGHT TIL ALIGNED
                    telemetry.addData("status", "moving right");
                    telemetry.addData("aligning right", checkAlign());
                    telemetry.update();
                    telemetry.update();
                    LF.setPower(-0.4);
                    RF.setPower(0.4);
                    LB.setPower(0.4);
                    RB.setPower(-0.4);
                }
                driveDistance(-0.4, 5); //PUSH AND BACK UP
                sleep(1000);
                driveDistance(0.3, 5);
                break;
        }
        angleOff = getYaw(); //UPDATE ANGLE
        disableDetector();
        rotate(0.2, 90 - angleOff, false, 5);   //ROTATE TOWARD WALL
        sleep(1000);
        driveDistance(0.5, 35 - x); //MOVE TOWARD WALL
        sleep(1000);
       // rotate(0.2, 45, false, 5);
        driveDistance(0.5, 5);  //ALIGN WITH WALL (by running into it)
        driveDistance(-0.5, 0.5);   //MOVE BACK FROM WALL (so we don't get caught on it)
        strafeDistance(0.8, 17,true);   //STRAFE TOWARD DEPOT
        driveDistance(0.5,5);   //REALIGN WITH WALL (to avoid hitting a mineral)
        strafeDistance(0.8, 17,true);   //CONTINUE INTO DEPOT
        marker.setPosition(0.41);   //DEPLOY MARKER
        sleep(1000);
        //marker.setPosition(0);
        strafeDistance(0.8, 36.5,false);    //STRAFE TOWARD CRATER
        driveDistance(-0.5,2.5);    //REALIGN WITH WALL (so we don't hit our alliance partner's mineral)
        strafeDistance(0.8, dist+10,false); //STRAFE INTO CRATER
        marker.setPosition(0.2);    //RETRACT MARKER DEPLOYMENT
        telemetry.addData("Status ", " auto done");*/
    }
}