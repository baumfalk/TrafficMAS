package nl.uu.trafficmas.agent.actions;

import java.util.ArrayList;

import nl.uu.trafficmas.agent.Agent;
import nl.uu.trafficmas.organisation.Sanction;
import nl.uu.trafficmas.roadnetwork.Edge;
import nl.uu.trafficmas.roadnetwork.Route;
import de.tudresden.sumo.cmd.Vehicle;
import de.tudresden.sumo.util.SumoCommand;

public class ChangeLaneAction extends SumoAgentAction {
	public static final int OVERTAKE_DURATION = 5;

	public ChangeLaneAction(int priority) {
		super(priority);
	}

	@Override
	public double getTime(int currentTime, double currentSpeed,
			double meanTravelSpeedNextLane, double currentPos,
			double currentLaneLength, double maxComfySpeed,
			double routeRemainderLength, double leaderAgentSpeed,
			double leaderDistance, Agent agent) {
		/*
		 * change lane time (no knowledge about RN)
		 * 
		 * lane_remainder       
		 * --------------   +   time on each road on the remaining route + current_time
		 * next_lane_speed      
		 */
		// change lane time:
	
		double finishTime = currentTime;
		double meanOrComfySpeedNextLane = Math.min(meanTravelSpeedNextLane,maxComfySpeed);
		double laneDistRemaining = (currentLaneLength-currentPos);
		double timeSpentOnNextLane = laneDistRemaining/meanOrComfySpeedNextLane;
		finishTime += timeSpentOnNextLane;
		
		
		Edge[] route = agent.getRoute();
		finishTime += Route.getRouteRemainderTime(route, agent.getRoadNetwork(), agent.getRoad(), agent.getMaxComfySpeed());
		
		return finishTime;
	}

	@Override
	public ArrayList<Sanction> getSanctions(double maxComfySpeed,
			double velocity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SumoCommand getCommand(Agent currentAgent) {
		byte agentLaneIndex = currentAgent.getLane().laneIndex;
		int maxLaneIndex = currentAgent.getRoad().laneList.size()-1;
		// cannot change
		if( agentLaneIndex >= maxLaneIndex) {
			return null;
		}
			
		return Vehicle.changeLane(currentAgent.agentID, (byte) (agentLaneIndex+1) , OVERTAKE_DURATION);
	}

}
