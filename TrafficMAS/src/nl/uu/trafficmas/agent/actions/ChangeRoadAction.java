package nl.uu.trafficmas.agent.actions;

import java.util.ArrayList;

import nl.uu.trafficmas.agent.Agent;
import nl.uu.trafficmas.organisation.Sanction;
import nl.uu.trafficmas.roadnetwork.Edge;
import de.tudresden.sumo.cmd.Vehicle;
import de.tudresden.sumo.util.SumoCommand;
import de.tudresden.ws.container.SumoStringList;

public class ChangeRoadAction extends SumoAgentAction {

	public ChangeRoadAction(int priority) {
		super(priority);
	}

	@Override
	public double getTime(int currentTime, double currentSpeed,
			double meanTravelSpeedNextLane, double currentPos,
			double currentLaneLength, double maxComfySpeed,
			double routeRemainderLength, double leaderAgentSpeed,
			double leaderDistance) {
		// TODO implement this
		return Double.POSITIVE_INFINITY;
	}

	@Override
	public ArrayList<Sanction> getSanctions(double maxComfySpeed,
			double velocity) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public SumoCommand getCommand(Agent currentAgent) {
		generateNewRoute(currentAgent);
		SumoStringList route = new SumoStringList();
		for(Edge edge : currentAgent.getRoute()){
			route.add(edge.getID());
		}
		return Vehicle.setRoute(currentAgent.agentID, route);
	}
	
	public void generateNewRoute(Agent currentAgent){
		/*
		 * ArrayList<String> newRoute = new ArrayList<String>();
		 * 
		 * 
		 * 
		 * A* algorithm goes here, generates a new route and returns a list of edge ID's
		 * 
		 * 
		 * 
		 * currentAgent.setRoute(newRoute);
		 */	
	}
}
