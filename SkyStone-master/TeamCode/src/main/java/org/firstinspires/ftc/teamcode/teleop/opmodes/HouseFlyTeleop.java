package org.firstinspires.ftc.teamcode.teleop.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;


@TeleOp(name = "basiceleopdrve")
public class HouseFlyTeleop extends OpMode {

    private DcMotor leftFront;
    private DcMotor rightFront;
    private DcMotor leftRear;
    private DcMotor rightRear;
    private DcMotor leftIntake;
    private DcMotor rightIntake;
    private DcMotor leftSlide;
    private DcMotor rightSlide;
    //private Servo flipper, gripper, outtake, leftSlam, rightSlam;



    // TODO: EDIT THIS SUTFF
    private double flipperFlipPosition = 1;
    private double flipperReadyPosition = 0;
    private double gripperGripPosition = 1;
    private double gripperReadyPosition = 0.5;
    private double outtakeOutPosition = 0;
    private double outtakeReadyPosition = 1;




    @Override
    public void init() {

        leftFront = hardwareMap.get(DcMotor.class, "FL");
        leftRear = hardwareMap.get(DcMotor.class, "BL");
        rightRear = hardwareMap.get(DcMotor.class, "FR");
        rightFront = hardwareMap.get(DcMotor.class, "BR");
        leftIntake = hardwareMap.get(DcMotor.class, "leftIntake");
        rightIntake = hardwareMap.get(DcMotor.class, "rightIntake");
        leftSlide = hardwareMap.get(DcMotor.class, "leftSlide");
        rightSlide = hardwareMap.get(DcMotor.class, "rightSlide");

  /*      gripper = hardwareMap.get(Servo.class, "gripper");
        flipper = hardwareMap.get(Servo.class, "flipper");
        outtake = hardwareMap.get(Servo.class, "outtake");
        leftSlam = hardwareMap.get(Servo.class, "leftSlam");
        rightSlam = hardwareMap.get(Servo.class, "rightSlam");
*/


        rightFront.setDirection(DcMotor.Direction.REVERSE);
        rightRear.setDirection(DcMotor.Direction.REVERSE);
        rightIntake.setDirection(DcMotor.Direction.REVERSE);
        leftSlide.setDirection(DcMotor.Direction.REVERSE);
        telemetry.addData("Status", "Initialized");
        telemetry.update();
    }

    // run until the end of the match (driver presses STOP)
    public void loop() {
        //Drive motor control


        if(gamepad2.left_bumper) {
            rightIntake.setPower(-1);
            leftIntake.setPower(-1);
        }

        else if(gamepad2.right_bumper) {
            rightIntake.setPower(1);
            leftIntake.setPower(-1);
        }

        else {
            rightIntake.setPower(0);
            leftIntake.setPower(0);
        }

        leftSlide.setPower(-gamepad2.right_stick_y);
        rightSlide.setPower(-gamepad2.right_stick_y);


        double motorPower;
        if(gamepad1.left_bumper) {
            motorPower = 0.5;
        }

        else if(gamepad1.right_bumper) {
            motorPower = 0.25;
        }

        else {
            motorPower = 1;
        }



        /*
        drive motor powers
         */
        double threshold = 0.157; // deadzone
        if(Math.abs(gamepad1.left_stick_y) > threshold || Math.abs(gamepad1.left_stick_x) > threshold || Math.abs(gamepad1.right_stick_x) > threshold)
        {
            rightFront.setPower(motorPower * (((-gamepad1.left_stick_y) + (gamepad1.left_stick_x)) + -gamepad1.right_stick_x));
            leftRear.setPower(motorPower * (((-gamepad1.left_stick_y) + (-gamepad1.left_stick_x)) + gamepad1.right_stick_x));
            leftFront.setPower(motorPower * (((-gamepad1.left_stick_y) + (gamepad1.left_stick_x)) + gamepad1.right_stick_x));
            rightRear.setPower(motorPower * (((-gamepad1.left_stick_y) + (-gamepad1.left_stick_x)) + -gamepad1.right_stick_x));
        }

        else
        {
            leftFront.setPower(0);
            rightFront.setPower(0);
            leftRear.setPower(0);
            rightRear.setPower(0);
        }

    }
}
