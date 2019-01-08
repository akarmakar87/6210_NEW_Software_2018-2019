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
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
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
    public static final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
    public static final String LABEL_GOLD_MINERAL = "Gold Mineral";
    public static final String LABEL_SILVER_MINERAL = "Silver Mineral";

    public static final String VUFORIA_KEY = "AQt2xVL/////AAABmXIVKUnTcEJbqvVBjp/Sw/9SqarohYyKotzRjT/Xl1/S8KDwsFHv/zYw6rXqXTjKrnjk92GfBA4hbZaQP17d1N6BiBuXO2W/hFNoMGxiF+fWlnvtDmUM1H/MF9faMOjZcPNjnQ7X8DVwdDDha3A3aqaoegefkKxb4A5EjP8Xcb0EPJ1JA4RwhUOutLbCDJNKUq6nCi+cvPqShvlYTvXoROcOGWSIrPxMEiOHemCyuny7tJHUyEg2FTd2upiQygKAeD+LN3P3cT02aK6AJbQ0DlQccxAtoo1+b//H6/eGro2s0fjxA2dH3AaoHB7qkb2K0Vl7ReFEwX7wmqJleamNUG+OZu7K3Zm68mPudzNuhAWQ";
    public VuforiaLocalizer vuforia;
    public TFObjectDetector tfod;

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

            BNO055IMU.Parameters bparameters = new BNO055IMU.Parameters();
            bparameters.mode = BNO055IMU.SensorMode.IMU;
            bparameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
            bparameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
            bparameters.loggingEnabled = false;

            imu.initialize(bparameters);

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
        return getYaw();
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
                        if (goldMineralX < silverMineral1X || goldMineralX < 600) {
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
    }

    public double pushGold(int goldpos, boolean crater) throws InterruptedException {
       // double angleOff = 0;
       // double power = 0.3;
        double x = 0;
        resetTime();
        telemetry.addData("gold is ", goldpos);
        telemetry.update();
        switch (goldpos){
            case 1:
                rotate(0.3, 35, true, 4); //WAS 27
                x = 15;
                sleep(1000);
                driveDistance(-0.4, 9.5); //PUSH AND BACK UP
                sleep(1000);
                driveDistance(0.3, 5);
                disableDetector();
                if (crater)
                    rotate(0.3, 120, false,5);   //ROTATE TOWARD WALL
                else
                    rotate(0.3, 60, true,5);   //ROTATE TOWARD WALL
                break;
            case 2:
                x = 20;
                //strafeDistance(0.5, 3, true);
                sleep(1000);
                driveDistance(-0.4, 9.5); //PUSH AND BACK UP
                sleep(1000);
                driveDistance(0.3, 5.5);
             //   angleOff = getYaw(); //UPDATE ANGLE
                disableDetector();
                //rotate(0.2, 90 - angleOff, false, 5);   //ROTATE TOWARD WALL
                if (crater)
                    rotate(0.3, 90, false,5);   //ROTATE TOWARD WALL
                else
                    rotate(0.3, 90, true, 5);   //ROTATE TOWARD WALL
                break;

            case 3:
                x = 27;
                strafeDistance(0.5, 7, true);
                rotate(0.3, 30, false, 4);
                sleep(1000);
                driveDistance(-0.4, 9.5); //PUSH AND BACK UP
                sleep(1000);
                driveDistance(0.3, 5.5);
               // angleOff = getYaw(); //UPDATE ANGLE
                disableDetector();
                //rotate(0.2, 90-angleOff, true, 5);   //ROTATE TOWARD WALL
                //rotate(0.3, 180, false, 3);
                if (crater)
                    rotate(0.3, 60, false,5);   //ROTATE TOWARD WALL
                else
                    rotate(0.3, 120, true, 5);   //ROTATE TOWARD WALL
                break;
        }
        sleep(1000);
        //IN ORDER TO FIX CHECKALIGN() ISSUE WITH DETECTOR NOT BEING DISABLED, TRY PUTTING A DEACTIVATE COMMAND HERE
        return x;
    }

 /**   public boolean checkAlign(){
        boolean aligned = false;
        activateDetector();
            List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions(); //MAKE LIST OF OBJECTS DETECTED
            if (updatedRecognitions != null) { //IF LIST IS NOT NULL
                telemetry.addData("# of Objects Detected", updatedRecognitions.size()); //GET # OF OBJECTS DETECTED
                if (updatedRecognitions.size() > 0) { //IF DETECT BOTH OBJECTS
                    int goldMineralX = -1;
                    int silverMineral1X = -1;
                    int silverMineral2X = -1;
                    for (Recognition recognition : updatedRecognitions) {
                        if (recognition.getLabel().equals(LABEL_GOLD_MINERAL)) { //IF OBJECT DETECTED IS GOLD
                            goldMineralX = (int) recognition.getLeft();
                        } else if (silverMineral1X == -1) {
                            silverMineral1X = (int) recognition.getLeft();
                        } else {
                            silverMineral2X = (int) recognition.getLeft();
                        }
                    }

                    if (goldMineralX > 500 && goldMineralX < 700) {
                        telemetry.addData("Gold", "Aligned");
                        aligned = true;
                    }else{
                        telemetry.addData("Gold", "Not Aligned");
                        aligned = false;
                    }
                    telemetry.addData("Gold x pos ", goldMineralX);
                }
                telemetry.update();

        }

        telemetry.update();
        return aligned;
    }**/

    public void unlatch() throws InterruptedException {
        lift.setPower(0.5);    //LIFT PULLS ROBOT UP (releases tension for easy unlock)
        lock.setPosition(1);    //UNLOCK LIFT
        lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT); //LET GRAVITY TAKE THE ROBOT DOWN
        sleep(1250);
        lock.setPosition(0);    //Stop lock movement
        sleep(750);
        int liftTarget = lift.getCurrentPosition()-4800; //FIND HOW FAR THE LIFT NEEDS TO RETRACT
        while (!isStopRequested() && lift.getCurrentPosition() > liftTarget){   //RETRACT LIFT
            lift.setPower(-1);
        }
        lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lift.setPower(0);
        sleep(250);
        //MOVE A BIT TO TRIGGER CAMERA VIEWING
        strafeDistance(-0.6, 3, true);
    }

    public void setHook() throws InterruptedException {
        int liftTarget = lift.getCurrentPosition()-4800; //FIND HOW FAR THE LIFT NEEDS TO RETRACT
        while (!isStopRequested() && lift.getCurrentPosition() > liftTarget){   //RETRACT LIFT
            lift.setPower(-1);
        }
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