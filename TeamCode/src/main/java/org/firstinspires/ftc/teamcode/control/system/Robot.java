package org.firstinspires.ftc.teamcode.control.system;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.BNO055IMUImpl;

import net.frogbots.ftcopmodetunercommon.opmode.TunableOpMode;

import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.teamcode.control.localization.WorkingOdometry;
import org.firstinspires.ftc.teamcode.control.path.Path;
import org.firstinspires.ftc.teamcode.control.path.PathPoints;
import org.firstinspires.ftc.teamcode.util.AllianceSide;
import org.firstinspires.ftc.teamcode.util.AxesSigns;
import org.firstinspires.ftc.teamcode.util.BNO055IMUUtil;
import org.firstinspires.ftc.teamcode.util.MathUtil;
import org.firstinspires.ftc.teamcode.util.OpModeClock;
import org.firstinspires.ftc.teamcode.util.Pose;
import org.openftc.revextensions2.ExpansionHubEx;
import org.openftc.revextensions2.ExpansionHubMotor;
import org.openftc.revextensions2.RevBulkData;

import java.util.LinkedList;

@Config
public abstract class Robot extends TunableOpMode {

    public abstract Pose startPose();
    public abstract AllianceSide allianceSide();

    public Pose currPose;
    public Pose currVel;
    public Pose currDrivePowers;

    public Path pathCache;
    public LinkedList<PathPoints.BasePathPoint> fullPathCopy;

    public RevBulkData masterBulkData;
    public RevBulkData slaveBulkData;
    public WorkingOdometry odometry;

    private FtcDashboard dashboard;
    public TelemetryPacket packet;
    public long updateMarker;

    public ExpansionHubEx masterHub;
    public ExpansionHubEx slaveHub;

    public ExpansionHubMotor frontLeft;
    public ExpansionHubMotor frontRight;
    public ExpansionHubMotor backLeft;
    public ExpansionHubMotor backRight;

    private BNO055IMU imu;
    private double headingOffset;

    @Override
    public void init() {
        OpModeClock.markInit();

        masterHub = hardwareMap.get(ExpansionHubEx.class, "masterHub");
        slaveHub = hardwareMap.get(ExpansionHubEx.class, "slaveHub");

        frontLeft = hardwareMap.get(ExpansionHubMotor.class, "frontLeft");
        frontRight = hardwareMap.get(ExpansionHubMotor.class, "frontRight");
        backLeft = hardwareMap.get(ExpansionHubMotor.class, "backLeft");
        backRight = hardwareMap.get(ExpansionHubMotor.class, "backRight");

        imu = hardwareMap.get(BNO055IMUImpl.class, "imu");
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.RADIANS;
        parameters.loggingEnabled  = false;
        imu.initialize(parameters);
        BNO055IMUUtil.remapAxes(imu, AxesOrder.XYZ, AxesSigns.NPN);
        headingOffset = imu.getAngularOrientation().firstAngle;


        odometry = new WorkingOdometry(hardwareMap, startPose());
        currPose = new Pose(startPose());
        currVel = new Pose();

        dashboard = FtcDashboard.getInstance();
        packet = null;
        updateMarker = OpModeClock.getElapsedInitTime();
    }

    @Override
    public void init_loop() {

    }

    @Override
    public void start() {
        OpModeClock.markStart();
    }

    @Override
    public void loop() {
        updateDataInputComponents();
        updateTelemetry();
    }

    public void updateDataInputComponents() {
        masterBulkData = masterHub.getBulkInputData();
        slaveBulkData = slaveHub.getBulkInputData();
        double lastHeading = imu.getAngularOrientation().firstAngle - headingOffset;
        odometry.realUpdate(MathUtil.angleWrap(lastHeading + startPose().heading));
        telemetry.addLine("" + odometry);

    }

    public void updateTelemetry() {
        telemetry.clear();
        packet = new TelemetryPacket();
        packet.put("pose", currPose.toString());
        packet.put("movement", currDrivePowers.toString());
        packet.put("velocity", currVel.toString());

        double[] x = new double[fullPathCopy.size()];
        double[] y = new double[fullPathCopy.size()];

        int index = 0;
        for(PathPoints.BasePathPoint p : fullPathCopy) {
            x[index] = p.x;
            y[index] = p.y;
            index++;
        }

        packet.fieldOverlay()
                .setFill("blue")
                .fillCircle(currPose.x, currPose.y, 3)
                .setStroke("red")
                .setStrokeWidth(1)
                .strokePolygon(x, y);

        if(dashboard != null) {
            packet.put("update time", System.nanoTime() - updateMarker);
            dashboard.sendTelemetryPacket(packet);
            updateMarker = System.nanoTime();
        }
    }
}





















