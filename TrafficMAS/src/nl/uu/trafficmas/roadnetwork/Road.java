package nl.uu.trafficmas.roadnetwork;

import java.util.ArrayList;

public class Road {
	private ArrayList<Lane> laneList;
	private double length;
	private int priority;

	public ArrayList<Lane> getLaneList(){
		return laneList;
	}
	
	public double getLength(){
		return length;
	}
	
	public double getPriority(){
		return priority;
	}
}
