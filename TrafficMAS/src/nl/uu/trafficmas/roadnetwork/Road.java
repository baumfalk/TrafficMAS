package nl.uu.trafficmas.roadnetwork;

import java.util.ArrayList;

public class Road {
	public final double length;
	public final ArrayList<Lane> laneList;
	public int priority;
	
	public Road(double length, ArrayList<Lane> laneList, int priority) {
		this.length = length;
		this.laneList = laneList;
		this.priority = priority;
	}
	
}
