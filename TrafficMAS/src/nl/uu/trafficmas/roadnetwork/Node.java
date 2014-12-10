package nl.uu.trafficmas.roadnetwork;

public class Node {
	public final String name;
	public Node(String name) {
		this.name = name;
	}
	
	@Override
	public boolean equals(Object node)  {
		if(!(node instanceof Node))
			return false;
		return ((Node) node).name.equals(name);	
	}
}
