package nl.uu.trafficmas.agent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.uu.trafficmas.agent.actions.AgentAction;
import nl.uu.trafficmas.agent.actions.ChangeLaneAction;
import nl.uu.trafficmas.agent.actions.DoNothingAction;
import nl.uu.trafficmas.agent.actions.SetVelocityXAction;
import nl.uu.trafficmas.exception.DistanceLargerThanRoadException;
import nl.uu.trafficmas.norm.NormInstantiation;
import nl.uu.trafficmas.norm.Sanction;
import nl.uu.trafficmas.roadnetwork.Edge;
import nl.uu.trafficmas.roadnetwork.Node;
import nl.uu.trafficmas.roadnetwork.Road;
import nl.uu.trafficmas.roadnetwork.RoadNetwork;
import nl.uu.trafficmas.roadnetwork.Route;
import nl.uu.trafficmas.simulationmodel.AgentData;

public abstract class Agent extends AgentPhysical {
	private Node 					goalNode;
	private int 					goalArrivalTime;
	protected final double 			maxSpeed;
	
	private double 					expectedArrivalTime;
	private List<Sanction> 			currentSanctionList;
	private List<NormInstantiation> currentNormInstList;
	
	private String 					currentRouteID;
	private Edge[] 					currentRouteEdges;
	private ArrayList<Double> 		expectedTravelTimePerRoad;
	private double 					maxComfySpeed;
	
	private double 					leaderAgentSpeed;
	private double 					leaderDistance;
	private RoadNetwork 			roadNetwork;
	private List<String> 			possibleNewRoute;
	
	public final static double DEFAULT_MAX_SPEED = (150/3.6);
	public final static double acceleration = 1;
	public final static double deceleration = -3;
	public abstract double specificUtility(double time, List<Sanction> sanctionList);
	
	/**
	 * Calculates and returns the Utility according to 'arrivalTime' and 'sanctionList'
	 * @param time
	 * @param sanctionList
	 * @return a double value that is a value between and including 0.0 and 1.0.
	 */
	public double utility(double time, List<Sanction> sanctionList) {
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
		currentSanctionList = new ArrayList<Sanction>();
		currentNormInstList	= new ArrayList<NormInstantiation>();	
	}
	
	
	/**
	 * Returns, according to the utility, the best AgentAction object for a specific agent.
	 * @param agentClearInst 
	 * @param agentInst 
	 * @param agentSanc 
	 * @return an AgentAction with the highest utility, or if no advantage can be gained from performing an action, null.
	 */
	public AgentAction doAction(int currentTime, List<Sanction> agentSanc, List<NormInstantiation> agentInst, List<NormInstantiation> agentClearInst) {

		// add the achieved sanctions
		if(agentSanc != null)
			currentSanctionList.addAll(agentSanc);
		if(agentClearInst!= null)
			currentNormInstList.removeAll(agentClearInst);
		if(agentInst != null)
			currentNormInstList.addAll(agentInst);
		
		// Only do an action if it improves our situation
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
		AgentData ad = this.getAgentData();
		for(AgentAction action : AgentAction.values()) {
			
			if(action instanceof ChangeLaneAction && !this.lane.hasLeftLane())
				continue;
			if(action instanceof SetVelocityXAction && currentNormInstList.isEmpty())
				continue;
			
			double time 			= 0;
			double dist 			= this.distance;
			double roadLength 		= this.road.length;
			double maxComfySp 		= this.maxComfySpeed;
			double leaderAgentSpeed = this.leaderAgentSpeed;
			double leaderDistance 	= this.leaderDistance;
			double vel 				= 0;
			if(action instanceof SetVelocityXAction) {
				// find best speed (==X) from norm instantiations
				double bestX = 0;
				double bestXUtil = Double.NEGATIVE_INFINITY;
				Map<String,String> parameters = new HashMap<String,String>();
				
				for (NormInstantiation ni : currentNormInstList) {
					//TODO also include lane, position etc in this
					vel = ni.goal().velocity;
					parameters.put("X", Double.toString(vel));
					action.setParameters(parameters);
					if(vel != -1) {
						
						time = action.getTime(currentTime,vel, leftLaneSpeed, dist, roadLength, maxComfySp, routeRemainderLength, leaderAgentSpeed, leaderDistance,this);

						List<Sanction> sanctions = null;
						if(!currentNormInstList.isEmpty()) {
							AgentData newAD = action.getNewAgentState(ad);
							sanctions = action.getSanctions(newAD,currentNormInstList);
						}
						double newUtility 				= utility(time, sanctions);
						if(newUtility > bestXUtil) {
							bestX = vel;
							bestXUtil = newUtility;
						}
					}
				}
				parameters.put("X", Double.toString(bestX));
				AgentAction newAction = new SetVelocityXAction(action.priority);
				newAction.setParameters(parameters);
				newAction.setUtility(bestXUtil);
				
				actionList.add(newAction);
				
			} else {
				vel = velocity;
				time = action.getTime(currentTime,vel, leftLaneSpeed, dist, roadLength, maxComfySp, routeRemainderLength, leaderAgentSpeed, leaderDistance,this);

				List<Sanction> sanctions = null;
				if(!currentNormInstList.isEmpty()) {
					AgentData newAD = action.getNewAgentState(ad);
					sanctions = action.getSanctions(newAD,currentNormInstList);
				}
				double newUtility 				= utility(time, sanctions);
				action.setUtility(newUtility);
				actionList.add(action);
			}
		}
		// sort the actions by their utility
		Collections.sort(actionList, new Comparator<AgentAction>() {
	        public int compare(AgentAction action1, AgentAction action2) {
	        	return AgentAction.compare(action1, action2);
	        }
	    });
		bestAction = actionList.get(0);
		
		if(bestAction instanceof DoNothingAction)
			bestAction = null;
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

	public List<Sanction> getCurrentSanctionList() {
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

	public void setRoute(List<String> newRoute){
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
	
	public RoadNetwork getRoadNetwork() {
		return roadNetwork;
	}

	public void setPossibleNewRoute(List<String> newRoute) {
		possibleNewRoute = newRoute;
	}

	public List<String> getPossibleNewRoute() {
		return possibleNewRoute;
	}
	
	public AgentData getAgentData() {
		Object [] leader = {null, this.leaderDistance};
		AgentData newData = new AgentData(this.agentID, leader, this.distance, (this.velocity), this.road.id, this.lane.laneIndex, Agent.deceleration, Agent.acceleration);
		return newData;
	}
}