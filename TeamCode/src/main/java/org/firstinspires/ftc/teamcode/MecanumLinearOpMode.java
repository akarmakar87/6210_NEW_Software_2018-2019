package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
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
    public BNO055IMU imu;
    public DcMotor lift;
    public Servo marker;
    public Servo lock;

    //gyro variables
    Orientation angles;

    static final double     COUNTS_PER_MOTOR_REV    = 1120 ;    // REV Motor Encoder
    static final double     DRIVE_GEAR_REDUCTION    = 1.0 ;     // This is < 1.0 if geared UP
    static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference

    public double encoderToInches = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION)/(WHEEL_DIAMETER_INCHES * Math.PI); //Multiply desired distance (inches)

    //Camera
    private static final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
    private static final String LABEL_GOLD_MINERAL = "Gold Mineral";
    private static final String LABEL_SILVER_MINERAL = "Silver Mineral";

    private static final String VUFORIA_KEY = "AQt2xVL/////AAABmXIVKUnTcEJbqvVBjp/Sw/9SqarohYyKotzRjT/Xl1/S8KDwsFHv/zYw6rXqXTjKrnjk92GfBA4hbZaQP17d1N6BiBuXO2W/hFNoMGxiF+fWlnvtDmUM1H/MF9faMOjZcPNjnQ7X8DVwdDDha3A3aqaoegefkKxb4A5EjP8Xcb0EPJ1JA4RwhUOutLbCDJNKUq6nCi+cvPqShvlYTvXoROcOGWSIrPxMEiOHemCyuny7tJHUyEg2FTd2upiQygKAeD+LN3P3cT02aK6AJbQ0DlQccxAtoo1+b//H6/eGro2s0fjxA2dH3AaoHB7qkb2K0Vl7ReFEwX7wmqJleamNUG+OZu7K3Zm68mPudzNuhAWQ";
    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;

    // INITIALIZE
    public double init(HardwareMap map, boolean auto){

        runtime     = new ElapsedTime();
        LF  = map.dcMotor.get("LF");
        RF  = map.dcMotor.get("RF");
        LB  = map.dcMotor.get("LB");
        RB  = map.dcMotor.get("RB");
        marker = map.servo.get("marker");
        imu            = map.get(BNO055IMU.class, "imu"); // Check which IMU is being used
        lift  = map.dcMotor.get("lift");
        lock  = map.servo.get("lock");

        LF.setDirection(DcMotorSimple.Direction.REVERSE);
        RF.setDirection(DcMotorSimple.Direction.FORWARD);
        RB.setDirection(DcMotorSimple.Direction.FORWARD);
        LB.setDirection(DcMotorSimple.Direction.REVERSE);


        LF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        RF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        LB.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        RB.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        resetEncoders();

        //SET UP GYRO
        if (auto) {
            angles = new Orientation();

            BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
            parameters.mode = BNO055IMU.SensorMode.IMU;
            parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
            parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
            parameters.loggingEnabled = false;

            imu.initialize(parameters);

            telemetry.addData("Mode", "calibrating...");
            telemetry.update();

            while (!isStopRequested() && !imu.isGyroCalibrated()) {
                sleep(50);
                idle();
            }

            telemetry.addData("imu calib status", imu.getCalibrationStatus().toString());
            telemetry.update();

            // Set up detector

            telemetry.addData("Mode", "setting up detector...");
            telemetry.update();

            telemetry.addData("detector", "enabled");
            telemetry.update();
        }
        telemetry.addData("Status: ", "Initialized");
        telemetry.update();
        return getYaw();
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
        sleep(seconds);
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
                LF.setPower(-power);
                RF.setPower(power);
                LB.setPower(power);
                RB.setPower(-power);
            }else {
                LF.setPower(power);
                RF.setPower(-power);
                LB.setPower(-power);
                RB.setPower(power);
            }
        }
        stopMotors();
    }


    //UPDATE ANGLE
    public void updateValues() {
        angles = imu.getAngularOrientation();
    }

    //GET ANGLE
    public double getYaw() {
        updateValues();
        return angles.firstAngle;
    }

    //ROTATE USING GYRO
    public void rotateP(double targetAngleChange, boolean turnRight, int timeout) {

        runtime.reset();

        double initAngle = getYaw();
        telemetry.addData("Initial Angle", initAngle);
        telemetry.update();

        double currAngleChange = getYaw() - initAngle;
        telemetry.addData("CurrAngleChange", currAngleChange);
        telemetry.update();

        while ((Math.abs(getYaw() - initAngle) < targetAngleChange) && opModeIsActive() && (runtime.seconds() < timeout)) {
            currAngleChange = getYaw() - initAngle;
            double kP = .5/90;
            double power = .1 + currAngleChange * kP;
            if (turnRight){
                setMotorPowers(power,-power);
            }else {
                setMotorPowers(-power, power);
            }

            telemetry.addData("Angle left", targetAngleChange - currAngleChange);
            telemetry.update();

        }
        stopMotors();
    }


    //ROTATE USING GYRO
    public void rotate(double power, double targetAngleChange, boolean turnRight, int timeout) {

        runtime.reset();

        targetAngleChange -= 5;

        double initAngle = getYaw();
        telemetry.addData("Initial Angle", initAngle);
        telemetry.update();

        double currAngleChange = getYaw() - initAngle;
        telemetry.addData("CurrAngleChange", currAngleChange);
        telemetry.update();

        while ((Math.abs(getYaw() - initAngle) < targetAngleChange) && opModeIsActive() && (runtime.seconds() < timeout)) {
            if (turnRight){
                setMotorPowers(power,-power);
            }else {
                setMotorPowers(-power, power);
            }

            telemetry.addData("Angle left", targetAngleChange - currAngleChange);
            telemetry.update();

        }
        stopMotors();
    }

    //GOLD SAMPLING

    public int findGold(int timeLimit){

        runtime.reset();

        int pos = 2;

        while (runtime.seconds() < timeLimit){
            if (tfod != null) {
                // getUpdatedRecognitions() will return null if no new information is available since
                // the last time that call was made.
                List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions(); //MAKE LIST OF OBJECTS DETECTED
                if (updatedRecognitions != null) { //IF LIST IS NOT NULL
                    int goldMineralX = -1;
                    telemetry.addData("# of Objects Detected", updatedRecognitions.size()); //GET # OF OBJECTS DETECTED
                    if (updatedRecognitions.size() > 0) { //IF DETECT ALL THREE OBJECTS
                        for (Recognition recognition : updatedRecognitions) {
                            if (recognition.getLabel().equals(LABEL_GOLD_MINERAL) && recognition.getTop() < 500) {
                            //IF OBJECT DETECTED IS GOLD AND ALSO RESTRICT HEIGHT TO AVOID CRATER GOLD CONFUSION
                                goldMineralX = (int) recognition.getLeft(); //GET X POSITION FROM LEFT (?)
                            }
                            telemetry.addData("Gold Mineral Position", goldMineralX);
                        }
                    }

                    if (goldMineralX < 300){
                        pos = 1;
                    }else if (goldMineralX > 300){
                        pos = 2;
                    }else{
                        pos = 3;
                    }
                }else{
                    telemetry.addData("Nothing", "Detected");
                }
            }else{
                telemetry.addData("No", "TFOD");
            }
            telemetry.update();
        }
        return pos;
    }


    /*public double getXpos(){
        return detector.getXPosition();
    }*/

    public void resetTime(){
        runtime.reset();
    }

    public double getTime(){
        return runtime.seconds();
    }

   /* public void disableDetector(){
        detector.disable();
    }*/

    public int checkAlign(){
        int align = 0;
        if (tfod != null) {
            // getUpdatedRecognitions() will return null if no new information is available since
            // the last time that call was made.
            List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions(); //MAKE LIST OF OBJECTS DETECTED
            if (updatedRecognitions != null) { //IF LIST IS NOT NULL
                int goldMineralX = -1;
                telemetry.addData("# of Objects Detected", updatedRecognitions.size()); //GET # OF OBJECTS DETECTED
                if (updatedRecognitions.size() > 0) { //IF DETECT ALL THREE OBJECTS
                    for (Recognition recognition : updatedRecognitions) {
                        if (recognition.getLabel().equals(LABEL_GOLD_MINERAL) && recognition.getTop() < 500) {
                            //IF OBJECT DETECTED IS GOLD AND ALSO RESTRICT HEIGHT TO AVOID CRATER GOLD CONFUSION
                            goldMineralX = (int) recognition.getLeft(); //GET X POSITION FROM LEFT (?)
                        }
                        telemetry.addData("Gold Mineral Position", goldMineralX);
                    }
                }

                if (goldMineralX < 300){
                    align = -1;
                }else if (goldMineralX > 300 && goldMineralX < 500){
                    align = 0;
                }else{
                    align = 1;
                }
            }else{
                telemetry.addData("Nothing", "Detected");
            }
        }else{
            telemetry.addData("No", "TFOD");
        }
        telemetry.update();
        return align;
    }

    public void unlatch(){
        lock.setPosition(1);
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