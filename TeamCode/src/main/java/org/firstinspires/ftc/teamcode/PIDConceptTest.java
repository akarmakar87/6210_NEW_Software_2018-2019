package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.SeasonMaterials.AutoLinearOpMode;

@TeleOp(name="PID concept Test", group="teleop")

public class PIDConceptTest extends AutoLinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        double PV = 0;  //Process Variable: Where we start
        double SP = 50; //SetPoint: Where we want to be
        double Error = SP - PV; //Difference between SP and PV
        double OP = Error / 5; //OutPut: How much we change

        waitForStart();
        while (opModeIsActive() && !isStopRequested()) {
            telemetry.addData("PV = ", PV);
            telemetry.addData("Error = ", Error);
            telemetry.addData("SP = ", SP);
            telemetry.addData("OP = ", OP);
            telemetry.update();
            if (PV < SP-0.1) {
                if (OP > 1){
                    PV = PV + OP;
                }
                else { PV = PV + OP * 5;}
                    sleep(500);

            }
            if (PV > SP+0.1) {
                if (OP < -1){
                    PV = PV + OP;
                }
                else { PV = PV + OP * 5;}
                sleep(500);

            }
            if (gamepad1.a){
                SP = SP + 50;
            }
            if (gamepad1.b){
                SP = SP - 50;
            }

            Error = SP - PV;
            OP = Error / 5;
        }

}
}