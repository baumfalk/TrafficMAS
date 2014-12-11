package nl.uu.trafficmas.agent;

import java.util.ArrayList;

import nl.uu.trafficmas.organisation.Sanction;
import nl.uu.trafficmas.roadnetwork.Node;

public class NormalAgent extends Agent {

	public NormalAgent(String agentID, Node goalNode, int goalArrivalTime,
			double maxSpeed) {
		super(agentID, goalNode, goalArrivalTime, maxSpeed);
		// TODO Auto-generated constructor stub
	}

	
	
	
	@Override
	public double utility(int arrivalTime, ArrayList<Sanction> sanctionList) {
		// TODO Auto-generated method stub
		
		
		
		return 0;
	}

}
