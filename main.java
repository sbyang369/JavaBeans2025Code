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
    private DcMotor wormDriveLeft = null;
    private CRServo wrist = null;
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
        wormDriveLeft = hardwareMap.get(DcMotor.class, "wormDriveLeft");
        wrist = hardwareMap.get(CRServo.class, "wrist");
        intake = hardwareMap.get(CRServo.class, "intake");

        wrist.setPower(0.0);
        
        // Reverse motor direction
        bottomL.setDirection(DcMotor.Direction.REVERSE);
        topL.setDirection(DcMotor.Direction.REVERSE);
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

        double wristPower = 0.0;
        double intakePower = 0.0;

        double linearSlideStartTime = 0;
        boolean isLinearSlideMoving = false;
        linearSlideRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        while (opModeIsActive()) {
            // Normal movement and other functions
            double y = -gamepad1.left_stick_y;
            double x = gamepad1.left_stick_x * 1.1; // Adjust for strafing
            double rx = gamepad1.right_stick_x;
            double maxPower = 0.5;
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

            // ------ Manual Linear Control (Dpad) Works Fine ------
            if (gamepad2.dpad_left) {
                linearSlideRight.setPower(-0.5);
            } else if (gamepad2.dpad_right) {
                linearSlideRight.setPower(0.5);
            } else {  
                linearSlideRight.setPower(0);
            }

            // Wrist and Intake Control (unchanged)
            wrist.setPower(gamepad2.left_trigger > 0.1 ? -0.2 : gamepad2.left_bumper ? 0.2 : 0.0);
            intake.setPower(gamepad2.right_trigger > 0.1 ? 0.5 : gamepad2.right_bumper ? -0.2 : 0.0);


            // Telemetry
            telemetry.addData("Wrist Power", wristPower);
            telemetry.addData("Intake Power", intakePower);
            telemetry.update();
        }
    }
}
