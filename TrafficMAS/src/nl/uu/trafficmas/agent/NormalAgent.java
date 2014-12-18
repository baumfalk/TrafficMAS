package nl.uu.trafficmas.agent;

import java.util.ArrayList;

import nl.uu.trafficmas.organisation.Sanction;
import nl.uu.trafficmas.roadnetwork.Edge;
import nl.uu.trafficmas.roadnetwork.Node;
import nl.uu.trafficmas.roadnetwork.Road;

public class NormalAgent extends Agent {

	public NormalAgent(String agentID, Node goalNode, Edge[] routeEdges, int goalArrivalTime, double maxSpeed) {
		super(agentID, goalNode, routeEdges, goalArrivalTime, maxSpeed);
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




	@Override
	public int goalArrivalTime(int startTime, int minimalTravelTime) {
		// normally we are kinda in a hurry
		return startTime + minimalTravelTime *2;
	}

}
