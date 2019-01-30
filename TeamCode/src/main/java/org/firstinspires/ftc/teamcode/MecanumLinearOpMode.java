package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;


public class MecanumLinearOpMode extends LinearOpMode{

    // DECLARE VARIABLES TO BE USED
    ElapsedTime runtime;

    //motors and sensors
    public DcMotor LF;
    public DcMotor RF;
    public DcMotor LB;
    public DcMotor RB;
    public DcMotor intakeL;
    public DcMotor intakeR;
    public DcMotor spinner;
    public BNO055IMU imu;
    public DcMotor lift;
    public Servo marker;
    public Servo lock;
    DistanceSensor range;

    //gyro variables
    Orientation angles;

    static final double     COUNTS_PER_MOTOR_REV    = 1120 ;    // REV Motor Encoder
    static final double     DRIVE_GEAR_REDUCTION    = 1.0 ;     // This is < 1.0 if geared UP
    static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference

    public double encoderToInches = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION)/(WHEEL_DIAMETER_INCHES * Math.PI); //Multiply desired distance (inches)

    //Camera
    public static final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
    public static final String LABEL_GOLD_MINERAL = "Gold Mineral";
    public static final String LABEL_SILVER_MINERAL = "Silver Mineral";

    public static final String VUFORIA_KEY = "AQt2xVL/////AAABmXIVKUnTcEJbqvVBjp/Sw/9SqarohYyKotzRjT/Xl1/S8KDwsFHv/zYw6rXqXTjKrnjk92GfBA4hbZaQP17d1N6BiBuXO2W/hFNoMGxiF+fWlnvtDmUM1H/MF9faMOjZcPNjnQ7X8DVwdDDha3A3aqaoegefkKxb4A5EjP8Xcb0EPJ1JA4RwhUOutLbCDJNKUq6nCi+cvPqShvlYTvXoROcOGWSIrPxMEiOHemCyuny7tJHUyEg2FTd2upiQygKAeD+LN3P3cT02aK6AJbQ0DlQccxAtoo1+b//H6/eGro2s0fjxA2dH3AaoHB7qkb2K0Vl7ReFEwX7wmqJleamNUG+OZu7K3Zm68mPudzNuhAWQ";
    public VuforiaLocalizer vuforia;
    public TFObjectDetector tfod;

    // INITIALIZE
    public void init(HardwareMap map, boolean auto){

        runtime     = new ElapsedTime();
        LF  = map.dcMotor.get("LF");
        RF  = map.dcMotor.get("RF");
        LB  = map.dcMotor.get("LB");
        RB  = map.dcMotor.get("RB");
        spinner = map.dcMotor.get("spinner");
        intakeL  = map.dcMotor.get("intakeL");
        intakeR  = map.dcMotor.get("intakeR");
        marker = map.servo.get("marker");
        range     = map.get(DistanceSensor.class, "range");
        imu = map.get(BNO055IMU.class, "imu"); // Check which IMU is being used

        lift  = map.dcMotor.get("lift");
        lock  = map.servo.get("lock");

        LF.setDirection(DcMotorSimple.Direction.FORWARD);
        RF.setDirection(DcMotorSimple.Direction.REVERSE);
        RB.setDirection(DcMotorSimple.Direction.REVERSE);
        LB.setDirection(DcMotorSimple.Direction.FORWARD);


        LF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        RF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        LB.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        RB.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        resetEncoders();

        //SET UP GYRO
        angles = new Orientation();

        if (auto) {
            BNO055IMU.Parameters bparameters = new BNO055IMU.Parameters();
            bparameters.mode = BNO055IMU.SensorMode.IMU;
            bparameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
            bparameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
            bparameters.loggingEnabled = false;

            imu.initialize(bparameters);

            angles = imu.getAngularOrientation(); //GET ORIENTATION

            telemetry.addData("Mode", "calibrating...");
            telemetry.update();

            while (!isStopRequested() && !imu.isGyroCalibrated()) {
                sleep(50);
                idle();
            }

            telemetry.addData("imu calib status", imu.getCalibrationStatus().toString());
            telemetry.update();
        }
        telemetry.addData("Status: ", "Initialized");
        telemetry.update();
    }

    //SET POWER TO DRIVE MOTORS
    public void setMotorPowers(double leftPower, double rightPower) {
        LF.setPower(Range.clip(leftPower, -1, 1));
        RF.setPower(Range.clip(rightPower, -1, 1));
        LB.setPower(Range.clip(leftPower, -1, 1));
        RB.setPower(Range.clip(rightPower, -1, 1));
    }

    public void setStrafePowers(double power, boolean right){
        if (right){
            LF.setPower(-Range.clip(power, -1, 1));
            RF.setPower(Range.clip(power, -1, 1));
            LB.setPower(Range.clip(power, -1, 1));
            RB.setPower(-Range.clip(power, -1, 1));
        }else{
            LF.setPower(Range.clip(power, -1, 1));
            RF.setPower(-Range.clip(power, -1, 1));
            LB.setPower(-Range.clip(power, -1, 1));
            RB.setPower(Range.clip(power, -1, 1));
        }

    }

    public void strafeAdjust(double power, double distance, boolean right, int timeout){

        double deltaHeading = 0;

        resetEncoders();
        while (getEncoderAvg() < distance * 55 && !isStopRequested()) {
           deltaHeading = getYaw();
           power = Range.clip(deltaHeading/0.5, 0.25, 1);

           if (Math.abs(getYaw()-90) > 5){
               driveTime(1,0.5);
           }else{
               setStrafePowers(power,right);
           }

        }
    }

    // TIME BASED MOVEMENT
    public void driveTime(double power, double seconds){
        setMotorPowers(power, power);
        long time = (long)(seconds) * 1000;
        sleep(time);
    }

    // TIME BASED TURNING
    public void turnTime(double power, boolean right, long seconds){
        if (right)
            setMotorPowers(power, -power);
        else
            setMotorPowers(-power, power);

        sleep(seconds);
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

    public int getEncoderAvg(){
        //divided by three for now because RB encoder returns 0
        int avg = (Math.abs(LF.getCurrentPosition()) + Math.abs(RF.getCurrentPosition()) + Math.abs(LB.getCurrentPosition()) + Math.abs(RB.getCurrentPosition()))/3;
        return avg;
    }

    public void resetEncoders(){
        RF.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        idle();
        RB.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        idle();
        LF.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        idle();
        LB.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        idle();

        RF.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        idle();
        RB.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        idle();
        LF.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        idle();
        LB.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        idle();
    }

    public void resetLift(){
        lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        idle();

        lift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        idle();
    }

    // ENCODER BASED MOVEMENT - FIXED

    public void driveDistance(double power, double distance) throws InterruptedException{
        resetEncoders();

        while (!isStopRequested() && getEncoderAvg() < distance * encoderToInches){
            setMotorPowers(power, power);
        }

        stopMotors();
    }

    public void strafeDistance(double power, double distance, boolean right) throws InterruptedException{
        resetEncoders();
        while (getEncoderAvg() < distance * 55 && !isStopRequested()){
            if (right){
                LF.setPower(power);
                RF.setPower(-power);
                LB.setPower(-power);
                RB.setPower(power);
            }else {
                LF.setPower(-power);
                RF.setPower(power);
                LB.setPower(power);
                RB.setPower(-power);
            }
        }
        stopMotors();
    }

    //GET ANGLE
    public double getYaw() {
        angles = imu.getAngularOrientation();
        return angles.firstAngle;
    }


    //ROTATE USING GYRO
    public void rotate(double targetAngleChange, int timeout) {

        runtime.reset();

        double power = 0;
        double origDiff = getYaw() - targetAngleChange;
        double deltaHeading = 0;

        while ((Math.abs(getYaw()-targetAngleChange) > 1) && opModeIsActive() && (runtime.seconds() < timeout)) {

            telemetry.addData("Turning:", "From " + getYaw() + " to " + targetAngleChange);
            telemetry.update();

            deltaHeading = getYaw() - targetAngleChange; //GET ANGLE LEFT UNTIL TARGET ANGLE
            power = Range.clip(0.4 * deltaHeading/origDiff, 0.2, 1); //PROPORTIONAL SPEED
            /** Why is dHeading/oDiff multiplied by 0.4? -Garrett **/
            if (deltaHeading < -180 || (deltaHeading > 0 && deltaHeading < 180) ) { //LEFT IS + , RIGHT IS -
                LF.setPower(power);
                LB.setPower(power);
                RF.setPower(-power);
                RB.setPower(-power);
            } else {
                LF.setPower(-power);
                LB.setPower(-power);
                RF.setPower(power);
                RB.setPower(power);
            }
        }
        stopMotors();
    }

    //GOLD SAMPLING

    int pos = 2;

    public void findGold(double timeLimit){

        runtime.reset();
        activateDetector();
        while (runtime.seconds() < timeLimit && opModeIsActive()){
            List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions(); //MAKE LIST OF OBJECTS DETECTED
            if (updatedRecognitions != null) { //IF LIST IS NOT NULL
                telemetry.addData("# of Objects Detected", updatedRecognitions.size()); //GET # OF OBJECTS DETECTED
                if (updatedRecognitions.size() > 0) { //IF DETECT BOTH OBJECTS
                    int goldMineralX = -1;
                    double goldMineralConf = 0;
                    double goldHeight = 0;
                    int silverMineral1X = -1;
                    int silverMineral2X = -1;
                    for (Recognition recognition : updatedRecognitions) {
                        if (recognition.getLabel().equals(LABEL_GOLD_MINERAL)) { //IF OBJECT DETECTED IS GOLD
                            goldMineralX = (int) recognition.getLeft();
                            goldMineralConf = recognition.getConfidence();
                            goldHeight = recognition.getHeight();
                        } else if (silverMineral1X == -1) {
                            silverMineral1X = (int) recognition.getLeft();
                        } else {
                            silverMineral2X = (int) recognition.getLeft();
                        }
                    }
                    if (goldMineralX != -1 && goldMineralConf > 0.2 && goldHeight > 50) {
                        //IF CONFIDENCE LEVEL DOESN'T WORK, TOP VALUE RESTRICTION OR HEIGHT CHECK
                        //MAKE SURE TO ACTUALLY TEST OUT THESE CONDITIONS BEFORE DECIDING TO USE THEM IN AUTO
                        //CHECK FOR CONFIDENCE AND HEIGHT OF A NORMAL GOLD SAMPLE VS SNEAKY CRATER GOLD
                        if (goldMineralX < 600 || goldMineralX < silverMineral1X) {
                            telemetry.addData("Gold Mineral Position", "Left");
                            pos = 1;
                        } else{
                            telemetry.addData("Gold Mineral Position", "Center");
                            pos = 2;
                        }
                    }else{
                        telemetry.addData("Gold Mineral Position", "Right");
                        pos = 3;
                    }
                    telemetry.addData("Gold x pos ", goldMineralX);
                    telemetry.addData("Gold conf ", goldMineralConf);
                    telemetry.addData("Gold height ", goldHeight);
                }
                telemetry.addData("Runtime", getTime());
                telemetry.update();
            }
        }
        telemetry.addData("Done with find gold", " ");
        sleep(1000);
        disableDetector();
    }

    public int retPos(){
        return pos;
    }

    public void resetTime(){
        runtime.reset();
    }

    public double getTime(){
        return runtime.seconds();
    }

    public void disableDetector(){
        tfod.deactivate();
        tfod.shutdown();
    }

    public void pushGoldEm(int goldpos) throws InterruptedException {
        resetTime();
        telemetry.addData("gold is ", goldpos);
        telemetry.update();
        switch (goldpos){
            case 1:
                rotate( -35, 4);
                sleep(1000);
                driveDistance(-0.4, 9.5); //PUSH AND BACK UP
                sleep(1000);
                driveDistance(0.3, 5);
                disableDetector();
                break;
            case 2:
                sleep(1000);
                driveDistance(-0.4, 9.5); //PUSH AND BACK UP
                sleep(1000);
                driveDistance(0.3, 5.5);
                disableDetector();
                break;

            case 3:
                strafeDistance(0.5, 7, true);
                //rotate(0.3, 30, false, 4);
                rotate(30,4);
                sleep(1000);
                driveDistance(-0.4, 9.5); //PUSH AND BACK UP
                sleep(1000);
                driveDistance(0.3, 5.5);
                disableDetector();
                break;
        }
        sleep(1000);
    }

    public double pushGold(int goldpos, boolean crater, double offset) throws InterruptedException {
        double x = 0;
        resetTime();
        telemetry.addData("gold is ", goldpos);
        telemetry.update();
        switch (goldpos){
            case 1:
                rotate(offset+30,4);
                x = 20; //WAS 15
                sleep(1000);
                driveDistance(0.4, 9.5); //PUSH AND BACK UP
                break;
            case 2:
                x = 25;
                sleep(1000);
                strafeDistance(1, 3.5, true);
                driveDistance(0.4, 9.5); //PUSH AND BACK UP
                break;
            case 3:
                x = 30;
                strafeDistance(0.5, 7, true);
                rotate(offset-30,4);
                sleep(1000);
                driveDistance(0.4, 9.5); //PUSH AND BACK UP
                break;
        }
        sleep(1000);
        driveDistance(-0.3, 4);
        disableDetector();
        if (crater)
            rotate(offset-90, 4);
        else
            rotate(offset+90, 4);
        sleep(1000);
        return x;
    }

    public void unlatch() throws InterruptedException {

        lock.setPosition(1);    //UNLOCK LIFT
        sleep(1000);
        lock.setPosition(0.51); //Stop lock movement
        lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT); //LET GRAVITY TAKE THE ROBOT DOWN
        sleep(1250);        /** We can speed up the auto by powering down the whole time instead of wasting time letting gravity do it **/
        //lock.setPosition(0);    //Stop lock movement
        sleep(750);
        int liftTarget = lift.getCurrentPosition()-250; //FIND HOW FAR THE LIFT NEEDS TO RETRACT : ORIGINALLY 4000
        while (!isStopRequested() && lift.getCurrentPosition() > liftTarget){   //RETRACT LIFT
            lift.setPower(-1);
        }
        lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lift.setPower(0);
        sleep(250);
        //MOVE A BIT TO TRIGGER CAMERA VIEWING
        strafeDistance(0.75, 5, false); //CHANGE to 5 to FIX SAMPLING
        //rotate(45,3);
        sleep(250);
    }

    public void setHook() throws InterruptedException {
        int liftTarget = lift.getCurrentPosition()-4000; //FIND HOW FAR THE LIFT NEEDS TO RETRACT
        while (!isStopRequested() && lift.getCurrentPosition() > liftTarget){   //RETRACT LIFT
            lift.setPower(-1);
        }
    }

    public void markerMove() throws InterruptedException{
        resetTime();
        while (range.getDistance(DistanceUnit.INCH) > 7 && runtime.seconds() < 5 && !isStopRequested() && opModeIsActive()){
            setStrafePowers(1, true);
            telemetry.addData("Inches left", range.getDistance(DistanceUnit.INCH));
            telemetry.update();
        }
    }

    public double getRange(){
        return range.getDistance(DistanceUnit.INCH);
    }

    public void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the Tensor Flow Object Detection engine.
    }

    public void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_GOLD_MINERAL, LABEL_SILVER_MINERAL);
    }

    public void activateDetector(){
        if (tfod != null) {
            tfod.activate();
        }
    }



    @Override
    public void runOpMode() throws InterruptedException {

    }
}