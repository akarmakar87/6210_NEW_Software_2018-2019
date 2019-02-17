package org.firstinspires.ftc.teamcode.SeasonMaterials.Testing;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.SeasonMaterials.MecanumLinearOpMode;

@TeleOp(name = "LockServoTest", group = "Sensor")
@Disabled
public class LockServoTest extends MecanumLinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        init(hardwareMap, false);

        waitForStart();

        //sleep(1000);
        lock.setPosition(1);
        sleep(1000);
        lock.setPosition(0.51);
        sleep(5000);
        telemetry.addData("position: ", 0);
        telemetry.update();
    }
}

//0.51 stops the servo's movement