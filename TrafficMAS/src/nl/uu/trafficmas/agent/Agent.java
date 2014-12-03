package nl.uu.trafficmas.agent;

import java.util.ArrayList;

import nl.uu.trafficmas.organisation.Sanction;
import nl.uu.trafficmas.roadnetwork.Node;

public abstract class Agent extends AgentPhysical {
	private Node goalNode;
	private int goalArrivalTime;
	
	private int expectedArrivalTime;
	private ArrayList<Sanction> currentSanctionList;
	public abstract double utility(int arrivalTime, ArrayList<Sanction> sanctionList);
}