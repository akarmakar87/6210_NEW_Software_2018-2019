package org.firstinspires.ftc.teamcode.Testing;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.MecanumLinearOpMode;

@Autonomous(name="MecanumAutoDepot", group = "auto")
@Disabled
public class MecanumAutoDepot extends MecanumLinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        /*init(hardwareMap, true);
        double dist = 48;
        double DD = 0;  //DD = Depot Distance and will account for varying distances to the depot depending on the mineral placement
        waitForStart();
        lift.setPower(0.90);    //LIFT PULLS ROBOT UP (to release tension on the lock)
        lock.setPosition(1);    //UNLOCK LIFT
        lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT); //LET GRAVITY TAKE THE ROBOT DOWN
        sleep(  2000);
        lock.setPosition(0.5); //Stop Servo Movement
        int liftTarget = lift.getCurrentPosition()-640; //FIND HOW FAR THE LIFT NEEDS TO RETRACT
        while (!isStopRequested() && lift.getCurrentPosition() > liftTarget){   //RETRACT LIFT
            lift.setPower(-1);
        }
        lift.setPower(0);
        double ang = getYaw();  //Why do we need this???
        strafeDistance(-0.3, 7, true); //MOVE A BIT TO TRIGGER CAMERA VIEWING
        //lock.setPosition(0);  //Don't need to relock
        int gold = findGold(5); //GET GOLD POSITION
        int x = 0;
        double angleOff = 0;
        telemetry.addData("gold is at", gold);
        telemetry.addData("align is ", checkAlign());
        telemetry.update();
        driveDistance(-0.3,7); //MOVE FORWARD OUT OF LANDER ZONE
        resetTime();
        switch (gold){
            case 2:
                while (!checkAlign() && !isStopRequested() && getTime() < 6){ //IF GOLD IN CENTER, ADJUST
                    //if (getXpos() < 400){
                    //    LF.setPower(0.3);
                    //    RF.setPower(-0.3);
                    //    LB.setPower(-0.4);
                    //    RB.setPower(0.4);
                    //}else{
                        LF.setPower(-0.4);
                        RF.setPower(0.4);
                        LB.setPower(0.4);
                        RB.setPower(-0.4);
                        DD = 9; //The robot is normally 9in short on depot strafe
                    //}
                }
                strafeDistance(-0.25, 3, true); // goes a bit left to not hit the right mineral off too
                sleep(1000);
                driveDistance(-0.3, 5); //PUSH AND BACK UP
                sleep(1000);
                x= 5;
                break;
            case 1:
                while (!checkAlign() && !isStopRequested() && getTime() < 6){ //IF GOLD ON LEFT, MOVE LEFT TIL ALIGNED
                    LF.setPower(0.4);
                    RF.setPower(-0.4);
                    LB.setPower(-0.4);
                    RB.setPower(0.4);
                }
                strafeDistance(-0.25, 3, true);
                sleep(1000);
                driveDistance(-0.3, 5); //PUSH AND BACK UP
                sleep(1000);
                x = 10;
                break;
            case 3:
                while (!checkAlign() && !isStopRequested() && getTime() < 4){ //IF GOLD ON RIGHT, MOVE RIGHT TIL ALIGNED
                    LF.setPower(-0.4);
                    RF.setPower(0.4);
                    LB.setPower(0.4);
                    RB.setPower(-0.4);
                }
                driveDistance(-0.3, 7); //PUSH AND BACK UP
                sleep(1000);
                break;
        }
        driveDistance(0.3, 6.5);
        angleOff = getYaw(); //UPDATE ANGLE
        disableDetector();
        rotate(0.2, 90 - angleOff, true, 5);
        driveDistance(-0.5, 18 - x); //MOVE TOWARD WALL
        rotate(0.2, 45, false, 5);  //TURN TOWARD WALL
        driveDistance(-0.4, 12);    //ALIGN WITH WALL (by running into it)
        driveDistance(0.4, 0.5);    //BACK UP FROM WALL (to not get stuck on it)
        strafeDistance(0.7, 35 + DD,true);  //STRAFE INTO DEPOT
        driveDistance(-0.4,5);  //REALIGN WITH WALL (to not hit the mineral)
        marker.setPosition(0.41);  //DEPLOY MARKER
        sleep(1000);
        //marker.setPosition(0);
        strafeDistance(0.7, 36.5,false);    //STRAFE TOWARD CRATER
        driveDistance(-0.4,2.5);    //REALIGN WITH WALL (so we don't hit our alliance's mineral)
        strafeDistance(0.8, dist,false);    //STRAFE INTO CRATER
        marker.setPosition(0.2);    //RETRACT MARKER DEPLOYMENT
        telemetry.addData("Status ", " auto done");*/
    }
}

