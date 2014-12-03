package nl.uu.trafficmas.agent;

import nl.uu.trafficmas.organisation.BruteState;
import nl.uu.trafficmas.roadnetwork.Lane;
import nl.uu.trafficmas.roadnetwork.Road;

public class AgentPhysical implements BruteState {
	private Road road;
	private Lane lane;
	private double distance;
	private double velocity;
	private AgentType agentType;
}
