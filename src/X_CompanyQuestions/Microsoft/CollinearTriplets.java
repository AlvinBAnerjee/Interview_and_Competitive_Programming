package X_CompanyQuestions.Microsoft;

import java.util.*;

public class CollinearTriplets {

    public static void main(String[] args) {
        Point2d A[] = new Point2d[6];
        A[0] = new Point2d(0, 0);
        A[1] = new Point2d(1, 1);
        A[2] = new Point2d(2, 2);

        A[3] = new Point2d(1, 0);
        A[4] = new Point2d(2, 1);
        A[5] = new Point2d(3, 2);

        System.out.println(countCollinear(A));
    }


    public static int factorial(int n) {
        int fact = 1;
        for (int i = 1; i <= n; i++) {
            fact = fact * i;
        }
        return fact;
    }

    private static int combinations(int n, int r) {
        return factorial(n) / (factorial(n - r) * factorial(r));
    }

    private static long countCollinear(Point2d[] points) {
        Map<Line, Set<Point2d>> lineToPoints = new HashMap<>();
        long result = 0;

        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                double slope = 0d, xIntercept, yIntercept; // Default slope paralell to y-axis
                if (points[i].x == points[j].x) {
                    slope = Double.MAX_VALUE; // Horizontal slope parallel to x-axis
                } else if (points[i].y != points[j].y) {
                    xIntercept = points[j].x - points[i].x;
                    yIntercept = points[j].y - points[i].y;
                    slope = yIntercept / xIntercept;
                }
                Line currLine = new Line(points[i], slope);
                if (Objects.isNull(lineToPoints.get(currLine))) {
                    lineToPoints.put(currLine, new HashSet<>());
                }
                lineToPoints.get(currLine).add(points[i]);
                lineToPoints.get(currLine).add(points[j]);
            }

        }
        for (Line line : lineToPoints.keySet()) {
            int size = lineToPoints.get(line).size();
            if (size >= 3) {
                result = result + combinations(size, 3);
            }
        }
        return result;
    }

    /**
     * Line which contains the starting point and slope so that you can identify exact line
     * equals method is overridden to check whether any new line is coinciding or parallel
     */
    static class Line {
        Point2d point;
        double slope;

        public Line(Point2d point, double slope) {
            this.point = point;
            this.slope = slope;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Line)) return false;
            Line line = (Line) o;

            if (line.slope == this.slope)
                return ((((double) (line.point.y - this.point.y)) / (line.point.x - this.point.x)) == this.slope);
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(slope);
        }
    }

    static class Point2d {
        int x;
        int y;

        public Point2d(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Point2d)) return false;
            Point2d point2d = (Point2d) o;
            return x == point2d.x &&
                    y == point2d.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }
}
