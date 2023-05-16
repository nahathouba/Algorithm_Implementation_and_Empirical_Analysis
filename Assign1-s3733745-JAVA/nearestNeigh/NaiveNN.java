package nearestNeigh;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is required to be implemented. Naive approach implementation.
 *
 * 
 */
public class NaiveNN implements NearestNeigh {
	private List<Point> pointsList;

	public NaiveNN() {
		pointsList = new ArrayList<Point>();
	}

	@Override
	public void buildIndex(List<Point> points) {
		// Storing the points
		pointsList = points;
	}

	@Override
	public List<Point> search(Point searchTerm, int k) {
		List<Point> pointsListCopy = new ArrayList<Point>();
		List<Point> kNearestNeigh = new ArrayList<Point>();

		pointsListCopy = pointsList;

		if (k != 0) {
			for (int i = 0; i < pointsListCopy.size() - 1; i++) {
				if (searchTerm.cat == pointsListCopy.get(i).cat) {
					for (int j = i + 1; j < pointsListCopy.size(); j++) {
						if (searchTerm.cat == pointsListCopy.get(j).cat) {
							if (searchTerm.distTo(pointsListCopy.get(i)) > searchTerm.distTo(pointsListCopy.get(j))) {
								Point temp = pointsListCopy.get(i);
								// Swapping the points
								pointsListCopy.add(i, pointsListCopy.get(j));
								pointsListCopy.add(j, temp);

								// Removing the redundant points
								pointsListCopy.remove(i + 1);
								pointsListCopy.remove(j + 1);
							}
						}
					}
				}
			}
			int i = 0;
			while (kNearestNeigh.size() != k) {
				if (pointsListCopy.get(i).cat == searchTerm.cat)
					kNearestNeigh.add(pointsListCopy.get(i));
				i++;
			}
		}
		return kNearestNeigh;
	}

	@Override
	public boolean addPoint(Point point) {
		boolean isPointExist = isPointIn(point);
		if (!isPointExist) {
			pointsList.add(point);
			return true;
		}
		return false;
	}

	@Override
	public boolean deletePoint(Point point) {
		// To be implemented.
		if (isPointIn(point)) {
			for (int i = 0; i < pointsList.size(); i++) {
				if (pointsList.get(i).equals(point)) {
					pointsList.remove(i);
					return true;
				}
			}
		} else {
			for (int i = 0; i < pointsList.size(); i++) {
				if (pointsList.get(i).cat == point.cat && pointsList.get(i).lat == point.lat
						&& pointsList.get(i).lon == point.lon) {
					pointsList.remove(i);
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean isPointIn(Point point) {
		// To be implemented.
		for (int i = 0; i < pointsList.size(); i++) {
			if (pointsList.get(i).equals(point))
				return true;
		}
		return false;
	}

}
