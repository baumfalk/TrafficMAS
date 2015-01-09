package nl.uu.trafficmas.agent;

import java.util.ArrayList;

import de.tudresden.ws.container.SumoColor;
import nl.uu.trafficmas.organisation.Sanction;
import nl.uu.trafficmas.roadnetwork.Edge;
import nl.uu.trafficmas.roadnetwork.Node;

public class HotShotAgent extends Agent {

	public HotShotAgent(String agentID, Node goalNode, Edge[] route, int goalArrivalTime, double maxSpeed,double maxComfySpeed) {
		super(agentID, goalNode, route, goalArrivalTime, maxSpeed, maxComfySpeed);
	}

	@Override
	public double specificUtility(double arrivalTime, ArrayList<Sanction> sanctionList) {
		double utility = 0;
		utility = (double) (this.getGoalArrivalTime() / (3*arrivalTime));
		
		return Math.max(0,Math.min(1, utility));
	}

	@Override
	public SumoColor getColor() {
		//blue
		return new SumoColor(0,0,255,255);
	}
}
