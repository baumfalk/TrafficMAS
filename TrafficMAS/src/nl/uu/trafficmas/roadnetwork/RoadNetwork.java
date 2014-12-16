package nl.uu.trafficmas.roadnetwork;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class RoadNetwork {
	private ArrayList<Node> nodes;
	private ArrayList<Edge> edges;
	
	public RoadNetwork() {
		nodes = new ArrayList<Node>();
		edges = new ArrayList<Edge>();
	}
	
	public RoadNetwork(ArrayList<Node> nodeList, ArrayList<Edge> edgeList) {
		this.nodes = new ArrayList<Node>();
		this.edges = new ArrayList<Edge>();
		addNodes(nodeList);
		addEdges(edgeList);
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
	
	public void addNodes(ArrayList<Node> newNodes){
		for(int i = 0; i < newNodes.size(); i++){
			nodes.add(newNodes.get(i));
		}
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
	
	public void addEdges(ArrayList<Edge> newEdges){
		for(int i = 0; i < newEdges.size(); i++){
			edges.add(newEdges.get(i));
		}
	}
	
	public void removeEdge(Edge edge){
		edges.remove(edge);
	}
	
	public Road getRoadFromID(String roadID){

		for(Edge edge: edges){
			if (edge.getRoad().id.equals(roadID)){
				return edge.getRoad();
			}
		}
		System.out.println("Failed to find road from ID");
		return null;
	}
	
	public boolean validateRoadNetwork(){

		ArrayList<Road> roads = new ArrayList<Road>();
		
		// Check for duplicate nodes
		Set<Node> setNodes = new HashSet<Node>(nodes);
		if (setNodes.size() < nodes.size()){
			return false;
		}
		
		// Check for duplicate edges
		Set<Edge> setEdges = new HashSet<Edge>(edges);
		if (setEdges.size() < edges.size()){
			return false;
		}
		
		// Check for all edges if,
		for (Edge edge : edges) {
			// "from" and "to" are not the same node.
			if(edge.getFromNode().equals(edge.getToNode())){
				return false;
			}
			// the "from" and to "nodes" are present in the RoadNetwork nodes list.
			if(!nodes.contains(edge.getFromNode()) && !nodes.contains(edge.getToNode())){
				return false;
			}
			// every edge has an unique road.
			roads.add(edge.getRoad());
		}
		
		// Check for duplicate Roads.
		Set<Road> setRoads = new HashSet<Road>(roads);
		if (setRoads.size() < roads.size()){
			return false;
		}
		
		
		// If none of the conditions are met, the RoadNetwork is valid.
		return true;
	}
	
	public void clear(){
		edges.clear();
		nodes.clear();
	}
}
