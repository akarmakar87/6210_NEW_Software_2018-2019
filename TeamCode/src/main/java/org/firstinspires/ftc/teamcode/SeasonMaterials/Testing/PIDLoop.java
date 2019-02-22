package org.firstinspires.ftc.teamcode.SeasonMaterials.Testing;

import org.firstinspires.ftc.teamcode.SeasonMaterials.AutoLinearOpMode;

public class PIDLoop extends AutoLinearOpMode {


    public void control(){

        double Kp = 0.02; //.025 < PID <.03
        double error = 12; //inches
      //  double totalError = 0;
        double tolerance = 0.01;
        double prevTime = getRuntime();
        while (Math.abs(error) > tolerance)    //not reaching target yet
        {
            double currTime = getRuntime();
            double deltaTime = currTime - prevTime;
            prevTime = currTime;
            double currPosition = RF.getCurrentPosition();
            error = 12 - currPosition;
         //   totalError += error*deltaTime;
            double pTerm = Kp*error;    // This term is proportional to the error
            double motorPower = pTerm;
            setMotorPowers(motorPower, motorPower);
        }
    }
}

