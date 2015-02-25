package nl.uu.trafficmas.roadnetwork;

import nl.uu.trafficmas.agent.PhysicalObject;

public class Sensor extends PhysicalObject {
	private final String id;
	private final Lane lane;
	private final double length;
	private final double position;
	private final int frequency;
	
	public Sensor(String id, Lane lane, double position, double length, int frequency){
		this.id 		= id;
		this.lane		= lane;
		this.position	= position;
		this.length		= length;
		this.frequency	= frequency;
	}
	
	public String getId(){
		return id;
	}
	
	public Lane getLane(){
		return lane;
	}
	
	public double getPosition(){
		return position;
	}
	
	public double getLength(){
		return length;
	}
	
	public int getFrequency(){
		return frequency;
	}
}
