package nl.uu.trafficmas.roadnetwork;

import java.util.List;

import nl.uu.trafficmas.agent.PhysicalObject;
import nl.uu.trafficmas.simulationmodel.AgentData;

public class Sensor extends PhysicalObject {
	private double length;
	
	public double getLength(){
		return length;
	}
	
	public double getEndPosition() {
		return this.distance+this.length;
	}

	public List<AgentData> readSensor() {
		// TODO Auto-generated method stub
		return null;
	}
}
