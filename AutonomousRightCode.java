//WORKING! USE ON MATCH DAY!
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.Blinker;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name = "AUTO_BlueRight.java")
public class AUTO_BlueRight extends LinearOpMode {

    private Blinker control_Hub;
    private DcMotor bottomL = null;
    private DcMotor bottomR = null;
    private DcMotor topL = null;
    private DcMotor topR = null;
    private DcMotor linearSlideRight = null; 
    private DcMotor wormDriveRight = null;
    private DcMotor linearSlideLeft = null; 
    private DcMotor wormDriveLeft = null;
    private CRServo wristLeft = null;
    private CRServo wristRight = null; 
    private CRServo intake = null;

    @Override
    public void runOpMode() {
        // Initialize the hardware variables
        topL = hardwareMap.get(DcMotor.class, "topL");
        topR = hardwareMap.get(DcMotor.class, "topR");
        bottomL = hardwareMap.get(DcMotor.class, "bottomL");
        bottomR = hardwareMap.get(DcMotor.class, "bottomR");
        linearSlideRight = hardwareMap.get(DcMotor.class, "linearSlideRight");
        wormDriveRight = hardwareMap.get(DcMotor.class, "wormDriveRight");
        linearSlideLeft = hardwareMap.get(DcMotor.class, "linearSlideLeft");
        wormDriveLeft = hardwareMap.get(DcMotor.class, "wormDriveLeft");
        wristLeft = hardwareMap.get(CRServo.class, "wristLeft");
        wristRight = hardwareMap.get(CRServo.class, "wristRight");
        intake = hardwareMap.get(CRServo.class, "intake");

        // Reverse motor direction
        bottomL.setDirection(DcMotor.Direction.REVERSE);
        topL.setDirection(DcMotor.Direction.REVERSE);
        topR.setDirection(DcMotor.Direction.REVERSE); 
        linearSlideRight.setDirection(DcMotor.Direction.REVERSE);
        wristRight.setDirection(DcMotor.Direction.REVERSE);

        // Set all motors to use encoders
        topL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        topR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        bottomL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        bottomR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        topL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        topR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        bottomL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        bottomR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // REQUIREMENTS: 
       // 1) the wormdrive and the wrist are already up (does the wrist flop?)
       // 2) a pixel is preloaded
       // 3) robot is EXACTLY two tiles away from fence 

        if (opModeIsActive()) {
            //make sure it works - can it reach high basket??
            sleep(15000); // Waits for 10,000 milliseconds (10 seconds)
          
            // Drive to high basket 
            driveForward(3,0.5);
            turnLeft(160,0.5);
            driveForward(70,0.5);
            turnLeft(75,0.5);
            driveForward(10,0.5);
            
            //Arm controls
            linearSlideRight.setPower(0.5); //extend arm
            linearSlideLeft.setPower(0.5);
            sleep(1200);
            linearSlideRight.setPower(0);
            linearSlideLeft.setPower(0);
            //----intake scoring stuff (wrist down, spin intake, wrist up)
            wrist.setPower(0.5);
            sleep(500);
            wrist.setPower(0);
          
            wrist.setPower(-0.5);
            sleep(500);
            wrist.setPower(0);
          
            intake.setPower(-0.5);
            sleep(500);
            intake.setPower(0);

            wrist.setPower(0.5);
            sleep(500);
            wrist.setPower(0);
            
            linearSlideRight.setPower(-0.5); //deextend arm
            linearSlideLeft.setPower(-0.5);
            sleep(800);
            linearSlideRight.setPower(0);
            linearSlideLeft.setPower(0);
            
            //--------Park in ascent
            driveForward(-10,0.5);
            turnRight(250,0.5);
            driveForward(40,0.5);
            turnRight(170,0.5);
            driveForward(35,0.5);
        }
    }

    // Method to drive forward or backward a given distance (in inches)
    public void driveForward(double inches, double power) {
        int ticks = inchesToTicks(inches);

        topL.setTargetPosition(topL.getCurrentPosition() + ticks);
        topR.setTargetPosition(topR.getCurrentPosition() + ticks);
        bottomL.setTargetPosition(bottomL.getCurrentPosition() + ticks);
        bottomR.setTargetPosition(bottomR.getCurrentPosition() + ticks);

        topL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        topR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        bottomL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        bottomR.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        topL.setPower(power);
        topR.setPower(power);
        bottomL.setPower(power);
        bottomR.setPower(power);

        while (opModeIsActive() && topL.isBusy() && topR.isBusy() && bottomL.isBusy() && bottomR.isBusy()) {
            telemetry.addData("Path", "Driving Forward");
            telemetry.update();
        }

        stopMotors();
    }

    // Method to turn left a given angle (in degrees)
    public void turnLeft(double degrees, double power) {
        int ticks = degreesToTicks(degrees);

        topL.setTargetPosition(topL.getCurrentPosition() - ticks);
        topR.setTargetPosition(topR.getCurrentPosition() + ticks);
        bottomL.setTargetPosition(bottomL.getCurrentPosition() - ticks);
        bottomR.setTargetPosition(bottomR.getCurrentPosition() + ticks);

        topL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        topR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        bottomL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        bottomR.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        topL.setPower(power);
        topR.setPower(power);
        bottomL.setPower(power);
        bottomR.setPower(power);

        while (opModeIsActive() && topL.isBusy() && topR.isBusy() && bottomL.isBusy() && bottomR.isBusy()) {
            telemetry.addData("Path", "Turning Left");
            telemetry.update();
        }

        stopMotors();
    }

    // Method to turn right a given angle (in degrees)
    public void turnRight(double degrees, double power) {
        int ticks = degreesToTicks(degrees);

        topL.setTargetPosition(topL.getCurrentPosition() + ticks);
        topR.setTargetPosition(topR.getCurrentPosition() - ticks);
        bottomL.setTargetPosition(bottomL.getCurrentPosition() + ticks);
        bottomR.setTargetPosition(bottomR.getCurrentPosition() - ticks);

        topL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        topR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        bottomL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        bottomR.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        topL.setPower(power);
        topR.setPower(power);
        bottomL.setPower(power);
        bottomR.setPower(power);

        while (opModeIsActive() && topL.isBusy() && topR.isBusy() && bottomL.isBusy() && bottomR.isBusy()) {
            telemetry.addData("Path", "Turning Right");
            telemetry.update();
        }

        stopMotors();
    }

    // Method to stop all motors
    private void stopMotors() {
        topL.setPower(0);
        topR.setPower(0);
        bottomL.setPower(0);
        bottomR.setPower(0);

        topL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        topR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        bottomL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        bottomR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    // Helper method to convert inches to encoder ticks
    private int inchesToTicks(double inches) {
        final double TICKS_PER_REV = 537.6;  // Example: GoBILDA 5202 motor
        final double WHEEL_DIAMETER = 4.0;  // In inches
        final double CIRCUMFERENCE = Math.PI * WHEEL_DIAMETER;
        return (int) ((inches / CIRCUMFERENCE) * TICKS_PER_REV);
    }

    // Helper method to convert degrees to encoder ticks
    private int degreesToTicks(double degrees) {
        final double ROBOT_DIAMETER = 18.0;  // Example robot diameter in inches
        final double ROBOT_CIRCUMFERENCE = Math.PI * ROBOT_DIAMETER;
        double distance = (degrees / 360.0) * ROBOT_CIRCUMFERENCE;
        return inchesToTicks(distance);
    }
}
