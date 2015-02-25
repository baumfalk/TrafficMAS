package nl.uu.trafficmas.simulationmodel;

import java.util.LinkedHashMap;
import java.util.Map;

public class SensorData implements Data {

	public final String id;
	public final String[] vehicleIDs;
	private final Map<String, AgentData> vehicleData;
	public SensorData(String id, String[] vehicleIDs) {
		this.id = id;
		this.vehicleIDs = vehicleIDs;
		vehicleData = new LinkedHashMap<String,AgentData>();
		for(String vehicleID : vehicleIDs) {
			vehicleData.put(vehicleID, null);
		}
	}
	public void addAgentData(String agentID, AgentData agentData) {
		if(vehicleData.containsKey(agentID)) {
			vehicleData.put(agentID,agentData);
		}
		
	}
	
	public AgentData getAgentData(String vehicleID) {
		return vehicleData.get(vehicleID);
	}
}
