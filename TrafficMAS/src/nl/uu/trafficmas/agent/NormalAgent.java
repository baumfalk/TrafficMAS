package nl.uu.trafficmas.agent;

import java.util.ArrayList;

import de.tudresden.ws.container.SumoColor;
import nl.uu.trafficmas.organisation.Sanction;
import nl.uu.trafficmas.roadnetwork.Edge;
import nl.uu.trafficmas.roadnetwork.Node;

public class NormalAgent extends Agent {

	public NormalAgent(String agentID, Node goalNode, Edge[] routeEdges, int goalArrivalTime, double maxSpeed, double maxComfySpeed, int currentTime) {
		super(agentID, goalNode, routeEdges, goalArrivalTime, maxSpeed, maxComfySpeed, currentTime);
	}
	
	@Override
	public double specificUtility(int arrivalTime, ArrayList<Sanction> sanctionList) {
		double utility = 0;
		utility = (double) (this.getGoalArrivalTime() / (arrivalTime));
		
		return Math.max(0,Math.min(1, utility));
	}

	@Override
	public SumoColor getColor() {
		//red
		return new SumoColor(255,0,0,255);
	}
}
