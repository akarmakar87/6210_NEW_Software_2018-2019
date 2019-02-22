package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.SeasonMaterials.MecanumLinearOpMode;

@Autonomous(name="TurnTest", group = "auto")

@Disabled

public class TurnTest extends MecanumLinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        init(hardwareMap, true);

        waitForStart();

        turnPID(90, 0.2, 0.5, 0.1, 5);
    }
}