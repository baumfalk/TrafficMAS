package nl.uu.trafficmas.datamodel;

import java.util.HashMap;

import nl.uu.trafficmas.agent.AgentProfileType;

public class MASData {
	public final int simulationLength;
	public final double spawnProbability;
	public final HashMap<AgentProfileType, Double> agentProfileTypeDistribution;
	public final HashMap<String, Double> routeIdAndProbability;
	public MASData(int simulationLength, double spawnProbability,
			HashMap<AgentProfileType, Double> agentProfileTypeDistribution, 
			HashMap<String, Double> routeIdAndProbability) {
		this.simulationLength 				= simulationLength;
		this.spawnProbability 				= spawnProbability;
		this.agentProfileTypeDistribution 	= agentProfileTypeDistribution;
		this.routeIdAndProbability 			= routeIdAndProbability;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("SimulationLength: "+simulationLength+"\r\n");
		sb.append("spawnProbability: "+spawnProbability+"\r\n");
		sb.append("Agent Profile Distr"+agentProfileTypeDistribution+"\r\n");
		sb.append("Route Spawn Prob"+routeIdAndProbability+"\r\n");
		return sb.toString();
	}
	
}
