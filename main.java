package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Blinker;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.CRServo; 

@TeleOp(name = "MeetOne2025")
public class MeetOne2025 extends LinearOpMode { 
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
        // Initialize hardware
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
        intake = hardwareMap.get(CRServo.class, "intake"); // Single intake servo
        wristLeft.setPower(0.0);
        wristRight.setPower(0.0);
        wristLeft.resetDeviceConfigurationForOpMode();
        wristRight.resetDeviceConfigurationForOpMode();

 

        // Reverse motor direction(!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!MAY HAVE AN ISSUE!!! CHECK THEM!!!!
        bottomL.setDirection(DcMotor.Direction.REVERSE);
        topL.setDirection(DcMotor.Direction.REVERSE);
        topR.setDirection(DcMotor.Direction.REVERSE); 
        linearSlideRight.setDirection(DcMotor.Direction.REVERSE);
        wristRight.setDirection(DcMotor.Direction.REVERSE);

        telemetry.addData("Status", "Initialized");
        telemetry.addData(">", "ROBOT IS READY. HURRY AND START.");
        telemetry.update(); 

        waitForStart();
        
        double wristPower = 0.0; // Initialize wrist power
        double intakePower = 0.0; // Initialize intake power
 

        while (opModeIsActive()) { 
            // ----- Movement Control ----- 
            double y = -gamepad1.left_stick_y;
            double x = gamepad1.left_stick_x * 1.1; // Adjust for strafing
            double rx = gamepad1.right_stick_x;
            double maxPower = 0.6;
            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            double frontLeftPower = (y + x + rx) / denominator * maxPower;
            double backLeftPower = (y - x + rx) / denominator * maxPower;
            double frontRightPower = (y - x - rx) / denominator * maxPower;
            double backRightPower = (y + x - rx) / denominator * maxPower;

            topL.setPower(frontLeftPower);
            bottomL.setPower(backLeftPower);
            topR.setPower(frontRightPower);
            bottomR.setPower(backRightPower); 
// ---------- Wormdrive
            if (gamepad2.dpad_up) {
                wormDriveRight.setPower(0.8); // Move up
                wormDriveLeft.setPower(0.8);
            } else if (gamepad2.dpad_down) {
                wormDriveRight.setPower(-0.8); // Move down
                wormDriveLeft.setPower(-0.8);
            } else {
                wormDriveRight.setPower(0); // Stop if out of bounds
                wormDriveLeft.setPower(0);
            }

// ---------- Linear SMALL 
            if (gamepad2.dpad_left) {
                linearSlideRight.setPower(-0.5);
                linearSlideLeft.setPower(-0.5);
            } else if (gamepad2.dpad_right) {
                linearSlideRight.setPower(0.5);
                linearSlideLeft.setPower(0.5);
            } else {
                linearSlideRight.setPower(0);
                linearSlideLeft.setPower(0);
            }
            
// ---------- Linear SHOOT

            if (gamepad2.y) {
                linearSlideLeft.setPower(-0.5);
                linearSlideRight.setPower(-0.5);
                sleep(800);
                linearSlideLeft.setPower(0);
                linearSlideRight.setPower(0);
                sleep(800);
            } else if (gamepad2.a) {
                linearSlideLeft.setPower(0.5);
                linearSlideRight.setPower(0.5);
                sleep(800);
                linearSlideLeft.setPower(0);
                linearSlideRight.setPower(0);
                sleep(800);
            } 
            // ----- Wrist Control (Left Side) -----

            if (gamepad2.left_trigger > 0.1) {
                wristLeft.setPower(0.5); // Rotate wrist left forward
                wristRight.setPower(0.5); // Rotate wrist right forward
            } else if (gamepad2.left_bumper) {
                wristLeft.setPower(-0.5); // Rotate wrist left backward
                wristRight.setPower(-0.5); // Rotate wrist right backward
            } else {
                wristLeft.setPower(0.0); // Stop wrist left
                wristRight.setPower(0.0); // Stop wrist right
            }

            wristLeft.setPower(wristPower);
            wristRight.setPower(wristPower);


            // ----- Intake Control (Right Side Controls) -----
            if (gamepad2.right_trigger > 0.1) {
                intakePower = 0.5; // Spin intake forward
            } else if (gamepad2.right_bumper) {
                intakePower = -0.5; // Spin intake backward
            } else {
                intakePower = 0.0; // Stop intake
            }

            intake.setPower(intakePower);

            // Telemetry for debugging
            telemetry.addData("Wrist Power", wristPower);
            telemetry.addData("Intake Power", intakePower);
            telemetry.addData("Front Left Power", frontLeftPower);
            telemetry.addData("Front Right Power", frontRightPower);
            telemetry.addData("Back Left Power", backLeftPower);
            telemetry.addData("Back Right Power", backRightPower);
            telemetry.update();
        }
    }
}
