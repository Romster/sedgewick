import java.util.Arrays;

/**
 * Created by romster on 24.09.15.
 */
public class FastCollinearPoints {

    private LineSegment[] segments;
    private double[] slopes;
    private Point[] lastPoints;
    private int segmentsCount;

    /**
     * finds all line segments containing 4 or more points
     *
     * @param points
     */
    public FastCollinearPoints(Point[] points) {
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new NullPointerException();
            }
            for (int j = i + 1; j < points.length; j++) {
                Point a = points[i];
                Point b = points[j];
                if (same(a, b)) {
                    throw new IllegalArgumentException("Same points founded");
                }
            }
        }
        segmentsCount = 0;
        if (points.length < 4) {
            segments = new LineSegment[0];
            return;
        }
        Point[] inputData = Arrays.copyOf(points, points.length);
        Arrays.sort(inputData);
        segments = new LineSegment[inputData.length];
        lastPoints = new Point[inputData.length];
        slopes = new double[inputData.length];
        for (int i = 0; i < inputData.length-3; i++) {
            Point p1 = inputData[i];
            Point[] ps = Arrays.copyOfRange(inputData, i+1, inputData.length);
            Arrays.sort(ps, p1.slopeOrder());
            double currSlop = p1.slopeTo(ps[0]);
            int pointCount = 2;
            for (int j = 1; j < ps.length; j++) {
                double s1 = p1.slopeTo(ps[j]);
                if (s1 == currSlop) {
                    pointCount++;
                } else {
                    if (pointCount >= 4) {
                        addLine(p1, ps[j - 1], currSlop);
                    }
                    currSlop = s1;
                    pointCount = 2;
                }
            }
            if (pointCount >= 4) {
                addLine(p1, ps[ps.length - 1], currSlop);
            }
        }
        segments = Arrays.copyOfRange(segments, 0, segmentsCount);
    }

    private void addLine(Point first, Point last, double slope) {
        if (first.compareTo(last) > 0) {
            return;
        }
        boolean duplicate = false;
        for (int i = 0; i < segmentsCount; i++) {
            if (slopes[i] == slope && same(last, lastPoints[i])) {
                duplicate = true;
                break;
            }
        }
        if (!duplicate) {
            int i = segmentsCount++;
            if (segmentsCount >= segments.length) {
                segments = Arrays.copyOf(segments, segments.length * 3 / 2);
                slopes = Arrays.copyOf(slopes, slopes.length * 3 / 2);
                lastPoints = Arrays.copyOf(lastPoints, lastPoints.length * 3 / 2);
            }
            segments[i] = new LineSegment(first, last);
            lastPoints[i] = last;
            slopes[i] = slope;
        }
    }

    /**
     * the number of line segments
     *
     * @return
     */

    public int numberOfSegments() {
        return segmentsCount;
    }

    /**
     * the line segments
     *
     * @return
     */
    public LineSegment[] segments() {

        return Arrays.copyOfRange(segments, 0, segmentsCount);
    }

    private boolean same(Point p1, Point p2) {
        return p1.compareTo(p2) == 0;
    }


}
