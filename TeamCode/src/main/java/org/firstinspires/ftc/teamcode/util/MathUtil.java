package org.firstinspires.ftc.teamcode.util;

import java.util.ArrayList;

public class MathUtil {
    public static final double EPSILON = 1e-6;
    public static final double tpi = 2 * Math.PI; //imlazi
    public static double angleWrap(double angle) {
        return angle - Math.ceil(angle / tpi - 0.5) * tpi;
    }

    public static double unwrap(double angle) {
        return angle - Math.floor(angle / tpi - 1) * tpi;
    }

    public static boolean epsilonEquals(double d1, double d2) {
        if (Double.isInfinite(d1)) {
            // Infinity - infinity is NaN, so we need a special case
            return d1 == d2;
        } else {
            return Math.abs(d1 - d2) < EPSILON;
        }
    }

    public static boolean angleThresh(double a, double b) {
        return Math.abs(MathUtil.angleWrap(a-b)) < Math.toRadians(0.5);
    }

    public static double minVal(double val, double min){
        if(val >= 0 && val <= min){
            return min;
        }
        if(val < 0 && val > -min){
            return -min;
        }
        return val;
    }

    public static boolean absGreater(double a, double b) {
        return Math.abs(a) > Math.abs(b);
    }


    public static double[] lineEquation(Point p1, double slope) {
        double m;
        double intercept;
        if(Double.isInfinite(slope)) {
            m = Double.NEGATIVE_INFINITY;  // vert line
            intercept = p1.x;
        } else {
            m = slope;
            intercept = p1.y = p1.x * m;
        }

        return new double[] {m, intercept};
    }

    public static Point clipIntersection1(Point start, double slope, Point p) {
        double[] equation = lineEquation(start, slope);
        double[] newEquation = lineEquation(p, -1/slope);

        Point intersect;
        if(Double.isInfinite(equation[0]))
            intersect = new Point(equation[1], equation[1] * newEquation[0] + newEquation[1]);
        else if(Double.isInfinite(newEquation[0]))
            intersect = new Point(newEquation[1], equation[1] * equation[0] + equation[1]);
        else
            intersect = new Point(
                    (newEquation[1] - equation[1]) / (equation[0] - newEquation[0]),
                    ((newEquation[1] - equation[1]) / (equation[0] - newEquation[0])) * equation[0] + equation[1]
            );

        return intersect;
    }

    public static double slop(Point p1, Point p2) {
        return (p2.y-p1.y)/(p2.x-p1.x);
    }
    public static Point clipIntersection2(Point start, Point end, Point robot) {
        if(start.y == end.y)
            start.y = end.y + 0.01;
        if(start.x == end.x)
            end.x = end.x + 0.01;

        double m1 = slop(start,end);
        double m2 = -1.0/m1;
        double xClip = ((-m2*robot.x) + robot.y + (m1 * start.x) - start.y) / (m1 - m2);
        double yClip = (m1 * (xClip - start.x)) + start.y;
        return new Point(xClip, yClip);
    }

    /**
     * Made this because java's math lib returns 1 for a=0
     * @param a input
     * @return sign of input if a != 0, 0 if a==0
     */
    public static int sgn(double a) {
        if(a>0)
            return 1;
        else if(a<0)
            return -1;
        return 0;
    }

    public static Point extendLine(Point firstPoint, Point secondPoint, double distance) {
        double lineAngle = Math.atan2(secondPoint.y - firstPoint.y, secondPoint.x - firstPoint.x);

        double lineLength = Math.hypot(secondPoint.y - firstPoint.y, secondPoint.x - firstPoint.x);
        double extendedLineLength = lineLength + distance;

        Point extended = new Point(secondPoint);
        extended.x = Math.cos(lineAngle) * extendedLineLength + firstPoint.x;
        extended.y = Math.sin(lineAngle) * extendedLineLength + firstPoint.y;
        return extended;
    }
    // change s_tart to a_start
    // change e_nd to a_end
    // change r_obot to c_x and c_y
    /**
     * Returns the closest intersection point to the end of a line segment created through the intersection of a line and circle.
     * The main purpose of this is for pure pursuit but I'll prob implement it into our goToPosition algorithm.
     * For pure pursuit use, c would be the clipped robot point, startPoint would be the current segment start point,
     * endPoint would be the current segment end point, and radius would be our follow distance
     *
     * @param center          center point of circle
     * @param startPoint start point of the line segment
     * @param endPoint   end point of the line segment
     * @param radius     radius of the circle
     * @return intersection point closest to endPoint
     * @see <a href="https://mathworld.wolfram.com/Circle-LineIntersection.html">https://mathworld.wolfram.com/Circle-LineIntersection.html</a>
     */
    public static Point circleLineIntersection(Point center, Point startPoint, Point endPoint, double radius) {
        Point start = new Point(startPoint.x - center.x, startPoint.y - center.y);
        Point end = new Point(endPoint.x - center.x, endPoint.y - center.y);

        double dx = end.x - start.x;
        double dy = end.y - start.y;
        double dr = Math.hypot(dx, dy);
        double D = start.x * end.y - end.x * start.y;
        double discriminant = radius * radius * dr * dr - D * D;

        // discriminant = 0 for 1 intersection, >0 for 2
        ArrayList<Point> intersections = new ArrayList<>();
        double xLeft = D * dy;
        double yLeft = -D * dx;
        double xRight = sgn(dy) * dx * Math.sqrt(discriminant);
        double yRight = Math.abs(dy) * Math.sqrt(discriminant);
        double div = dr * dr;

        if (discriminant == 0) {
            intersections.add(new Point(xLeft / div, yLeft / div));
        } else {
            // add 2 points, one with positive right side and one with negative right side
            intersections.add(new Point(
                    (xLeft + xRight) / div,
                    (yLeft + yRight) / div
            ));

            intersections.add(new Point(
                    (xLeft - xRight) / div,
                    (yLeft - yRight) / div
            ));
        }

        Point closest = new Point(-10000, -10000);
        for (Point p : intersections) { // add circle center offsets
            p.x += center.x;
            p.y += center.y;
            if (p.distance(endPoint) < closest.distance(endPoint))
                closest = p;
        }
        return closest;
    }
}
