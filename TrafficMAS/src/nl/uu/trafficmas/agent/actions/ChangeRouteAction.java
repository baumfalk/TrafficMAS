package nl.uu.trafficmas.agent.actions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nl.uu.trafficmas.agent.Agent;
import nl.uu.trafficmas.organisation.Sanction;
import nl.uu.trafficmas.roadnetwork.AStar;
import nl.uu.trafficmas.roadnetwork.Edge;
import nl.uu.trafficmas.roadnetwork.Node;
import de.tudresden.sumo.cmd.Vehicle;
import de.tudresden.sumo.util.SumoCommand;
import de.tudresden.ws.container.SumoStringList;

public class ChangeRouteAction extends SumoAgentAction {

	public ChangeRouteAction(int priority) {
		super(priority);
	}

	@Override
	public double getTime(int currentTime, double currentSpeed,
			double meanTravelSpeedNextLane, double currentPos,
			double currentLaneLength, double maxComfySpeed,
			double routeRemainderLength, double leaderAgentSpeed,
			double leaderDistance, Agent agent) {
		Node nearestNode = agent.getRoute()[0].getToNode();
		Map<String,Double>averageTravelTime = agent.getRoadNetwork().getAverageTravelTime();
		
		List<String> newRoute = AStar.findShortestPath(nearestNode, agent.getGoalNode(), agent.getRoadNetwork(), averageTravelTime, agent.getMaxComfySpeed());
		
		// no possible route
		if(newRoute == null) {
			return Double.MAX_VALUE;
		}
		newRoute.add(0, agent.getRoute()[0].getID());
		agent.setPossibleNewRoute(newRoute);
		
		/*
		 * change route time 
		 * 
		 * lane_remainder       
		 * --------------   +   time on each road on the new route + current_time
		 * current_speed      
		 */
		
		double totalTime = currentTime;
		totalTime += (currentLaneLength-currentPos)/currentSpeed;
		for(String edgeID : newRoute) {
			if(edgeID.equals(agent.getRoad().id)) {
				continue;
			}
			totalTime += averageTravelTime.get(edgeID);
		}
		return totalTime;
	}

	@Override
	public ArrayList<Sanction> getSanctions(double maxComfySpeed,
			double velocity) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public SumoCommand getCommand(Agent currentAgent) {
		currentAgent.setRoute(currentAgent.getPossibleNewRoute());
		currentAgent.setPossibleNewRoute(null);
		SumoStringList route = new SumoStringList();
		for(Edge edge : currentAgent.getRoute()){
			route.add(edge.getID());
		}
		return Vehicle.setRoute(currentAgent.agentID, route);
	}
}
