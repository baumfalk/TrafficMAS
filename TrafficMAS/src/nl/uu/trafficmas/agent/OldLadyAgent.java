package nl.uu.trafficmas.agent;

import java.util.ArrayList;

import nl.uu.trafficmas.organisation.Sanction;
import nl.uu.trafficmas.roadnetwork.Node;

public class OldLadyAgent extends Agent {

	public OldLadyAgent(String agentID, Node goalNode, int goalArrivalTime,
			double maxSpeed) {
		super(agentID, goalNode, goalArrivalTime, maxSpeed);
		// TODO Auto-generated constructor stub
	}

	@Override
	public double specificUtility(int arrivalTime, ArrayList<Sanction> sanctionList) {
		double utility = 0;
		if(arrivalTime == this.getGoalArrivalTime() && sanctionList == null) {
			utility = 1;
		} else{
			utility = (double) (this.getGoalArrivalTime() / (arrivalTime));
		}
		
		return Math.max(0,Math.min(1, utility));
	}

}
