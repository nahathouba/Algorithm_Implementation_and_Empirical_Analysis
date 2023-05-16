package nearestNeigh;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

/**
 * This class is required to be implemented. Kd-tree implementation.
 *
 * 
 */
public class KDTreeNN implements NearestNeigh {

	/** Root node of the tree. */
	private Node mRoot;

	public KDTreeNN() {
		mRoot = null;
	}

	@Override
	public void buildIndex(List<Point> points) {
		int splitDim = 0;
		// Calling the recursive build method to build the tree
		buildTree(points, splitDim);

	}

	private void buildTree(List<Point> points, int splitDim) {
		List<Point> leftPoints = new ArrayList<>();
		List<Point> rightPoints = new ArrayList<>();

		// Getting the median point for the current points list
		int medianIndex = getMedian(points, splitDim);
		// Set the left points from the median point
		for (int i = 0; i < medianIndex; i++) {
			leftPoints.add(points.get(i));
		}
		// Set the right points from the median point
		for (int i = medianIndex + 1; i < points.size(); i++) {
			rightPoints.add(points.get(i));
		}

		// Added the median point in the tree
		addPoint(points.get(medianIndex));

		if (leftPoints.size() != 0) {
			buildTree(leftPoints, splitDim);
		}
		if (rightPoints.size() != 0) {
			buildTree(rightPoints, splitDim);
		}

	}

	private int getMedian(List<Point> points, int splitDim) {
		if (splitDim % 2 == 0) {
			// Sorting ascending order based on latitude (x-coordinate)
			for (int i = 0; i < points.size() - 1; i++) {
				for (int j = i + 1; j < points.size(); j++) {
					if (points.get(i).lat > points.get(j).lat) {
						Point temp = points.get(i);
						// Swapping points
						points.add(i, points.get(j));
						points.add(j, temp);

						// Removing the redundant points
						points.remove(i + 1);
						points.remove(j + 1);
					}
				}
			}
			int mid = (int) Math.floor(points.size() / 2);
			// If any same latitude (x-coordinate) exists on the left, take that point as
			// Median Point
			for (int i = 0; i < mid; i++) {
				if (points.get(i).lat == points.get(mid).lat)
					return i;
			}
			return mid;
		} else {
			// Sorting ascending order based on longitude (y-coordinate)
			for (int i = 0; i < points.size() - 1; i++) {
				for (int j = i + 1; j < points.size(); j++) {
					if (points.get(i).lon > points.get(j).lon) {
						Point temp = points.get(i);
						// Swapping points
						points.add(i, points.get(j));
						points.add(j, temp);

						// Removing the redundant points
						points.remove(i + 1);
						points.remove(j + 1);
					}
				}
			}
			int mid = (int) Math.floor(points.size() / 2);
			/*
			 * If any same longitude (y-coordinate) exists on the left, take that point as
			 * median point
			 */
			for (int i = 0; i < mid; i++) {
				if (points.get(i).lon == points.get(mid).lon)
					return i;
			}
			return mid;
		}
	}

	@Override
	public List<Point> search(Point searchTerm, int k) {
		List<Point> nearestNeighbour = new ArrayList<Point>();
		List<Point> returnNeighbour = new ArrayList<Point>();
		int count = 0;
		while (count < k) {
			nearestNeighbour.add((findOneNN(searchTerm, mRoot, mRoot, 0)).point);
			count++;
		}
		
		Collections.sort(nearestNeighbour, new PointComparator(searchTerm));
		
		for (int i = 0; i < k; i++) {
			returnNeighbour.add(nearestNeighbour.get(i));
		}
		return nearestNeighbour;
	}
	
	private Node findOneNN(Point search, Node root, Node best, int splitDim) {
		Node goodSide = null;
		Node badSide = null;
		if (root == null || (root.getVisit() == true) || (search.cat != root.point.cat)) return best;
		
		if (search.distTo(root.point) < search.distTo(best.point)) best = root;
		
		if (splitDim % 2 == 0) {
			if (search.lat < root.point.lat) {
				goodSide = root.leftChild;
				badSide = root.rightChild;
			} else {
				goodSide = root.rightChild;
				badSide = root.leftChild;
			}
		} else {
			if (search.lon < root.point.lon) {
				goodSide = root.leftChild;
				badSide = root.rightChild;
			} else {
				goodSide = root.rightChild;
				badSide = root.leftChild;
			}
		}
		
		best = findOneNN(search, goodSide, best, ++splitDim);
		if (badSide != null) {
			if (search.distTo(best.point) > search.distTo(badSide.point)) {
				best = findOneNN(search, badSide, best, ++splitDim);
			}
		}
		best.setVisit(true);
		return best;
	}
	
	
	

	@Override
	public boolean addPoint(Point point) {
		int xDim = 0;
		if (!isPointIn(point)) {
			mRoot = addPointRecursive(mRoot, point, xDim);
			return true;
		}
		return false;
	}

