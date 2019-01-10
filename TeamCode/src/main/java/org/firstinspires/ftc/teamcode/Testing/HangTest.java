package org.firstinspires.ftc.teamcode.Testing;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.AutoLinearOpMode;
import org.firstinspires.ftc.teamcode.MecanumLinearOpMode;

@TeleOp(name = "HangTest", group = "Hang")
@Disabled
public class HangTest extends MecanumLinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        init(hardwareMap, false);
        waitForStart();

        while (opModeIsActive() && !isStopRequested()) {
            lift.setPower(gamepad1.left_stick_y * 0.5);
            telemetry.addData("Lift pos", lift.getCurrentPosition());
            telemetry.update();
        }
    }
}
