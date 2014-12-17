package nl.uu.trafficmas.agent;

import java.util.ArrayList;

import nl.uu.trafficmas.agent.actions.AgentAction;
import nl.uu.trafficmas.organisation.Sanction;
import nl.uu.trafficmas.roadnetwork.Node;

public abstract class Agent extends AgentPhysical {
	private Node goalNode;
	private int goalArrivalTime;
	private final double maxSpeed;
	
	private int expectedArrivalTime;
	private ArrayList<Sanction> currentSanctionList;
	
	public final static double DEFAULT_MAX_SPEED = 70;
	
	public abstract double specificUtility(int arrivalTime, ArrayList<Sanction> sanctionList);
	
	public double utility(int arrivalTime, ArrayList<Sanction> sanctionList) {
		double utility = 0;
		if(arrivalTime == this.getGoalArrivalTime() && sanctionList == null) {
			utility = 1;
		} else{
			//to prevent division by zero
			if(arrivalTime==0) {
				arrivalTime = 1;
			}
			utility = specificUtility(arrivalTime, sanctionList);
		}
		
		return Math.max(0,Math.min(1, utility));
	}
	
	public Agent(String agentID,Node goalNode, int goalArrivalTime, double maxSpeed){
		super(agentID);
		this.goalNode 			= goalNode;
		this.goalArrivalTime 	= goalArrivalTime;
		this.maxSpeed			= maxSpeed;
		expectedArrivalTime 	= goalArrivalTime;
		currentSanctionList 	= new ArrayList<Sanction>();
	}
	
	
	public AgentAction doAction() {
		double bestUtility 		= 0;
		AgentAction bestAction 	= null;
		for(AgentAction action : AgentAction.values()) {
			int time = action.getTime();
			ArrayList<Sanction> sanctions = action.getSanctions();
			double newUtility = utility(time, sanctions);
			if(newUtility > bestUtility) {
				bestAction = action;
				bestUtility = newUtility; 
			}
		}
		return bestAction;
	}
	
	public Node getGoalNode() {
		return goalNode;
	}
	public void setGoalNode(Node goalNode) {
		this.goalNode = goalNode;
	}
	
	public int getGoalArrivalTime() {
		return goalArrivalTime;
	}
	public void setGoalArrivalTime(int goalArrivalTime) {
		this.goalArrivalTime = goalArrivalTime;
	}

	public double getMaxSpeed() {
		return maxSpeed;
	}

	public int getExpectedArrivalTime() {
		return expectedArrivalTime;
	}
	public void setExpectedArrivalTime(int expectedArrivalTime) {
		this.expectedArrivalTime = expectedArrivalTime;
	}

	public ArrayList<Sanction> getCurrentSanctionList() {
		return currentSanctionList;
	}
	public void setCurrentSanctionList(ArrayList<Sanction> currentSanctionList) {
		this.currentSanctionList = currentSanctionList;
	}
	
	
}