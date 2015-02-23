package nl.uu.trafficmas.datamodel;

import java.util.HashMap;
import java.util.LinkedHashMap;

import nl.uu.trafficmas.agent.AgentProfileType;

public class MASData {
	public final int simulationLength;
	public final LinkedHashMap<String, Double> spawnProbabilities;
	public final boolean multipleRoutes;
	public final HashMap<String, LinkedHashMap<AgentProfileType, Double>> routeAgentTypeSpawnDist;
	public MASData(int simulationLength, 
			LinkedHashMap<String,Double> spawnProbabilities, 
			boolean multipleRoutes,
			HashMap<String, LinkedHashMap<AgentProfileType, Double>> routeAgentTypeSpawnDist) {
		this.simulationLength 			= simulationLength;
		this.spawnProbabilities 		= spawnProbabilities;
		this.multipleRoutes 			= multipleRoutes;
		this.routeAgentTypeSpawnDist 	= routeAgentTypeSpawnDist;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("SimulationLength: "+simulationLength+"\r\n");
		sb.append("spawnProbability: "+spawnProbabilities+"\r\n");
		sb.append("multipleRoutes: "+multipleRoutes+"\r\n");
		sb.append("AgentType Spawnprob per route"+routeAgentTypeSpawnDist+"\r\n");
		return sb.toString();
	}
	
}
