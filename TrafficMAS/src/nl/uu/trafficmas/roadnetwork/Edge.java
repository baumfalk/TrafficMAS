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
	
	public Node getFromNode(){
		return from;
	}
	
	public Node getToNode(){
		return to;
	}
	
	public Road getRoad(){
		return road;
	}
	
	public String getID() {
		return road.id;
	}
	
	@Override 
	public String toString() {
		return from+" --> "+to+"\r\n"+road;
	}
}