	private Node addPointRecursive(Node root, Point point, int splitDim) {
		/*
		 * If splitDim is divisible by 2, which means x-dim split OR is not divisible by
		 * 2 means y-dim split.
		 */
		if (splitDim % 2 == 0) {
			if (root == null) {
				root = new Node(point);
			} else if (point.lat < root.point.lat) {
				root.leftChild = addPointRecursive(root.leftChild, point, ++splitDim);
			} else if (point.lat >= root.point.lat) {
				root.rightChild = addPointRecursive(root.rightChild, point, ++splitDim);
			}
		} else {
			if (root == null) {
				root = new Node(point);
			} else if (point.lon < root.point.lon) {
				root.leftChild = addPointRecursive(root.leftChild, point, ++splitDim);
			} else if (point.lon >= root.point.lon) {
				root.rightChild = addPointRecursive(root.rightChild, point, ++splitDim);
			}
		}
		return root;
	}

	@Override
	public boolean deletePoint(Point point) {
		if (isPointIn(point)) {
			deletePoint(point, mRoot, 0);
			return true;
		}
		return false;
	}

	private Node deletePoint(Point point, Node root, int splitDim) {
		if (root == null)
			return root;

		if (root.point.equals(point) || (root.point.lat == point.lat && root.point.lon == point.lon)) {
			if (root.rightChild != null) {
				Node min = findMinNode(root.rightChild, splitDim, (splitDim + 1) % 2);
				copyPoint(root, min);
				root.rightChild = deletePoint(min.point, root.rightChild, ++splitDim);
			} else if (root.leftChild != null) {
				Node min = findMinNode(root.leftChild, splitDim, (splitDim + 1) % 2);
				root.rightChild = deletePoint(min.point, root.leftChild, ++splitDim);
			} else {
				root = null;
				return root;
			}
			return root;
		}

		if (splitDim % 2 == 0) {
			if (point.lat < root.point.lat) {
				root.leftChild = deletePoint(point, root.leftChild, ++splitDim);
			} else {
				root.rightChild = deletePoint(point, root.rightChild, ++splitDim);
			}
			return root;
		} else {
			if (point.lon < root.point.lon) {
				root.leftChild = deletePoint(point, root.leftChild, ++splitDim);
			} else {
				root.rightChild = deletePoint(point, root.rightChild, ++splitDim);
			}
			return root;
		}
	}

	private void copyPoint(Node root, Node minPoint) {
		root.point = minPoint.point;
	}

	private Node findMinNode(Node root, int dim, int splitDim) {
		Node temp1, temp2;
		Point point1 = null;
		Point point2 = null;

		if (root == null)
			return root;

		temp1 = findMinNode(root.leftChild, dim, (splitDim + 1) % 2);
		if (temp1 != null) {
			point1 = temp1.point;
		}
		if (dim != splitDim) {
			temp2 = findMinNode(root.rightChild, dim, (splitDim + 1) % 2);
			if (temp2 != null) {
				point2 = temp2.point;
			}
			if (dim % 2 == 0) {
				if ((temp1 == null) || ((temp2 != null) && point1.lat > point2.lat)) {
					temp1 = temp2;
				}
			} else {
				if ((temp1 == null || ((temp2 != null) && point1.lon > point2.lon))) {
					temp1 = temp2;
				}
			}
			point1 = point2;
		}
		Point p = root.point;
		if (dim % 2 == 0) {
			if ((temp1 == null) || (point1.lat > p.lat)) {
				return root;
			} else {
				return temp1;
			}
		} else {
			if ((temp1 == null || (point1.lon > p.lon))) {
				return root;
			} else {
				return temp1;
			}
		}
	}

	@Override
	public boolean isPointIn(Point point) {
		boolean checkPointExist = checkPoint(mRoot, point, 0);
		return checkPointExist;
	}

	private boolean checkPoint(Node root, Point point, int splitDim) {
		boolean isPointExist = false;

		if (root == null)
			return false;

		if (splitDim % 2 == 0) {
			if (root.point.equals(point) || (root.point.lat == point.lat && root.point.lon == point.lon)) {
				isPointExist = true;
			} else if (point.lat < root.point.lat) {
				isPointExist = checkPoint(root.leftChild, point, ++splitDim);
			} else if (point.lat >= root.point.lat) {
				isPointExist = checkPoint(root.rightChild, point, ++splitDim);
			}
		} else {
			if (root.point.equals(point) || (root.point.lat == point.lat && root.point.lon == point.lon)) {
				isPointExist = true;
			} else if (point.lon < root.point.lon) {
				isPointExist = checkPoint(root.leftChild, point, ++splitDim);
			} else if (point.lon >= root.point.lon) {
				isPointExist = checkPoint(root.rightChild, point, ++splitDim);
			}
		}
		return isPointExist;
	}

}
