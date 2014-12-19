package nl.uu.trafficmas.roadnetwork;

import java.util.ArrayList;

public class Road {
	public final String id;
	public final double length;
	public final ArrayList<Lane> laneList;
	public int priority;
	private double meanTravelTime;

	public Road(String id, double length, ArrayList<Lane> laneList, int priority) {
		this.id = id;
		this.length = length;
		this.laneList = laneList;
		this.priority = priority;
		this.meanTravelTime = Double.MAX_VALUE;
	}
	
	public double getPriority(){
		return priority;
	}

	public void setMeanTravelTime(double meanTravelTime) {
		this.meanTravelTime = meanTravelTime;
	}
	
	public Lane[] getLanes() {
		Lane[] lanes = new Lane[laneList.size()];
		return laneList.toArray(lanes);
	}
	@Override
	public boolean equals(Object other) {
		if (other == null) return false;
		if (other == this) return true;
		if (!(other instanceof Road))return false;
		Road otherMyClass = (Road)other;
		
		
		return otherMyClass.id.equals(this.id);
	}

	public double getMeanTravelTime() {
		// TODO Auto-generated method stub
		return meanTravelTime;
	}
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("ID:"+id+"\r\n");
		sb.append("meanTime:"+meanTravelTime+"\r\n");

		for(Lane lane : laneList) {
			sb.append("\t LaneID:"+lane.getID()+"\r\n");
			sb.append("\t meanTimeLane:"+lane.getMeanTravelTime()+"\r\n");
		}
		return sb.toString();
		
	}
}
