package nl.uu.trafficmas.agent;

import java.util.List;

import nl.uu.trafficmas.norm.Sanction;
import nl.uu.trafficmas.roadnetwork.Node;
import nl.uu.trafficmas.roadnetwork.RoadNetwork;
import nl.uu.trafficmas.roadnetwork.Route;
import de.tudresden.ws.container.SumoColor;

public class HotShotAgent extends AgentSumo {

	public HotShotAgent(String agentID, Node goalNode, Route route, RoadNetwork roadNetwork, int goalArrivalTime, double maxSpeed,double maxComfySpeed) {
		super(agentID, goalNode, route, roadNetwork, goalArrivalTime, maxSpeed, maxComfySpeed);
	}

	@Override
	public double specificUtility(double arrivalTime, List<Sanction> sanctionList) {
		double utility = 0;
		utility = (double) (this.getGoalArrivalTime() / (arrivalTime));
		
		return Math.max(0,Math.min(1, utility));
	}

	@Override
	public SumoColor getColor() {
		//blue
		return new SumoColor(0,0,255,255);
	}
}
