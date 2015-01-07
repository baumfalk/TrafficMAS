package nl.uu.trafficmas.datamodel;

import java.util.HashMap;

import nl.uu.trafficmas.agent.AgentProfileType;

public class MASData {
	public final int simulationLength;
	public final String sumoConfigPath;
	public final double spawnProbability;
	public final HashMap<AgentProfileType, Double> agentProfileTypeDistribution;
	public MASData(int simulationLength, String sumoConfigPath, 
			double spawnProbability,
			HashMap<AgentProfileType, Double> agentProfileTypeDistribution) {
		this.simulationLength 				= simulationLength;
		this.sumoConfigPath 				= sumoConfigPath;
		this.spawnProbability 				= spawnProbability;
		this.agentProfileTypeDistribution 	= agentProfileTypeDistribution;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("SimulationLength: "+simulationLength+"\r\n");
		sb.append("sumoConfigPath: "+sumoConfigPath+"\r\n");
		sb.append("spawnProbability: "+spawnProbability+"\r\n");
		sb.append("Agent Profile Distr"+agentProfileTypeDistribution+"\r\n");
		return sb.toString();
		
	}
	
}
