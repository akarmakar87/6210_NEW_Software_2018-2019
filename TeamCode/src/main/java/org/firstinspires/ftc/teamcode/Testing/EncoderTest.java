package org.firstinspires.ftc.teamcode.Testing;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.AutoLinearOpMode;
import org.firstinspires.ftc.teamcode.MecanumLinearOpMode;

@Autonomous(name="MoveTest", group = "auto")

@Disabled

public class EncoderTest extends MecanumLinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        init(hardwareMap, true);

        waitForStart();

        strafeDistance(0.5, 10, true);
    }
}