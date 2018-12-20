package org.firstinspires.ftc.teamcode.Testing;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.MecanumLinearOpMode;

@Autonomous(name = "IntakeTest", group = "teleop")
@Disabled
public class IntakeTest extends MecanumLinearOpMode{

    @Override
    public void runOpMode() throws InterruptedException {

        init(hardwareMap, false);

        waitForStart();

        telemetry.addData("status", "expelling");
        telemetry.update();
        //expel(3000);
        telemetry.addData("status", "taking in");
        telemetry.update();
        //takeIn(3000);
        telemetry.addData("status", "done");
        telemetry.update();


    }
}