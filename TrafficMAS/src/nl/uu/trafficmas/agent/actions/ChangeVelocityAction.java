package nl.uu.trafficmas.agent.actions;

import java.util.ArrayList;
import java.util.Map;

import nl.uu.trafficmas.agent.Agent;
import nl.uu.trafficmas.organisation.Sanction;
import nl.uu.trafficmas.roadnetwork.Edge;
import de.tudresden.sumo.cmd.Vehicle;
import de.tudresden.sumo.util.SumoCommand;

public class ChangeVelocityAction extends SumoAgentAction {

	public ChangeVelocityAction(int priority) {
		super(priority);
	}

	protected double speedIncrease;
	@Override
	public double getTime(int currentTime, double currentSpeed,
			double meanTravelSpeedNextLane, double currentPos,
			double currentLaneLength, double maxComfySpeed,
			double routeRemainderLength, double leaderAgentSpeed,
			double leaderDistance, Agent agent) {
		/*
		 * increase velocity time
		 * lane_remainder                     
		 * ------------------------------- + time on each road on the remaining route + current_time
		 * (currentSpeed + speedIncrease)     
		 */
		double laneDistRemaining = (currentLaneLength-currentPos);
		double finishTime = currentTime;
		double newSpeed = Math.min(currentSpeed + speedIncrease, maxComfySpeed);
		//  normally this is the time we spent on a the rest of the lane.
		double timeSpentOnLane = laneDistRemaining/newSpeed;
		
		// but if we have someone in front of us who drives slower...
		if(leaderAgentSpeed >= 0 && leaderDistance >= 0 && newSpeed > leaderAgentSpeed && laneDistRemaining > leaderDistance) {
			// new speed is higher than speed of leader
			// also, distance is smaller than remaining lane distance.
			// assumption: we can drive distance between us and leader
			// with our speed. After that it's their speed.
			// New lane: assume max_comfy_speed
			
			double timeSpentDrivingOwnSpeed 	= leaderDistance/newSpeed;
			double laneDistRemainingForLeader	= laneDistRemaining-leaderDistance;
			double timeSpentLaneRemainder		= laneDistRemainingForLeader/leaderAgentSpeed; 
			timeSpentOnLane = timeSpentDrivingOwnSpeed + timeSpentLaneRemainder;
		}
		
		finishTime += timeSpentOnLane;
		Map<String,Double>roadBestLaneAverageTime = agent.getRoadNetwork().getRoadBestLaneAverageTravelTime();
		Edge[] route = agent.getRoute();
		for(Edge edge : route) {
			if(edge.getRoad().equals(agent.getRoad())) {
				continue;
			}
			finishTime += roadBestLaneAverageTime.get(edge.getID());
		}
		
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
		double velocity = currentAgent.getVelocity();
		double maxComfySpeed = currentAgent.getMaxComfySpeed();
		return Vehicle.setMaxSpeed(currentAgent.agentID, Math.min(velocity+speedIncrease, maxComfySpeed));
	}

}
