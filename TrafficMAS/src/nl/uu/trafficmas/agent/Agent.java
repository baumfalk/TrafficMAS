package nl.uu.trafficmas.agent;

import java.util.ArrayList;

import de.tudresden.sumo.cmd.Vehicle;
import de.tudresden.sumo.util.SumoCommand;
import de.tudresden.ws.container.SumoStringList;
import nl.uu.trafficmas.organisation.Sanction;
import nl.uu.trafficmas.roadnetwork.Node;

public abstract class Agent extends AgentPhysical {
	private String agentID;
	private Node goalNode;
	private int goalArrivalTime;
	
	private int expectedArrivalTime;
	private ArrayList<Sanction> currentSanctionList;
	public abstract double utility(int arrivalTime, ArrayList<Sanction> sanctionList);
	
	// Change speed by decelerating to goalVelocity.
	public SumoCommand changeVelocity(float goalVelocity){
		return Vehicle.setSpeed(agentID, goalVelocity);
	}
	
	// Change Lane for a certain duration of time.
	public SumoCommand changeLane(byte laneIndex, int duration){
		return Vehicle.changeLane(agentID, laneIndex, duration);
	}
	
	// Calculate new route according to goalRoadID
	public SumoCommand changeRoad(int goalRoadID){
		SumoStringList edgeList = new SumoStringList();
		return Vehicle.setRoute(agentID, edgeList);
	}
}