package nl.uu.trafficmas.agent;

import nl.uu.trafficmas.organisation.BruteState;

public class AgentPhysical extends PhysicalObject implements BruteState {
	private double velocity;
	private AgentType agentType;
	private static int currentAgentID=0;

	
	public double getVelocity() {
		return velocity;
	}
	public void setVelocity(double velocity) {
		this.velocity = velocity;
	}
	
	public AgentType getAgentType() {
		return agentType;
	}
	public void setAgentType(AgentType agentType) {
		this.agentType = agentType;
	}
	
	public static String getNextAgentID() {
		return "Agent "+ currentAgentID++;
	}
}
