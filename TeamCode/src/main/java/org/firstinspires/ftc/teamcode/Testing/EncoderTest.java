package org.firstinspires.ftc.teamcode.Testing;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.AutoLinearOpMode;
import org.firstinspires.ftc.teamcode.MecanumLinearOpMode;

@Autonomous(name="EncoderTest", group = "auto")

@Disabled

public class EncoderTest extends MecanumLinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        init(hardwareMap, false);

        telemetry.addData("Status", "Initialized");
        telemetry.addData("LF encoder:", LF.getCurrentPosition());
        telemetry.addData("LB encoder:", LB.getCurrentPosition());
        telemetry.addData("RF encoder:", RF.getCurrentPosition());
        telemetry.addData("RB encoder:", RB.getCurrentPosition());
        telemetry.update();

        waitForStart();

        strafeDistance(0.4, 20, true);

        stopMotors();

        telemetry.addData("LF encoder:", LF.getCurrentPosition());
        telemetry.addData("LB encoder:", LB.getCurrentPosition());
        telemetry.addData("RF encoder:", RF.getCurrentPosition());
        telemetry.addData("RB encoder:", RB.getCurrentPosition());
        telemetry.addData("encoder avg: ", getEncoderAvg());
        telemetry.update();

        sleep(20000);
    }
}