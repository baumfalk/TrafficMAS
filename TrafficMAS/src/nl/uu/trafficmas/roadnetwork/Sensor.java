package nl.uu.trafficmas.roadnetwork;

import java.util.List;

import nl.uu.trafficmas.agent.PhysicalObject;
import nl.uu.trafficmas.simulationmodel.AgentData;
import nl.uu.trafficmas.simulationmodel.SensorData;

public class Sensor extends PhysicalObject {
	public final double length;
	public final String id;
	
	public Sensor(String id, double length) {
		this.id 	= id;
		this.length = length;
	}
	
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

	public void updateSensorData(SensorData sensorData) {
		// TODO Auto-generated method stub
		
	}
}
