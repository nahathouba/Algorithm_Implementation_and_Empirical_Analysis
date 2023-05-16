package nearestNeigh;

import java.util.Comparator;

public class PointComparator implements Comparator<Point>{
	Point target;
	public PointComparator(Point target) {
		this.target = target;
	}
	public int compare (Point p1, Point p2) {
		if (p1 == null)
			return 0;
		if (p2 == null)
			return 0;
		if (target.distTo(p1) < target.distTo(p2)) {
			return -1;
		} else if (target.distTo(p1) > target.distTo(p2)){
			return 1;
		}
		return 0;
	}

}
