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
	protected final double maxSpeed;
	
	private int expectedArrivalTime;
	private ArrayList<Sanction> currentSanctionList;
	private Edge[] currentRoute;
	private ArrayList<Double> expectedTravelTimePerRoad;
	private double maxComfySpeed;
	private AgentProfileType agentProfileType;
	private int currentTime;
	
	public final static double DEFAULT_MAX_SPEED = 20;
	
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
	
	public AgentProfileType getAgentType(){

		return agentProfileType;
	}
	
	public Agent(String agentID,Node goalNode,Edge[] routeEdges, int goalArrivalTime, double maxSpeed, AgentProfileType agentProfileType, double maxComfySpeed, int currentTime){
		super(agentID);
		this.goalNode 					= goalNode;
		this.goalArrivalTime 			= goalArrivalTime;
		this.maxSpeed					= maxSpeed;
		this.maxComfySpeed 				= maxComfySpeed;
		this.expectedArrivalTime 		= goalArrivalTime;
		this.currentRoute 				= routeEdges;
		this.agentProfileType 			= agentProfileType;
		this.expectedTravelTimePerRoad 	= new ArrayList<>();
		this.currentTime				= currentTime;
		for(Edge edge : routeEdges) {
			double time = edge.getRoad().length/maxComfySpeed;
			expectedTravelTimePerRoad.add(time);
		}
		currentSanctionList 	= new ArrayList<Sanction>();
	}
	
	public int getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(int currentTime) {
		this.currentTime = currentTime;
	}

	public AgentAction doAction() {
		// Only do an action if it improves our situation
		double bestUtility 		= utility(expectedArrivalTime,currentSanctionList);
		AgentAction bestAction 	= null;
		
		int currentRoadID = 0;
		for(;currentRoadID<currentRoute.length;currentRoadID++) {
			if(currentRoute[currentRoadID].getRoad().id.equals(this.road.id)) {
				break;
			}
		}
		
		for(AgentAction action : AgentAction.values()) {
			int time 						= action.getTime(currentTime, this.lane.getMeanTravelTime(), this.distance, this.road.length, currentRoadID, expectedTravelTimePerRoad);
			ArrayList<Sanction> sanctions 	= action.getSanctions();
			double newUtility 				= utility(time, sanctions);
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
	
	public Edge [] getRoute() {
		return currentRoute;
	}
	@Override
	public void setRoad(Road road) {
		super.setRoad(road);
		int currentRoadID = 0;
		for(;currentRoadID<currentRoute.length;currentRoadID++) {
			if(this.road.id.equals(currentRoute[currentRoadID].getRoad().id)) {
				break;
			}
		}
		//update the currentRoute
		Edge[] tempRoute = new Edge[currentRoute.length-currentRoadID];
		for(int i=currentRoadID; i<currentRoute.length;i++) {
			tempRoute[i-currentRoadID] = currentRoute[i];
		}
		currentRoute = tempRoute;
	}
	
	public double getMaxComfySpeed() {
		return maxComfySpeed;
	}
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName()+" " +agentID;
	}
}