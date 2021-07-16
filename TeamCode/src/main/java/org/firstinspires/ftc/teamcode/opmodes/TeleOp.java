package org.firstinspires.ftc.teamcode.opmodes;

import org.firstinspires.ftc.teamcode.control.system.Robot;
import org.firstinspires.ftc.teamcode.util.AllianceSide;
import org.firstinspires.ftc.teamcode.util.Pose;

public class TeleOp extends Robot {
    @Override
    public Pose startPose() {
        return new Pose(0,0,0);
    }

    @Override
    public AllianceSide allianceSide() {
        return AllianceSide.BLUE;
    }

    @Override
    public void loop() {
        super.loop();
        controlGamepad();
    }

    private void controlGamepad() {
        if(pathCache.isEmpty()) {
            double driveScale = 0.5 - (gamepad1.left_bumper ? 0.2 : 0);
            driveTrain.powers.set(new Pose(
                    -gamepad1.left_stick_y * driveScale * 0.75,
                    gamepad1.left_stick_x * driveScale,
                    -gamepad1.right_stick_x * driveScale
            ));
        }
    }
}