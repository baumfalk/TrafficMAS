package nl.uu.trafficmas.agent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import nl.uu.trafficmas.agent.actions.AgentAction;
import nl.uu.trafficmas.organisation.Sanction;
import nl.uu.trafficmas.roadnetwork.Edge;
import nl.uu.trafficmas.roadnetwork.Node;
import nl.uu.trafficmas.roadnetwork.Road;
import nl.uu.trafficmas.roadnetwork.RoadNetwork;
import nl.uu.trafficmas.roadnetwork.Route;

public abstract class Agent extends AgentPhysical {
	private Node goalNode;
	private int goalArrivalTime;
	protected final double maxSpeed;
	
	private double expectedArrivalTime;
	private ArrayList<Sanction> currentSanctionList;
	private String currentRouteID;
	private Edge[] currentRouteEdges;
	private ArrayList<Double> expectedTravelTimePerRoad;
	private double maxComfySpeed;
	private double leaderAgentSpeed;
	private double leaderDistance;
	private RoadNetwork roadNetwork;
	
	public final static double DEFAULT_MAX_SPEED = 20;
	
	public abstract double specificUtility(double time, ArrayList<Sanction> sanctionList);
	
	/**
	 * Calculates and returns the Utility according to 'arrivalTime' and 'sanctionList'
	 * @param time
	 * @param sanctionList
	 * @return a double value that is a value between and including 0.0 and 1.0.
	 */
	public double utility(double time, ArrayList<Sanction> sanctionList) {
		double utility = 0;
		if(Math.round(time) == Math.round(this.getGoalArrivalTime()) && sanctionList == null) {
			utility = 1;
		} else{
			//to prevent division by zero
			if(time==0) {
				time = 1;
			}
			utility = specificUtility(time, sanctionList);
		}
		
		return Math.max(0,Math.min(1, utility));
	}
	
	public Agent(String agentID,Node goalNode,Route route, RoadNetwork roadNetwork, int goalArrivalTime, double maxSpeed, double maxComfySpeed){
		super(agentID);
		this.goalNode 					= goalNode;
		this.goalArrivalTime 			= goalArrivalTime;
		this.maxSpeed					= maxSpeed;
		this.maxComfySpeed 				= maxComfySpeed;
		this.expectedArrivalTime 		= goalArrivalTime;
		this.currentRouteID				= route.routeID;
		this.currentRouteEdges 			= route.getRoute();
		this.roadNetwork				= roadNetwork;
		this.expectedTravelTimePerRoad 	= new ArrayList<>();
		for(Edge edge : route.getRoute()) {
			double time = edge.getRoad().length/maxComfySpeed;
			expectedTravelTimePerRoad.add(time);
		}
		currentSanctionList 	= new ArrayList<Sanction>();
	}
	
	
	/**
	 * Returns, according to the utility, the best AgentAction object for a specific agent.
	 * @return an AgentAction with the highest utility, or if no advantage can be gained from performing an action, null.
	 */
	public AgentAction doAction(int currentTime) {
		// Only do an action if it improves our situation
		double noActionUtility	= utility(expectedArrivalTime,currentSanctionList);
		AgentAction bestAction 	= null;
		// Set currentRoadID value
		double routeRemainderLength = Route.getRouteRemainderLength(this.currentRouteEdges, this.road);
		
		// Loop through all AgentAction objects and calculate utility for each.
		// If no action returns a better utility than the one we currently have, bestAction remains null.
		List<AgentAction> actionList = new ArrayList<AgentAction>();
		double leftLaneSpeed = 0;
		if(this.lane.hasLeftLane()) {
			leftLaneSpeed = this.lane.getLeftLane().getLaneMeanSpeed();
		}
		for(AgentAction action : AgentAction.values()) {
			
			double time = action.getTime(currentTime,velocity, leftLaneSpeed, this.distance, this.road.length, this.maxComfySpeed, routeRemainderLength, this.leaderAgentSpeed, this.leaderDistance);
			ArrayList<Sanction> sanctions 	= action.getSanctions(maxComfySpeed, velocity);
			double newUtility 				= utility(time, sanctions);
			action.setUtility(newUtility);
			actionList.add(action);
		}
		// sort the actions by their utility
		Collections.sort(actionList, new Comparator<AgentAction>() {
	        public int compare(AgentAction action1, AgentAction action2) {
	        	return AgentAction.compare(action1, action2);
	        }
	    });
		// only do an action if it is strictly better than doing nothing.
		if(actionList.get(0).getUtility() > noActionUtility) {
			bestAction = actionList.get(0);
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

	public double getExpectedArrivalTime() {
		return expectedArrivalTime;
	}
	public void setExpectedArrivalTime(double expectedArrivalTime2) {
		this.expectedArrivalTime = expectedArrivalTime2;
	}

	public ArrayList<Sanction> getCurrentSanctionList() {
		return currentSanctionList;
	}
	public void setCurrentSanctionList(ArrayList<Sanction> currentSanctionList) {
		this.currentSanctionList = currentSanctionList;
	}
	
	public Edge[] getRoute() {
		return currentRouteEdges;
	}
	
	public String getRouteID(){
		return currentRouteID;
	}
	
	@Override
	public void setRoad(Road road) {
		super.setRoad(road);
		if(road == null)
			return;
		int currentRoadID = 0;
		for(;currentRoadID<currentRouteEdges.length;currentRoadID++) {
			if(this.road.id.equals(currentRouteEdges[currentRoadID].getRoad().id)) {
				break;
			}
		}
		//update the currentRoute
		Edge[] tempRoute = new Edge[currentRouteEdges.length-currentRoadID];
		for(int i=currentRoadID; i<currentRouteEdges.length;i++) {
			tempRoute[i-currentRoadID] = currentRouteEdges[i];
		}
		currentRouteEdges = tempRoute;
	}

	public void setRoute(ArrayList<String> newRoute){
		this.currentRouteEdges = new Edge[newRoute.size()];
		for(int i=0; i<newRoute.size();i++){
			this.currentRouteEdges[i] = roadNetwork.getEdge(newRoute.get(i));
		}
	}
	
	public double getMaxComfySpeed() {
		return maxComfySpeed;
	}
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName()+" " +agentID;
	}

	public void setLeader(double leaderAgent, double leaderDistance) {
		// TODO Auto-generated method stub
		this.leaderAgentSpeed 	= leaderAgent;
		this.leaderDistance 	= leaderDistance;
	}
	
	public boolean hasLeader() {
		return (this.leaderAgentSpeed >=0) && (this.leaderDistance >= 0);
	}

	public List<String> getRouteStringList() {
		List<String> list = new ArrayList<String>();
		for(Edge edge : currentRouteEdges) {
			list.add(edge.getID());
		}
		return list;
	}
}