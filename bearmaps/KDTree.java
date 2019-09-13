package bearmaps;
// import java.util.ArrayList;
// import java.util.HashSet;
import java.util.List;

public class KDTree implements PointSet {

    private List<Point> pointList;
    private KDTreeNode root;
    private static final boolean HORIZONTAL = false;

    /* Constructs a KDTree using POINTS. You can assume POINTS contains at least one
       Point object. */
    public KDTree(List<Point> points) {
        root = new KDTreeNode(points.get(0));
        KDTreeNode curr = root;
        for (Point p: points) {
            root = insert(p, root, curr.orientation);
        }
    }

    /* You might find this insert helper method useful when constructing your KDTree!
    Think of what arguments you might want insert to take in. If you need
    inspiration, take a look at how we do BST insertion! */

    private KDTreeNode insert(Point p, KDTreeNode node, boolean orientation) {
        if (node == null) {
            KDTreeNode newChild = new KDTreeNode(p);
            node = newChild;
            newChild.orientation = orientation;
            return newChild;
        }
        if (p.equals(node.point)) {
            return node;
        }
        if (comparePoint(p, node.point, orientation) < 0) {
            node.left = insert(p, node.left, !orientation);
        } else if (comparePoint(p, node.point, orientation) >= 0) {
            node.right = insert(p, node.right, !orientation);
        }
        return node;
    }

    private double comparePoint(Point a, Point b, boolean orientation) {
        if (orientation == HORIZONTAL) {
            return a.getX() - b.getX();
        } else {
            return a.getY() - b.getY();
        }
    }

    /* Returns the closest Point to the inputted X and Y coordinates. This method
       should run in O(log N) time on average, where N is the number of POINTS.*/
    public Point nearest(double x, double y) {
        Point p = new Point(x, y);
        return nearestHelper(root, p, root.point);
    }

    public Point nearestHelper(KDTreeNode n, Point goal, Point best) {
        KDTreeNode goodSide;
        KDTreeNode badSide;
        if (n == null) {
            return best;
        }
        if (Point.distance(n.point, goal) < Point.distance(best, goal)) {
            best = n.point;
        }
        if (comparePoint(goal, n.point, n.orientation) < 0) {
            goodSide = n.left();
            badSide = n.right();
        } else {
            goodSide = n.right();
            badSide = n.left();
        }
        best = nearestHelper(goodSide, goal, best);
        double nearest = Point.distance(best, goal);
        if (n.orientation == HORIZONTAL) {
            if (Point.distance(goal, new Point(n.point.getX(), goal.getY())) < nearest) {
                best = nearestHelper(badSide, goal, best);
            }
        } else {
            if (Point.distance(goal, new Point(goal.getX(), n.point.getY())) < nearest) {
                best = nearestHelper(badSide, goal, best);
            }
        }
        return best;
    }

    private class KDTreeNode {

        private Point point;
        private KDTreeNode left; // also refers to down child
        private KDTreeNode right; // also refers to up child
        private boolean orientation;
        // If you want to add any more instance variables, put them here!

        KDTreeNode(Point p) {
            this.point = p;
        }

        KDTreeNode(Point p, KDTreeNode left, KDTreeNode right) {
            this.point = p;
            this.left = left;
            this.right = right;
        }

        Point point() {
            return point;
        }

        KDTreeNode left() {
            return left;
        }

        KDTreeNode right() {
            return right;
        }

        // If you want to add any more methods, put them here!
    }

    public static void main(String[] args) {
        Point p1 = new Point(2, 3); // constructs a Point with x = 1.1, y = 2.2
        Point p2 = new Point(4, 2);
        Point p3 = new Point(4, 2);
        Point p4 = new Point(4, 5);
        Point p5 = new Point(3, 3);
        Point p6 = new Point(1, 5);
        Point p7 = new Point(4, 4);

        KDTree kd = new KDTree(List.of(p1, p2, p3, p4, p5, p6, p7));
        Point ret = kd.nearest(3.0, 4.0); // returns p2
        System.out.println(ret.getX()); // evaluates to 3.3
        System.out.println(ret.getY()); // evaluates to 4.4
    }
}
