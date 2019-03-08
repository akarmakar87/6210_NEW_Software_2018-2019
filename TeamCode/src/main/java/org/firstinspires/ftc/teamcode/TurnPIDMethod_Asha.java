package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.SeasonMaterials.AutoLinearOpMode;
import org.firstinspires.ftc.teamcode.SeasonMaterials.MecanumLinearOpMode;

@Autonomous(name="TurnPIDTestAsha", group="auto")

public class TurnPIDMethod_Asha extends MecanumLinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {}

    public void turnPID(double tAngle, double kP, double kI, double kD, double timeOut){
        double power, prevError, error, dT, prevTime, currTime, P, I, D; //DECLARE ALL VARIABLES
        prevError = error = tAngle - getYaw(); //INITIALIZE THESE VARIABLES
        power = dT = prevTime = currTime = P = I = D = 0;
        ElapsedTime time = new ElapsedTime(); //CREATE NEW TIME OBJECT
        resetTime();
        while (Math.abs(error) > 0.5 && currTime < timeOut){
            prevError = error;
            error = tAngle - getYaw(); //GET ANGLE REMAINING TO TURN (tANGLE MEANS TARGET ANGLE, AS IN THE ANGLE YOU WANNA GO TO)
            prevTime = currTime;
            currTime = time.milliseconds();
            dT = currTime - prevTime; //GET DIFFERENCE IN CURRENT TIME FROM PREVIOUS TIME
            P = error;
            I = error * dT;
            D = (error - prevError)/dT;
            power = P * kP + I * kI + D * kD;
            setMotorPowers(Range.clip(power, 0.2, 1), -Range.clip(power, 0.2, 1));

            telemetry.addData("tAngle: ", tAngle)
                    .addData("P:", P)
                    .addData("I:", I)
                    .addData("D:", D)
                    .addData("power", power)
                    .addData("error: ", error)
                    .addData("currTime: ", currTime);
        }
    }

    public void turnP(double tAngle, double kP, double timeOut){
        double power, prevError, error, dT, prevTime, currTime, P; //DECLARE ALL VARIABLES
        prevError = error = tAngle - getYaw(); //INITIALIZE THESE VARIABLES
        power = dT = prevTime = currTime = P = 0;
        ElapsedTime time = new ElapsedTime(); //CREATE NEW TIME OBJECT
        resetTime();
        while (Math.abs(error) > 0.5 && currTime < timeOut){
            prevError = error;
            error = tAngle - getYaw(); //GET ANGLE REMAINING TO TURN (tANGLE MEANS TARGET ANGLE, AS IN THE ANGLE YOU WANNA GO TO)
            prevTime = currTime;
            currTime = time.milliseconds();
            dT = currTime - prevTime; //GET DIFFERENCE IN CURRENT TIME FROM PREVIOUS TIME
            P = error;
            power = P * kP;
            setMotorPowers(Range.clip(power, 0.2, 1), -Range.clip(power, 0.2, 1));

            telemetry.addData("tAngle: ", tAngle)
                    .addData("P:", P)
                    .addData("power", power)
                    .addData("error: ", error)
                    .addData("currTime: ", currTime);
        }
    }

    public void turnPI(double tAngle, double kP, double kI, double timeOut){
        double power, prevError, error, dT, prevTime, currTime, P, I; //DECLARE ALL VARIABLES
        prevError = error = tAngle - getYaw(); //INITIALIZE THESE VARIABLES
        power = dT = prevTime = currTime = P = I = 0;
        ElapsedTime time = new ElapsedTime(); //CREATE NEW TIME OBJECT
        resetTime();
        while (Math.abs(error) > 0.5 && currTime < timeOut){
            prevError = error;
            error = tAngle - getYaw(); //GET ANGLE REMAINING TO TURN (tANGLE MEANS TARGET ANGLE, AS IN THE ANGLE YOU WANNA GO TO)
            prevTime = currTime;
            currTime = time.milliseconds();
            dT = currTime - prevTime; //GET DIFFERENCE IN CURRENT TIME FROM PREVIOUS TIME
            P = error;
            I = error * dT;
            power = P * kP + I * kI;
            setMotorPowers(Range.clip(power, 0.2, 1), -Range.clip(power, 0.2, 1));

            telemetry.addData("tAngle: ", tAngle)
                    .addData("P:", P)
                    .addData("I:", I)
                    .addData("power", power)
                    .addData("error: ", error)
                    .addData("currTime: ", currTime);
        }
    }

    public void turnPD(double tAngle, double kP, double kD, double timeOut){
        double power, prevError, error, dT, prevTime, currTime, P, D; //DECLARE ALL VARIABLES
        prevError = error = tAngle - getYaw(); //INITIALIZE THESE VARIABLES
        power = dT = prevTime = currTime = P = D = 0;
        ElapsedTime time = new ElapsedTime(); //CREATE NEW TIME OBJECT
        resetTime();
        while (Math.abs(error) > 0.5 && currTime < timeOut){
            prevError = error;
            error = tAngle - getYaw(); //GET ANGLE REMAINING TO TURN (tANGLE MEANS TARGET ANGLE, AS IN THE ANGLE YOU WANNA GO TO)
            prevTime = currTime;
            currTime = time.milliseconds();
            dT = currTime - prevTime; //GET DIFFERENCE IN CURRENT TIME FROM PREVIOUS TIME
            P = error;
            D = (error - prevError)/dT;
            power = P * kP + D * kD;
            setMotorPowers(Range.clip(power, 0.2, 1), -Range.clip(power, 0.2, 1));

            telemetry.addData("tAngle: ", tAngle)
                    .addData("P:", P)
                    .addData("D:", D)
                    .addData("power", power)
                    .addData("error: ", error)
                    .addData("currTime: ", currTime);
        }
    }


}