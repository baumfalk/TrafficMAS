package nl.uu.trafficmas.roadnetwork;

import java.util.ArrayList;

public class RoadNetwork {
	private ArrayList<Node> nodes;
	private ArrayList<Edge> edges;
	
	public RoadNetwork() {
		nodes = new ArrayList<Node>();
		edges = new ArrayList<Edge>();
	}
	
	public Node[] getNodes() {
		Node[] list = new Node[nodes.size()];
		return nodes.toArray(list);
	}
	
	public Edge[] getEdges() {
		Edge[] list = new Edge[edges.size()];
		return edges.toArray(list);
	}
	
	public void addNode(Node node) {
		nodes.add(node);
	}
	
	public void removeNode(Node node) {
		nodes.remove(node);
	}
	
	public void addEdge(Edge edge) {
		edges.add(edge);
	}
	
	public void addEdge(Node from, Node to, Road road) {
		edges.add(new Edge(from,to,road));
	}
	
	public void removeEdge(Edge edge){
		edges.remove(edge);
	}
	
}
