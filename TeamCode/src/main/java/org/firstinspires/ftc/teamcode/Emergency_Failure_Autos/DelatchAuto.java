package org.firstinspires.ftc.teamcode.Emergency_Failure_Autos;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.teamcode.MecanumLinearOpMode;

@Autonomous(name="DelatchAuto", group = "auto")
//@Disabled
public class DelatchAuto extends MecanumLinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        init(hardwareMap, true);

        waitForStart();

        unlatch();

        driveDistance(1, 5);
    }
}