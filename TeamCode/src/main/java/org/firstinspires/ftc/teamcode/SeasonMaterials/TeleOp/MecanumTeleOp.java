package org.firstinspires.ftc.teamcode.SeasonMaterials.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.SeasonMaterials.AutoLinearOpMode;

@TeleOp(name="MecanumTeleOp", group="teleop")
@Disabled
public class MecanumTeleOp extends AutoLinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        init(hardwareMap);

        //setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        double leftPower = 0, rightPower = 0, scale = 1, liftPower = 0, intakePower = 1;

        boolean halfSpeed = false;

        telemetry.addData("Mode: ", "Waiting for start");
        telemetry.update();

        waitForStart();

        //runtime.reset();

        while (opModeIsActive() && !isStopRequested()) {

            //Found this online and at a quick glance it seems good but I will research it more
            double r = Math.hypot(gamepad1.left_stick_x, gamepad1.left_stick_y);
            double robotAngle = Math.atan2(gamepad1.left_stick_y, gamepad1.left_stick_x) - Math.PI / 4;
            double rightX = gamepad1.right_stick_x;
            final double v1 = r * Math.cos(robotAngle) + rightX;
            final double v2 = r * Math.sin(robotAngle) - rightX;
            final double v3 = r * Math.sin(robotAngle) + rightX;
            final double v4 = r * Math.cos(robotAngle) - rightX;

            LF.setPower(v1);
            RF.setPower(v2);
            LB.setPower(v3);
            RB.setPower(v4);

            setMotorPowers(leftPower, rightPower);

            telemetry.addData("Status", "Run Time: "); //+ /runtime.toString())
                    //.addData("Motors", "left (%.2f), right (%.2f)", leftPower, rightPower)
                    //.addData("Half Speed", halfSpeed)
                    //.addData("Deployment", "Power %.2f - Position %d", deployment.getPower(), deployment.getCurrentPosition());
           // telemetry.addData("status: ", deployment.getCurrentPosition());
            telemetry.update();
        }
    }
}
