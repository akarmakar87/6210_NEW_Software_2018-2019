package org.firstinspires.ftc.teamcode.Emergency_Failure_Autos;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;


import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.teamcode.MecanumLinearOpMode;
import org.firstinspires.ftc.teamcode.OldMecanumLinearOpMode;

@Autonomous(name="CraterGoldEnemyBlock", group = "em")
//@Disabled
public class CraterGoldOpposingBlock extends OldMecanumLinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        double offset = 0;
        init(hardwareMap, true);

        // Set up detector
        initVuforia();
        if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
            initTfod();
        } else {
            telemetry.addData("Sorry!", "This device is not compatible with TFOD");
        }

        telemetry.addData("Mode", "setting up detector...");
        telemetry.update();

        telemetry.addData("detector", "enabled");
        telemetry.update();

        double dist = 0;
        waitForStart();
        unlatch();

        //START DETECTION

        findGold(2); //GET GOLD POSITION
        tfod.deactivate();
        int gold = retPos();
        sleep(1000);
        telemetry.addData("Gold is at", gold);
        telemetry.update();
        driveDistance(-0.3,4); //MOVE FORWARD OUT OF LANDER ZONE

        dist = pushGold(gold,true);

        driveDistance(1, 30);
        rotate(1, 10, true, 2);
        driveTime(1,1);
        strafeDistance(1, 10, true);
    }}