package nl.uu.trafficmas.agent;

import java.util.ArrayList;

import de.tudresden.ws.container.SumoColor;
import nl.uu.trafficmas.organisation.Sanction;
import nl.uu.trafficmas.roadnetwork.Edge;
import nl.uu.trafficmas.roadnetwork.Node;

public class PregnantWomanAgent extends Agent {

	public PregnantWomanAgent(String agentID, Node goalNode, Edge[] route, int goalArrivalTime, double maxSpeed,double maxComfySpeed,int currentTime) {
		super(agentID, goalNode, route, goalArrivalTime, maxSpeed, maxComfySpeed,currentTime);
	}

	@Override
	public double specificUtility(int arrivalTime, ArrayList<Sanction> sanctionList) {
		double utility = 0;
		if(arrivalTime == this.getGoalArrivalTime() && sanctionList == null) {
			utility = 1;
		} else{
			utility = (double) (this.getGoalArrivalTime() / (3*arrivalTime));
		}
		
		
		return Math.max(0,Math.min(1, utility));
	}

	@Override
	public SumoColor getColor() {
		//blue
		return new SumoColor(0,0,255,255);
	}
}
