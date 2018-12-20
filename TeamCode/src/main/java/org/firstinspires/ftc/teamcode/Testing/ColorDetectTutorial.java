package org.firstinspires.ftc.teamcode.Testing;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.MecanumLinearOpMode;

import java.util.Arrays;

@TeleOp (name = "ColorTutorial", group = "Sensor")
@Disabled
public class ColorDetectTutorial extends MecanumLinearOpMode{

    @Override
    public void runOpMode() {

        init(hardwareMap, false);

        waitForStart();

        while (opModeIsActive() && !isStopRequested()){

           /* telemetry.addData("All ", Arrays.toString(getAutoColor()));
            telemetry.addData("Hue: ", getAutoColor()[0]);
            telemetry.addData("Saturation: ", getAutoColor()[1]);

            if ((getAutoColor()[0] > 30 && getAutoColor()[0] < 50) && (getAutoColor()[1] > .3)) // Change values later
                telemetry.addData("Gold ", "Detected");
            else*/
                telemetry.addData("Gold", "Not found");

            telemetry.update();
        }
    }
}
