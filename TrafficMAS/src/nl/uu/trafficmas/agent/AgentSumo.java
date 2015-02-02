package nl.uu.trafficmas.agent;

import nl.uu.trafficmas.roadnetwork.Node;
import nl.uu.trafficmas.roadnetwork.RoadNetwork;
import nl.uu.trafficmas.roadnetwork.Route;
import de.tudresden.ws.container.SumoColor;

public abstract class AgentSumo extends Agent {

	public AgentSumo(String agentID, Node goalNode, Route route, RoadNetwork roadNetwork,
			int goalArrivalTime, double maxSpeed, double maxComfySpeed) {
		super(agentID, goalNode, route, roadNetwork, goalArrivalTime, maxSpeed, maxComfySpeed);
	}
	
	public abstract SumoColor getColor();
}
