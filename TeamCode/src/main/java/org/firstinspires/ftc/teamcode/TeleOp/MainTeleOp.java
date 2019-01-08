package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.MecanumLinearOpMode;

@TeleOp(name="MainTeleOp", group="teleop")
//@Disabled
public class MainTeleOp extends MecanumLinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        init(hardwareMap, false);

        double leftPower = 0, rightPower = 0, scale = 1;

        boolean halfSpeed = false;

        boolean servoDown = false;
        boolean locked = false;

        double lockpos = 0;

        telemetry.addData("Mode: ", "Waiting for start");
        telemetry.update();

        waitForStart();

        while (opModeIsActive() && !isStopRequested()) {

            //left motor
            if(Math.abs(gamepad1.left_stick_y) > 0.05){
                rightPower = -gamepad1.left_stick_y * scale;
            }else{
                rightPower = 0;
            }
            //right motor
            if(Math.abs(gamepad1.right_stick_y) > 0.05){
                leftPower = -gamepad1.right_stick_y * scale;
            }else{
                leftPower = 0;
            }
//h
            //halfspeed
            if (gamepad1.right_trigger > 0.5 || gamepad2.right_trigger > 0.5) {
                halfSpeed = true;
                leftPower = leftPower / 2;
                rightPower = rightPower / 2;
            }else{
                halfSpeed = false;
            }

            //lift
            if (gamepad2.right_bumper) {
                lift.setPower(1); //Lift Down
            }else if(gamepad2.left_bumper){
                lift.setPower(-1); //Lift Up
            }else{
                lift.setPower(0);
            }

            //Marker Deployment
            if (gamepad2.x) {
                marker.setPosition(0.41); //Bring Marker Up
            }else if (gamepad2.y) {
                marker.setPosition(0.2); //Bring Marker Down
            }

            //DON'T NEED BECAUSE AUTO GETS IT OUT OF THE WAY AND DON'T NEED IT IN TELE-OP
            if(gamepad2.a){
                setHook();
            }

            //Strafe Controls
            if (gamepad1.left_bumper){ //Strafe right
                LF.setPower(-1);
                RF.setPower(1);
                LB.setPower(1);
                RB.setPower(-1);
            }else if(gamepad1.right_bumper){ //Strafe left
                    LF.setPower(1);
                    RF.setPower(-1);
                    LB.setPower(-1);
                    RB.setPower(1);
            }
            setMotorPowers(leftPower, rightPower);

            telemetry.addData("Motors", "left (%.2f), right (%.2f)", leftPower, rightPower)
                    .addData("Half Speed", halfSpeed)
                    .addData("lock pos: ", lock.getPosition())
                    .addData("Lift pos", lift.getCurrentPosition());
            telemetry.update();
        }
    }
}
