package org.firstinspires.ftc.teamcode.Testing;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.AutoLinearOpMode;

@Autonomous(name = "GoldServoTest", group = "Sensor")
@Disabled
public class GoldServoTest extends AutoLinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        init(hardwareMap);

        waitForStart();

        /*goldHitter.setPosition(0);
        telemetry.addData("Pos: ", 0);
        telemetry.update();
        sleep(2000);
        goldHitter.setPosition(45);
        telemetry.addData("Pos: ", 45);
        telemetry.update();
        sleep(2000);
        goldHitter.setPosition(90);
        telemetry.addData("Pos: ", 90);
        telemetry.update();
        sleep(2000);*/

        telemetry.addData("Status", "Finished");
        telemetry.update();

    }
}
