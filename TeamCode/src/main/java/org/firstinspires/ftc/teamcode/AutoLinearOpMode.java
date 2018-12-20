package org.firstinspires.ftc.teamcode;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.motors.RevRoboticsCoreHexMotor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

import static java.lang.Double.isNaN;

public class AutoLinearOpMode extends LinearOpMode{

    // DECLARE VARIABLES TO BE USED
    ElapsedTime runtime;

    //motors and sensors
    public DcMotor LF;
    public DcMotor RF;
    public DcMotor LB;
    public DcMotor RB;
    public BNO055IMU imu;
    /*
    public DcMotor liftR;
    public DcMotor liftL;
    public DcMotor intake;
    public DcMotor deployment;
    Servo goldHitter;
    Servo boxServo;

    ColorSensor goldSensor = null;
    DistanceSensor sensorDistance = null;
*/
    //gyro variables
    Orientation oldAngle;
    double globalAngle;



    static final double     COUNTS_PER_MOTOR_REV    = 1120 ;    // REV Motor Encoder
    static final double     DRIVE_GEAR_REDUCTION    = 1.0 ;     // This is < 1.0 if geared UP
    static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference

    public double encoderToInches = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION)/(WHEEL_DIAMETER_INCHES * Math.PI); //Multiply desired distance (inches)

    float hsvValues[] = {0F, 0F, 0F};
    final float values[] = hsvValues;
    final double SCALE_FACTOR = 255;

    // INITIALIZE
    public void init(HardwareMap map){
        runtime     = new ElapsedTime();
        LF  = map.dcMotor.get("LF");
        RF  = map.dcMotor.get("RF");
        LB  = map.dcMotor.get("LB");
        RB  = map.dcMotor.get("RB");
        imu            = map.get(BNO055IMU.class, "imu"); // Check which IMU is being used
      /*  liftL  = map.dcMotor.get("liftL");
        liftR  = map.dcMotor.get("liftR");
        intake           = map.dcMotor.get("intake");
        deployment       = map.dcMotor.get("deployment");
        goldHitter     = hardwareMap.servo.get("goldHitter");
        boxServo     = hardwareMap.servo.get("boxServo");
        goldSensor     = map.get(ColorSensor.class, "colorRange"); */


        LF.setDirection(DcMotorSimple.Direction.REVERSE);
        RF.setDirection(DcMotorSimple.Direction.FORWARD);
        RB.setDirection(DcMotorSimple.Direction.FORWARD);
        LB.setDirection(DcMotorSimple.Direction.REVERSE);

        LF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        RF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        LB.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        RB.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // TODO: One way to "hold" the deployment drawbridge in place, but needs encoder wired up

        /*
        deployment.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        deployment.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        deployment.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        deployment.setTargetPosition(100); // Something small, no movement
*/


        int relativeLayoutId = map.appContext.getResources().getIdentifier("RelativeLayout", "id", map.appContext.getPackageName());
        final View relativeLayout = ((Activity) map.appContext).findViewById(relativeLayoutId);

        //SET UP GYRO

        oldAngle = new Orientation();

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.mode                 = BNO055IMU.SensorMode.IMU;
        parameters.angleUnit            = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit            = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled       = false;

        imu.initialize(parameters);

        telemetry.addData("Mode", "calibrating...");
        telemetry.update();

        while (!isStopRequested() && !imu.isGyroCalibrated())
        {
            sleep(50);
            idle();
        }

        telemetry.addData("Status: ", "Initialized");
        telemetry.addData("imu calib status", imu.getCalibrationStatus().toString());
        telemetry.update();
    }

    //SET POWER TO DRIVE MOTORS
    public void setMotorPowers(double leftPower, double rightPower) {
        LF.setPower(Range.clip(leftPower, -1, 1));
        RF.setPower(Range.clip(rightPower, -1, 1));
        LB.setPower(Range.clip(leftPower, -1, 1));
        RB.setPower(Range.clip(rightPower, -1, 1));
    }

    // TIME BASED MOVEMENT
    public void driveTime(double power, long seconds){
        setMotorPowers(power, power);
        sleep(seconds * 1000);
    }

    // TIME BASED MOVEMENT
    public void turnTime(double power, long seconds){
        setMotorPowers(-power, power);
        sleep(seconds * 1000);
    }

    // SET RUNMODE TO DRIVE MOTORS
    public void setMode(DcMotor.RunMode runMode) throws InterruptedException {
        LF.setMode(runMode);
        idle();
        RF.setMode(runMode);
        idle();
        LB.setMode(runMode);
        idle();
        RB.setMode(runMode);
        idle();
    }

    // STOP DRIVE MOTORS
    public void stopMotors(){
        setMotorPowers(0,0);
    }

    // ENCODER BASED MOVEMENT
    public void driveDistance(double power, double distance) throws InterruptedException {
        setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        distance = -1 * distance * encoderToInches;

        LF.setTargetPosition(LF.getCurrentPosition() + (int)distance);
        LB.setTargetPosition(LB.getCurrentPosition() + (int)distance);
        RF.setTargetPosition(RF.getCurrentPosition() + (int)distance);
        RB.setTargetPosition(RB.getCurrentPosition() + (int)distance);

        setMode(DcMotor.RunMode.RUN_TO_POSITION);
        setMotorPowers(power, power);

        while (LF.isBusy() && RF.isBusy() && LB.isBusy() && RB.isBusy() && opModeIsActive() && !isStopRequested()){
            idle();
        }
        stopMotors();
        setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void strafeLeftTime(int time){
        LF.setPower(-1);
        RF.setPower(1);
        LB.setPower(1);
        RB.setPower(-1);
        sleep(time *1000);
    }

    public void strafeRightTime(int time){
        LF.setPower(-1);
        RF.setPower(1);
        LB.setPower(1);
        RB.setPower(-1);
        sleep(time *1000);
    }

    // GET DISTANCE TO OBJECT USING RANGE SENSOR
    /*public double getDist() {
        double dist = sensorDistance.getDistance(DistanceUnit.INCH);
        while (dist > 1000 || isNaN(dist) && opModeIsActive()) {
            dist = sensorDistance.getDistance(DistanceUnit.INCH);
        }
        return dist;
    }*/

    //RESET ANGLE
    public void resetAngle() {
        oldAngle = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
    }

    //GET CURRENT IMU HEADING
    public double getAngle() {
        Orientation currentAngle = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

        double deltaAngle = currentAngle.firstAngle - oldAngle.firstAngle;

        if (deltaAngle < 0)
            deltaAngle *= -1;
        else if (deltaAngle > 0)
            deltaAngle = 360 - deltaAngle;

        return deltaAngle;
    }

    //ROTATE USING GYRO
    public void rotate(int target, double power) {
        runtime.reset();
        double reduction = 1;
        double leftPower = 0;
        double rightPower = 0;
        double dheading = target;

        do {
            resetAngle();

            telemetry.addData("imu heading", "Delta Angle %.2f, Old Angle %.2f, Current Angle %.2f, Target angle %d",
                    getAngle(), oldAngle.firstAngle,
                    imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle,
                    target);
            telemetry.update();

            // getAngle() returns + when rotating counter clockwise (left) and - when rotating clockwise (right).
            dheading = target - oldAngle.firstAngle;
         //   reduction = Math.abs(dheading/target);
            if (dheading > 180)
            {   // turn left.
                leftPower = power;
                rightPower = -power;
            }
            else if (dheading < 180)
            {   // turn right.
                leftPower = -power;
                rightPower = power;
            }

            setMotorPowers(leftPower * reduction,rightPower * reduction);

            // rotate until turn is completed.
            if (dheading > 0 && dheading < 180)
            {
                // On right turn we have to get off zero first.
                while (opModeIsActive() && !isStopRequested() && getAngle() == 0) {
                    idle();
                }

                while (opModeIsActive() && !isStopRequested() && getAngle() < target) {
                    idle();
                }
            }
            else if(dheading > 0 && dheading > 180){
                // left turn.
                while (opModeIsActive() && !isStopRequested() && getAngle() == 0) {
                    idle();
                }

                while (opModeIsActive() && !isStopRequested() && getAngle() > target) {
                    idle();
                }
            }
        } while (opModeIsActive() && !isStopRequested()&& dheading <= 5); // Add in runtime limit later

        /*

        runtime.reset();
        double leftPower = 0;
        double rightPower = 0;
        double heading = degrees;

            resetAngle();

            telemetry.addData("imu heading", oldAngle.firstAngle);
            telemetry.addData("global heading", globalAngle);
            telemetry.update();

            // getAngle() returns + when rotating counter clockwise (left) and - when rotating clockwise (right).

            if (heading < 0)
            {   // turn right.
                leftPower = -power;
                rightPower = power;
            }
            else if (heading > 0)
            {   // turn left.
                leftPower = power;
                rightPower = -power;
            }

            setMotorPowers(leftPower,rightPower);

            // rotate until turn is completed.
            if (degrees < 0)
            {
                // On right turn we have to get off zero first.
                while (opModeIsActive() && getAngle() == 0) {}

                while (opModeIsActive() && getAngle() > degrees) {}
            }
            else    // left turn.
                while (opModeIsActive() && getAngle() < degrees) {}
*/
            stopMotors();
            sleep(1000);
            resetAngle();
    }

   /* //GET COLOR OF MINERAL
   public float[] getAutoColor() {
        Color.RGBToHSV((int) (goldSensor.red() * SCALE_FACTOR),
                (int) (goldSensor.green() * SCALE_FACTOR),
                (int) (goldSensor.blue() * SCALE_FACTOR),
                hsvValues);
        return hsvValues;
    }

    //CHECK IF MINERAL IS GOLD
   public boolean isGold(){
        if ((getAutoColor()[0] > 30 && getAutoColor()[0] < 50) && (getAutoColor()[1] > .35)) {
            telemetry.addData("Gold ", "Detected");
            telemetry.update();
            return true;
        }else {
            telemetry.addData("Gold", "Not found");
            telemetry.update();
            return false;
        }
    }*/

    //KNOCK OFF GOLD
    public void knockGold(){
       // goldHitter.setPosition(0.75); // Set servo position
        //telemetry.addData("status ", "knocked gold");
        //telemetry.update();
    }

   public void openBox(){
        //boxServo.setPosition(1); // Set servo position
        //telemetry.addData("status ", "box open");
        //telemetry.update();
    }

    public void closeBox(){
        //boxServo.setPosition(1); // Set servo position
       // telemetry.addData("status ", "box closed");
        //telemetry.update();
    }

   public void expel(long time){
       // intake.setPower(-1);
       // sleep(time);
      //  intake.setPower(0);
    }
    public void takeIn(long time){
        //intake.setPower(1);
       // sleep(time);
        //intake.setPower(0);
    }

    //DEPLOYING AND RETRACTING INTAKE METHODS IF WE NEED THEM
    public void deploy(long time){
        //deployment.setPower(0.5);
        //sleep(time);
    }

    public void pullBack(long time){
        //deployment.setPower(-1);
        //sleep(time);
    }

    //SET WAIT TIME IN AUTO
    public int getWait(){
        int seconds = 0;
        do{
            if (gamepad1.x)
                seconds++;
        }while (!gamepad1.y && runtime.time() < 10);
        return seconds;
    }

    @Override
    public void runOpMode() throws InterruptedException {

    }
}