import java.util.Arrays;

/**
 * Created by romster on 24.09.15.
 */
public class BruteCollinearPoints {

    private LineSegment[] segments;
    private int segmentsCount;

    /**
     * finds all line segments containing 4 points
     *
     * @param points
     */
    public BruteCollinearPoints(Point[] points) {
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
        fillSegments(points);
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
     * @return the line segments
     */
    public LineSegment[] segments() {
        return Arrays.copyOfRange(segments, 0, segmentsCount);
    }

    private void fillSegments(Point[] ps) {
        segments = new LineSegment[ps.length - 3];
        for (int i = 0; i < ps.length - 3; i++) {
            Point p1 = ps[i];
            for (int j = i + 1; j < ps.length - 2; j++) {
                Point p2 = ps[j];
                double slope = p1.slopeTo(p2);
                for (int g = j + 1; g < ps.length - 1; g++) {
                    Point p3 = ps[g];
                    if (p1.slopeTo(p3) == slope) {
                        for (int h = g + 1; h < ps.length; h++) {
                            Point p4 = ps[h];
                            if (p1.slopeTo(p4) == slope) {
                                Point[] mp = sortPoints(p1, p2, p3, p4);
                                segments[segmentsCount++] = new LineSegment(mp[0], mp[3]);
                            }
                        }
                    }
                }
            }
        }
    }


    private Point[] sortPoints(Point p1, Point p2, Point p3, Point p4) {
        Point[] arr = {p1, p2, p3, p4};
        Arrays.sort(arr);
        return arr;
    }

    private boolean same(Point p1, Point p2) {
        return p1.compareTo(p2) == 0;
    }
}
