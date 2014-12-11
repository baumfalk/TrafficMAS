package nl.uu.trafficmas.agent;

import java.util.ArrayList;

import de.tudresden.sumo.cmd.Vehicle;
import de.tudresden.ws.container.SumoStringList;
import nl.uu.trafficmas.organisation.Sanction;
import nl.uu.trafficmas.roadnetwork.Edge;
import nl.uu.trafficmas.roadnetwork.Node;

public abstract class Agent extends AgentPhysical {
	private final String agentID;
	private Node goalNode;
	private int goalArrivalTime;
	private final double maxSpeed;
	
	private int expectedArrivalTime;
	private ArrayList<Sanction> currentSanctionList;
	public abstract double utility(int arrivalTime, ArrayList<Sanction> sanctionList);
	
	public Agent(String agentID,Node goalNode, int goalArrivalTime, double maxSpeed){
		this.agentID = agentID;
		this.goalNode = goalNode;
		this.goalArrivalTime = goalArrivalTime;
		this.maxSpeed = maxSpeed;
		expectedArrivalTime = goalArrivalTime;
		currentSanctionList = new ArrayList<Sanction>();
	}
	
	
	// Change speed by decelerating to goalVelocity.
	public AgentAction changeVelocity(float goalVelocity){
		return null;
	}
	
	// Change Lane for a certain duration of time.
	public AgentAction changeLane(byte laneIndex, int duration){
		return null;
	}
	
	// Calculate new route according to goalRoadID
	public AgentAction changeRoad(int goalRoadID){
		return null;
	}
	
	public String getAgentID(){
		return agentID;
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