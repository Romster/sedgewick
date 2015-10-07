import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by romster on 07.10.15.
 */
public class KdTree {

    private Node root;
    private int size;

    public KdTree() {
    }

    public boolean isEmpty() {
        return root == null;
    }

    /**
     * number of points in the set
     *
     * @return
     */
    public int size() {
        return size;
    }

    /**
     * add the point to the set (if it is not already in the set)
     *
     * @param p
     */
    public void insert(Point2D p) {
        if (p == null) throw new NullPointerException();
        Node newNode = new Node(p);
        root = addNode(newNode, root);
    }

    private Node addNode(Node newNode, Node parent) {
        if (parent == null) {
            size++;
            return newNode;
        }
        newNode.isHorizontal = !parent.isHorizontal;
        int comp = newNode.compareTo(parent);
        if (comp > 0) {
            parent.rt = addNode(newNode, parent.rt);
        } else if (comp < 0) {
            parent.lb = addNode(newNode, parent.lb);
        } else {
            parent.point = newNode.point;
        }
        return parent;
    }

    /**
     * does the set contain point p?
     *
     * @param p
     * @return
     */
    public boolean contains(Point2D p) {
        Node searchNode = new Node(p);
        return contains(searchNode, root);
    }

    private boolean contains(Node searchNode, Node parent) {
        if (parent == null) return false;
        int result = searchNode.compareTo(parent);
        if (result > 0) {
            return contains(searchNode, parent.rt);
        } else if (result < 0) {
            return contains(searchNode, parent.lb);
        } else {
            return true;
        }
    }

    /**
     * draw all points to standard draw
     */
    public void draw() {

    }

    /**
     * all points that are inside the rectangle
     *
     * @param rect
     * @return
     */
    public Iterable<Point2D> range(RectHV rect) {
        List<Point2D> points = new ArrayList<>();
        if (!isEmpty()) {
            reccurRange(root, rect, points);
        }
        return points;
    }

    private void reccurRange(Node node, RectHV rect, List<Point2D> range) {
        if (rect.contains(node.point)) {
            range.add(node.point);
        }
        if (node.lb != null) {
            boolean ignoreLeftBottom = (!node.isHorizontal && rect.xmin() > node.point.x())
                    || (node.isHorizontal && rect.ymin() > node.point.y());
            if (!ignoreLeftBottom) {
                reccurRange(node.lb, rect, range);
            }
        }
        if (node.rt != null) {
            boolean ignoreRightTop = (!node.isHorizontal && rect.xmax() < node.point.x())
                    || (node.isHorizontal && rect.ymax() < node.point.y());
            if (!ignoreRightTop) {
                reccurRange(node.rt, rect, range);
            }
        }
    }

    /**
     * a nearest neighbor in the set to point p; null if the set is empty
     *
     * @param p
     * @return
     */
    public Point2D nearest(Point2D p) {
        if (isEmpty()) return null;
        Node targetNode = new Node(p);
        return reccurNearest(targetNode, root, null).point;
    }

    private Champion reccurNearest(Node targetPointNode, Node currNode, Champion lastChampion) {
        Champion currChampion = lastChampion;
        if (currChampion == null) {
            currChampion = new Champion(currNode.point, targetPointNode.point);
        } else {
            double newDistantSqr = currNode.point.distanceSquaredTo(targetPointNode.point);
            if (newDistantSqr < currChampion.distanceToTargetSqr) {
                currChampion = new Champion(currNode.point, targetPointNode.point);
            }
        }
        int comp = (targetPointNode.compareTo(currNode));
        if (comp < 0) {
            currChampion = searchChampionInLeft(targetPointNode, currNode, currChampion);
            currChampion = searchChampionInRight(targetPointNode, currNode, currChampion);
        } else if (comp > 0) {
            currChampion = searchChampionInRight(targetPointNode, currNode, currChampion);
            currChampion = searchChampionInLeft(targetPointNode, currNode, currChampion);
        } else {
            return currChampion;
        }
        return currChampion;
    }

    private Champion searchChampionInLeft(Node targetPointNode, Node currNode, Champion lastChampion) {
        Champion currChampion = lastChampion;
        if (currNode.lb == null)
            return currChampion;
        double distanceFromChampionToTarget = currChampion.distanceToTargetNoSqr();
        boolean ignoreLeftBottom =
                (currNode.isHorizontal
                        && targetPointNode.point.y() - currNode.point.y() > distanceFromChampionToTarget)
                        || (!currNode.isHorizontal
                        && targetPointNode.point.x() - currNode.point.x() > distanceFromChampionToTarget);
        if (!ignoreLeftBottom) {
            currChampion = reccurNearest(targetPointNode, currNode.lb, currChampion);
        }
        return currChampion;
    }

    private Champion searchChampionInRight(Node targetPointNode, Node currNode, Champion lastChampion) {
        Champion currChampion = lastChampion;
        if (currNode.rt == null)
            return currChampion;
        double distanceFromChampionToTarget = currChampion.distanceToTargetNoSqr();
        boolean ignoreRightTop =
                (currNode.isHorizontal
                        && currNode.point.y() - targetPointNode.point.y() > distanceFromChampionToTarget)
                        || (!currNode.isHorizontal
                        && currNode.point.x() - targetPointNode.point.x() > distanceFromChampionToTarget);
        if (!ignoreRightTop) {
            currChampion = reccurNearest(targetPointNode, currNode.rt, currChampion);
        }
        return currChampion;
    }

    public static void main(String[] args) {

    }


    private class Champion {
        private Point2D point;
        private double distanceToTargetSqr;
        private double distanceNotToUseOutside = Double.NaN; //DO NOT USE OUTSIDE! (use distanceToTargetNoSqr())

        public Champion(Point2D point, Point2D target) {
            this.point = point;
            distanceToTargetSqr = point.distanceSquaredTo(target);
        }

        private double distanceToTargetNoSqr() {
            if (Double.isNaN(distanceNotToUseOutside)) {
                distanceNotToUseOutside = Math.sqrt(distanceToTargetSqr);
            }
            return distanceNotToUseOutside;
        }
    }


    private class Node implements Comparable<Node> {
        private Point2D point;      // the point
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree
        private boolean isHorizontal; //in 2d tree - is this a horizontal node

        public Node(Point2D p) {
            this.point = p;
        }

        @Override
        public int compareTo(Node o) {
            int result;
            if (o.isHorizontal) {
                result = Double.compare(this.point.y(), o.point.y());
            } else {
                result = Double.compare(this.point.x(), o.point.x());
            }
            if (result == 0 && !this.point.equals(o.point)) {
                return 1;
            }
            return result;
        }
    }
}
