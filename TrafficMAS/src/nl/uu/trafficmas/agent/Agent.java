package nl.uu.trafficmas.agent;

import java.util.ArrayList;

import nl.uu.trafficmas.agent.actions.AgentAction;
import nl.uu.trafficmas.organisation.Sanction;
import nl.uu.trafficmas.roadnetwork.Edge;
import nl.uu.trafficmas.roadnetwork.Node;
import nl.uu.trafficmas.roadnetwork.Road;

public abstract class Agent extends AgentPhysical {
	private Node goalNode;
	private int goalArrivalTime;
	private final double maxSpeed;
	
	private int expectedArrivalTime;
	private ArrayList<Sanction> currentSanctionList;
	private Edge[] currentRoute;
	private ArrayList<Double> expectedTravelTimePerRoad;
	
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
	
	public Agent(String agentID,Node goalNode,Edge[] routeEdges, int goalArrivalTime, double maxSpeed){
		super(agentID);
		this.goalNode 			= goalNode;
		this.goalArrivalTime 	= goalArrivalTime;
		this.maxSpeed			= maxSpeed;
		expectedArrivalTime 	= goalArrivalTime;
		this.currentRoute = routeEdges;
		this.expectedTravelTimePerRoad = new ArrayList<>();
		for(Edge edge : routeEdges) {
			double time = edge.getRoad().length/maxSpeed;
			expectedTravelTimePerRoad.add(time);
		}
		currentSanctionList 	= new ArrayList<Sanction>();
	}
	
	public abstract int goalArrivalTime(int startTime, int minimalTravelTime);
	
	public AgentAction doAction(int currentTime, double meanSpeedNextLane) {
		// Only do an action if it improves our situation
		double bestUtility 		= utility(expectedArrivalTime,currentSanctionList);
		AgentAction bestAction 	= null;
		for(AgentAction action : AgentAction.values()) {
			int time = action.getTime(currentTime, meanSpeedNextLane, this.distance, this.road.length, expectedTravelTimePerRoad);
			ArrayList<Sanction> sanctions = action.getSanctions();
			double newUtility = utility(time, sanctions);
			if(newUtility > bestUtility) {
				bestAction = action;
				bestUtility = newUtility; 
			}
		}
		if(bestAction != null) {
			System.out.println(this.agentID + " will do " + bestAction);
		} else {
			System.out.println(this.agentID + " will do nothing");
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