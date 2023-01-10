package anomaly_detectors;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WelzlAlgorithm {
    private Random rand = new Random();

    public Circle miniDisk(final List<Point> points) {
        return bMinidisk(points, new ArrayList<Point>());
    }

    private Circle bMinidisk(final List<Point> points, final List<Point> boundary) {
        Circle minimumCircle = null;

        if (boundary.size() == 3) {
            minimumCircle = new Circle(boundary.get(0), boundary.get(1), boundary.get(2));
        }
        else if (points.isEmpty() && boundary.size() == 2) {
            minimumCircle = new Circle(boundary.get(0), boundary.get(1));
        }
        else if (points.size() == 1 && boundary.isEmpty()) {
            minimumCircle = new Circle(points.get(0).x, points.get(0).y, (float)0);
        }
        else if (points.size() == 1 && boundary.size() == 1) {
            minimumCircle = new Circle(points.get(0), boundary.get(0));
        }
        else {
            Point p = points.remove(rand.nextInt(points.size()));
            minimumCircle = bMinidisk(points, boundary);

            if (minimumCircle != null && !minimumCircle.containsPoint(p)) {
                boundary.add(p);
                minimumCircle = bMinidisk(points, boundary);
                boundary.remove(p);
                points.add(p);
            }
        }

        return minimumCircle;
    }
}
