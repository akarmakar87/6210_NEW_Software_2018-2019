package org.firstinspires.ftc.teamcode.SeasonMaterials.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.SeasonMaterials.MecanumLinearOpMode;

@TeleOp(name="MainTeleOp", group="teleop")
//@Disabled
public class MainTeleOp extends MecanumLinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        init(hardwareMap, true);

        double leftPower = 0, rightPower = 0;

        boolean halfSpeed = false;

        boolean servoDown = false;

        telemetry.addData("Mode: ", "Waiting for start");
        telemetry.update();

        waitForStart();

        while (opModeIsActive() && !isStopRequested()) {

            //RIGHT (ACTUALLY LEFT) MOTOR
            if(Math.abs(gamepad1.left_stick_y) > 0.05){
                rightPower = gamepad1.left_stick_y;
            }else{
                rightPower = 0;
            }
            //LEFT (ACTUALLY RIGHT) MOTOR
            if(Math.abs(gamepad1.right_stick_y) > 0.05){
                leftPower = gamepad1.right_stick_y;
            }else{
                leftPower = 0;
            }

            //HALFSPEED
            if (gamepad1.right_trigger > 0.5 || gamepad2.right_trigger > 0.5) {
                halfSpeed = true;
                leftPower = leftPower / 2;
                rightPower = rightPower / 2;
            }else{
                halfSpeed = false;
            }

            //LIFT CONTROLS
            if (gamepad2.right_bumper) {
                lift.setPower(1); //LIFT DOWN
            }else if(gamepad2.left_bumper){
                lift.setPower(-1); //LIFT UP
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
            /*if(gamepad2.a){
                setHook();
            }*/

            //LOCK CONTROLS
            while(gamepad2.dpad_right){    //LOCK
                lock.setPosition(-1);
            }
            while(gamepad2.dpad_left){   //UNLOCK
                lock.setPosition(1);
            }
            while(gamepad2.dpad_down){   //STAY STILL
                lock.setPosition(0.51);
            }

            //Strafe Controls
            while (gamepad1.left_bumper){ //Strafe right - inverted
                setStrafePowers(-1,true);
            }
            while (gamepad1.right_bumper){ //Strafe left - inverted
                setStrafePowers(-1,false);
            }

            setMotorPowers(leftPower, rightPower);

            telemetry.addData("Motors", "left (%.2f), right (%.2f)", leftPower, rightPower)
                    .addData("Half Speed", halfSpeed)
                    .addData("Lift pos", lift.getCurrentPosition());
            telemetry.update();
        }
    }
}
