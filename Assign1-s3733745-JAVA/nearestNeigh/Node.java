package nearestNeigh;

public class Node {
	/** Point that is stored in the node. */
    public Point point;
    /** Reference to left child. */
    public Node leftChild;
    /** Reference to right child. */
    public Node rightChild;
    
    private boolean visited;


    /**
     * Constructor.
     *
     * @param point Point to store in node.
     */
    public Node(Point point) {
            this.point = point;
            leftChild = null;
            rightChild = null;
            visited = false;
    }


    /**
     *
     * @return Point stored in node.
     */
    public Point getPoint() {
        return point;
    }  // end of getPoint()


    /**
     *
     * @return Reference to left child of node.
     */
    public Node leftChild() {
        return leftChild;
    } // end of leftChild()


    /**
     *
     * @return Reference to right child of node.
     */
    public Node rightChild() {
        return rightChild;
    } // end of rightChild()
    
    public void setVisit(boolean v) {
    	this.visited = v;
    }
    
    public boolean getVisit() {
    	return visited;
    }
    

}
