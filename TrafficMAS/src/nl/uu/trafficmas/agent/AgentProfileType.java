package nl.uu.trafficmas.agent;

import java.util.ArrayList;

import nl.uu.trafficmas.roadnetwork.Edge;
import nl.uu.trafficmas.roadnetwork.Node;
import nl.uu.trafficmas.roadnetwork.Road;

public enum AgentProfileType {
	Normal,
	PregnantWoman,
	OldLady;
	
	public Agent toAgent(String agentID, Node goalNode, Edge[] routeEdges, int goalArrivalTime, double maxSpeed) {
		Agent agent = null;
		switch(this) {
		case Normal:
			agent = new NormalAgent(agentID, goalNode, routeEdges, goalArrivalTime, maxSpeed);
			break;
		case OldLady:
			agent = new OldLadyAgent(agentID, goalNode, routeEdges, goalArrivalTime, maxSpeed);
			break;
		case PregnantWoman:
			agent = new PregnantWomanAgent(agentID, goalNode, routeEdges, goalArrivalTime, maxSpeed);
			break;
		}
		return agent;
	}
}
