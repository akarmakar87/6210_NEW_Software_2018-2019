package org.firstinspires.ftc.teamcode.SeasonMaterials.Testing;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.SeasonMaterials.MecanumLinearOpMode;

@TeleOp (name = "RangeTest", group = "Sensor")
@Disabled
public class Range extends MecanumLinearOpMode{

    @Override
    public void runOpMode() {

        init(hardwareMap, false);

        waitForStart();

        while (opModeIsActive() && !isStopRequested()){

            telemetry.addData("Distance in inches: ", getRange());
            telemetry.update();
        }
    }
}
