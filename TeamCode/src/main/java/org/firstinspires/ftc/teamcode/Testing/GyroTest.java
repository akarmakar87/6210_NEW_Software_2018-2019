package org.firstinspires.ftc.teamcode.Testing;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.AutoLinearOpMode;


@Autonomous(name="TestTurn", group = "Sensor")

@Disabled

public class GyroTest extends AutoLinearOpMode {

    double power, correction;

    @Override
    public void runOpMode() {

        init(hardwareMap);

        waitForStart();

        rotate(90, 0.2);

        telemetry.addData("Status", "done");
        telemetry.update();

    }

}