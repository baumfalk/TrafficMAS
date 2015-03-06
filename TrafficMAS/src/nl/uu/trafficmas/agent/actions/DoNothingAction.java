package nl.uu.trafficmas.agent.actions;

import nl.uu.trafficmas.agent.Agent;
import nl.uu.trafficmas.roadnetwork.Edge;
import nl.uu.trafficmas.roadnetwork.Route;
import nl.uu.trafficmas.simulationmodel.AgentData;
import de.tudresden.sumo.util.SumoCommand;

public class DoNothingAction extends SumoAgentAction {

	public DoNothingAction(int priority) {
		super(priority);
	}

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
		double newSpeed = Math.max(0.01,Math.min(currentSpeed, maxComfySpeed));
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
		Edge[] route = agent.getRoute();
		finishTime += Route.getRouteRemainderTime(route, agent.getRoadNetwork(), agent.getRoad(), agent.getMaxComfySpeed());
		
		return finishTime;
	}

	@Override
	public SumoCommand getCommand(Agent currentAgent) {
		return null;
	}

	@Override
	public AgentData getNewAgentState(AgentData agentData) {
		// TODO make new constructor for agentdata so we don't need Object [] leader
		Object [] leader = {agentData.leaderId, agentData.leaderDistance};
		AgentData newData = new AgentData(agentData.id, leader, agentData.position, Math.max(0,agentData.velocity), agentData.roadID, agentData.laneIndex,Agent.deceleration, Agent.acceleration);	
		return newData;
	}

}
