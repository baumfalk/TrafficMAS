package nl.uu.trafficmas.agent;

import nl.uu.trafficmas.organisation.BruteState;

public class AgentPhysical extends PhysicalObject implements BruteState {
	private double velocity;
	private AgentType agentType;
	
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
}
