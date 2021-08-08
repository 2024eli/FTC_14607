package org.firstinspires.ftc.teamcode.opmodes

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.hardware.DriveTrain
import org.firstinspires.ftc.teamcode.util.DataPacket
import org.firstinspires.ftc.teamcode.util.Pose
import org.openftc.revextensions2.ExpansionHubMotor

@TeleOp

class BasicTeleOp : OpMode() {
    private lateinit var driveTrain: DriveTrain
    private lateinit var frontLeft: ExpansionHubMotor
    private lateinit var frontRight: ExpansionHubMotor
    private lateinit var backLeft: ExpansionHubMotor
    private lateinit var backRight: ExpansionHubMotor

    override fun init() {
        frontLeft = hardwareMap.get(ExpansionHubMotor::class.java, "FL")
        frontRight = hardwareMap.get(ExpansionHubMotor::class.java, "FR")
        backLeft = hardwareMap.get(ExpansionHubMotor::class.java, "BL")
        backRight = hardwareMap.get(ExpansionHubMotor::class.java, "BR")
        driveTrain = DriveTrain(frontLeft, frontRight, backLeft, backRight)
    }

    override fun loop() {
        controlGamePad()
    }

    private fun controlGamePad() {
        val x = -gamepad1.left_stick_x * 1.0
        val y = gamepad1.left_stick_y * 1.0
        val h = -gamepad1.right_stick_x * 1.0

        driveTrain.powers = Pose(x, y, h)
        driveTrain.update(DataPacket())
    }
}
