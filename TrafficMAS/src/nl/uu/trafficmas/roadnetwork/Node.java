package nl.uu.trafficmas.roadnetwork;

public class Node {
	public final String name;
	public final double x;
	public final double y;
	public Node(String name,double x, double y) {
		this.name = name;
		this.x = x;
		this.y = y;
	}
	
	@Override
	public boolean equals(Object node)  {
		if(!(node instanceof Node))
			return false;
		return ((Node) node).name.equals(name) && 0 == nodeDistance(this,((Node)node));	
	}
	
	public static double nodeDistance(Node fromNode, Node toNode) {
		return Math.sqrt(Math.pow(Math.abs(fromNode.x-toNode.x),2)+Math.pow(Math.abs(fromNode.y-toNode.y),2));
	}
}
