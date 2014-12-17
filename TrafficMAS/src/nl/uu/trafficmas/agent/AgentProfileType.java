package nl.uu.trafficmas.agent;

import nl.uu.trafficmas.roadnetwork.Node;

public enum AgentProfileType {
	Normal,
	PregnantWoman,
	OldLady;
	
	public Agent toAgent(String agentID, Node goalNode, int goalArrivalTime, double maxSpeed) {
		Agent agent = null;
		switch(this) {
		case Normal:
			agent = new NormalAgent(agentID, goalNode, goalArrivalTime, maxSpeed);
			break;
		case OldLady:
			agent = new OldLadyAgent(agentID, goalNode, goalArrivalTime, maxSpeed);
			break;
		case PregnantWoman:
			agent = new PregnantWomanAgent(agentID, goalNode, goalArrivalTime, maxSpeed);
			break;
		}
		return agent;
	}
}
