package nl.uu.trafficmas.roadnetwork;

public class Edge {
	private Node from;
	private Node to;
	private Road road;
	
	public Edge(Node from, Node to, Road road) {
		this.from = from;
		this.to = to;
		this.road = road;
	}
}
