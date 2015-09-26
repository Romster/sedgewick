import edu.princeton.cs.algs4.StdDraw;

import java.util.Comparator;

/**
 * Created by romster on 24.09.15.
 */
public class Point implements Comparable<Point> {

    private final int x;
    private final int y;

    /**
     * constructs the point (x, y)
     *
     * @param x
     * @param y
     */
    public Point(int x, int y) {
        if (x < 0 || x > 32767
                || y < 0 || y > 32767) {
            throw new IllegalArgumentException("arguments should be between 0 and 32767");
        }
        this.x = x;
        this.y = y;
    }

    /**
     * draws this point
     */
    public void draw() {
         /* DO NOT MODIFY */
        StdDraw.point(x, y);
    }

    /**
     * draws the line segment from this point to that point
     *
     * @param that
     */
    public void drawTo(Point that) {
        /* DO NOT MODIFY */
        StdDraw.line(this.x, this.y, that.x, that.y);

    }

    /**
     * string representation
     *
     * @return
     */
    public String toString() {
        /* DO NOT MODIFY */
        return "(" + x + ", " + y + ")";
    }

    /**
     * compare two points by y-coordinates, breaking ties by x-coordinates
     *
     * @param that
     * @return
     */
    public int compareTo(Point that) {
        if (this.y < that.y) return -1;
        else if (this.y > that.y) return 1;
        else { //this.y == that.y
            if (this.x < that.x) return -1;
            else if (this.x > that.x) return 1;
            else return 0;
        }
    }

    /**
     * the slope between this point and that point
     *
     * @param that
     * @return
     */
    public double slopeTo(Point that) {
        if (this.x == that.x && this.y == that.y) {
            return Double.NEGATIVE_INFINITY;
        }
        if (this.x == that.x) {
            return Double.POSITIVE_INFINITY;
        }
        if (this.y == that.y) {
            return 0.0;
        }
        return (that.y - this.y) / (double) (that.x - this.x);
    }

    /**
     * @return
     */
    public Comparator<Point> slopeOrder() {
        return (Point a, Point b) ->
                Double.compare(Point.this.slopeTo(a), Point.this.slopeTo(b));
    }


    /**
     * Unit tests the Point data type.
     */
    public static void main(String[] args) {
        /* YOUR CODE HERE */
    }

}
