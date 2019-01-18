package org.firstinspires.ftc.teamcode.Testing;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.AutoLinearOpMode;
import org.firstinspires.ftc.teamcode.MecanumLinearOpMode;


@Autonomous(name="TestTurn", group = "Sensor")

//@Disabled

public class GyroTest extends MecanumLinearOpMode {

    double power, correction;

    @Override
    public void runOpMode() {

        init(hardwareMap, true);

        waitForStart();

        rotate(0.2, 90, true, 5);

        telemetry.addData("Status", "done");
        telemetry.update();

    }

}