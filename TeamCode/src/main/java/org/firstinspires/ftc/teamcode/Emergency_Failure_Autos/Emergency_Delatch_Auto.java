package org.firstinspires.ftc.teamcode.Emergency_Failure_Autos;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.MecanumLinearOpMode;

@Autonomous(name="Emergency Delatch Auto", group = "auto")
//@Disabled
public class Emergency_Delatch_Auto extends MecanumLinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        init(hardwareMap, true);
        double dist = 48;
        waitForStart();
        lift.setPower(0.75);
        lock.setPosition(1);
        lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        sleep(2000);
        int liftTarget = lift.getCurrentPosition() - 640;
        while (!isStopRequested() && lift.getCurrentPosition() > liftTarget) {
            lift.setPower(-1);
        }
        lift.setPower(0);
        //driveDistance(0.3,0.5);
        double ang = getYaw();
        // rotate(0.2,-ang,false, 2 );
        strafeDistance(-0.3, 7, true); //MOVE A BIT TO TRIGGER CAMERA VIEWING
        lock.setPosition(0);
    }
}