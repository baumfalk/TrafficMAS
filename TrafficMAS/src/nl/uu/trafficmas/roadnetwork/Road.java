package nl.uu.trafficmas.roadnetwork;

import java.util.ArrayList;

public class Road {
	public final String id;
	public final double length;
	public final ArrayList<Lane> laneList;
	public int priority;

	public Road(String id, double length, ArrayList<Lane> laneList, int priority) {
		this.id = id;
		this.length = length;
		this.laneList = laneList;
		this.priority = priority;
	}
	
	public double getPriority(){
		return priority;
	}

	
	public Lane[] getLanes() {
		Lane[] lanes = new Lane[laneList.size()];
		return laneList.toArray(lanes);
	}

}
