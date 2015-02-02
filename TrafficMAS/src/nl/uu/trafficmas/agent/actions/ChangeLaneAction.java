package nl.uu.trafficmas.agent.actions;

import java.util.ArrayList;

import nl.uu.trafficmas.agent.Agent;
import nl.uu.trafficmas.organisation.Sanction;
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
			double leaderDistance) {
		/*
		 * change lane time (no knowledge about RN)
		 * 
		 * lane_remainder       rest_route_length
		 * --------------   +   ----------------- + current_time
		 * next_lane_speed      max_comfy_speed
		 */
		// change lane time:
		
		double finishTime = currentTime;
		double meanOrComfySpeedNextLane = Math.min(meanTravelSpeedNextLane,maxComfySpeed);
		double laneDistRemaining = (currentLaneLength-currentPos);
		double timeSpentOnNextLane = laneDistRemaining/meanOrComfySpeedNextLane;
		finishTime += timeSpentOnNextLane;
		finishTime += routeRemainderLength/maxComfySpeed;
		
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
