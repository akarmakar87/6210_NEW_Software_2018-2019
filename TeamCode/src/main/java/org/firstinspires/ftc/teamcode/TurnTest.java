package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.SeasonMaterials.MecanumLinearOpMode;

@Autonomous(name="TurnTest", group = "auto")

@Disabled

public class TurnTest extends TurnPIDMethod_Asha {

    @Override
    public void runOpMode() throws InterruptedException {

        init(hardwareMap, true);

        waitForStart();

        turnP(90, 0.6/90,5);
        telemetry.addData("Turn P","Complete");
        turnPI(90, 0.6/90, 0.5, 5);
        telemetry.addData("Turn PI","Complete");
        turnPD(90, 0.6/90, 0.1, 5);
        telemetry.addData("Turn PD","Complete");
        turnPID(90, 0.6/90, 0.5, 0.1, 5);
        telemetry.addData("Turn PID","Complete");


    }
}