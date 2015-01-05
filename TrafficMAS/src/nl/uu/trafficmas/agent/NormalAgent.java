package nl.uu.trafficmas.agent;

import java.util.ArrayList;

import nl.uu.trafficmas.organisation.Sanction;
import nl.uu.trafficmas.roadnetwork.Edge;
import nl.uu.trafficmas.roadnetwork.Node;

public class NormalAgent extends Agent {

	public NormalAgent(String agentID, Node goalNode, Edge[] routeEdges, int goalArrivalTime, double maxSpeed, double maxComfySpeed, int currentTime) {
		super(agentID, goalNode, routeEdges, goalArrivalTime, maxSpeed, AgentProfileType.Normal,maxComfySpeed, currentTime);
	}
	
	@Override
	public double specificUtility(int arrivalTime, ArrayList<Sanction> sanctionList) {
		double utility = 0;
		if(arrivalTime == this.getGoalArrivalTime() && sanctionList == null) {
			utility = 1;
		} else{
			utility = (double) (this.getGoalArrivalTime() / (2*arrivalTime));
		}
		
		return Math.max(0,Math.min(1, utility));
	}
}
