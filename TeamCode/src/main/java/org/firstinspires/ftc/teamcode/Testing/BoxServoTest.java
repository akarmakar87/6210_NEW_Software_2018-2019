package org.firstinspires.ftc.teamcode.Testing;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.AutoLinearOpMode;
import org.firstinspires.ftc.teamcode.MecanumLinearOpMode;

@TeleOp(name = "BoxServoTest", group = "Sensor")
//@Disabled
public class BoxServoTest extends MecanumLinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        init(hardwareMap, false);

        waitForStart();

            marker.setPosition(0.41);
            sleep(1000);
            marker.setPosition(0.2);
            sleep(1000);
            telemetry.addData("position: ", 0);
            telemetry.update();
    }
}
