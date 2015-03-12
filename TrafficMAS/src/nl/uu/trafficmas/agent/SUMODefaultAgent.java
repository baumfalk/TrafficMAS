package nl.uu.trafficmas.agent;

import java.util.List;

import nl.uu.trafficmas.norm.Sanction;
import nl.uu.trafficmas.roadnetwork.Node;
import nl.uu.trafficmas.roadnetwork.RoadNetwork;
import nl.uu.trafficmas.roadnetwork.Route;
import de.tudresden.ws.container.SumoColor;

public class SUMODefaultAgent extends AgentSumo {

	public SUMODefaultAgent(String agentID, Node goalNode, Route route,
			RoadNetwork roadNetwork, int goalArrivalTime, double maxSpeed,
			double maxComfySpeed) {
		super(agentID, goalNode, route, roadNetwork, goalArrivalTime, maxSpeed,
				maxComfySpeed);
	}

	@Override
	public SumoColor getColor() {
		return new SumoColor(255,255,0,255);
	}

	@Override
	public double specificUtility(double time, List<Sanction> sanctionList) {
		// TODO Auto-generated method stub
		return 0;
	}

}
