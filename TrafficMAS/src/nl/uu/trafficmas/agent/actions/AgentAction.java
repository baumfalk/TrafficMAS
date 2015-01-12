package nl.uu.trafficmas.agent.actions;

import java.util.ArrayList;

import nl.uu.trafficmas.organisation.Sanction;

public enum AgentAction {
	ChangeLane,
	ChangeRoad,
	ChangeVelocity5,
	ChangeVelocity10,
	ChangeVelocity20,
	ChangeVelocityMax;
	
	private double utility;

	public double getTime(int currentTime, double currentSpeed, double meanTravelSpeedNextLane, double currentPos, double currentLaneLength, double maxComfySpeed, double routeRemainderLength, double leaderAgentSpeed, double leaderDistance){ 
		double time;
		switch(this) {
		case ChangeLane:
			time = getChangeLaneTime(currentTime, meanTravelSpeedNextLane, currentPos, currentLaneLength, maxComfySpeed, routeRemainderLength);
			break;
		case ChangeRoad:
			time = getChangeRoadTime();
			break;
		case ChangeVelocity5:
			time = getChangeVelocityTime(5, currentSpeed, currentTime, currentPos, currentLaneLength, maxComfySpeed, routeRemainderLength,leaderAgentSpeed,leaderDistance);
			break;
		case ChangeVelocity10:
			time = getChangeVelocityTime(10, currentSpeed, currentTime, currentPos, currentLaneLength, maxComfySpeed, routeRemainderLength,leaderAgentSpeed,leaderDistance);
			break;
		case ChangeVelocity20:
			time = getChangeVelocityTime(20, currentSpeed, currentTime, currentPos, currentLaneLength, maxComfySpeed, routeRemainderLength,leaderAgentSpeed,leaderDistance);
			break;
		case ChangeVelocityMax:
			time = getChangeVelocityTime(maxComfySpeed-currentSpeed, currentSpeed, currentTime, currentPos, currentLaneLength, maxComfySpeed, routeRemainderLength,leaderAgentSpeed,leaderDistance);
			break;
		default:
			throw new Error("unsupported action:"+ this);
		}
		return time;
	}

	public ArrayList<Sanction> getSanctions(double maxComfySpeed, double velocity) {
		ArrayList<Sanction> sanctions;
		switch(this) {
		case ChangeLane:
			sanctions = getChangeLaneSanctions();
			break;
		case ChangeRoad:
			sanctions = getChangeRoadSanctions();
			break;
		case ChangeVelocity5:
			sanctions = getChangeVelocitySanctions(5);
			break;
		case ChangeVelocity10:
			sanctions = getChangeVelocitySanctions(10);
			break;
		case ChangeVelocity20:
			sanctions = getChangeVelocitySanctions(20);
			break;
		case ChangeVelocityMax:
			sanctions = getChangeVelocitySanctions(maxComfySpeed-velocity);
			break;
		default:
			throw new Error("unsupported action:"+ this);		
		}
		return sanctions;
	}
	
	
	private ArrayList<Sanction> getChangeLaneSanctions() {
		// TODO Auto-generated method stub
		return null;
	}

	private ArrayList<Sanction> getChangeRoadSanctions() {
		// TODO Auto-generated method stub
		return null;
	}

	private ArrayList<Sanction> getChangeVelocitySanctions(double d) {
		// TODO Auto-generated method stub
		return null;
	}

	private double getChangeVelocityTime(double speedIncrease, double currentSpeed, int currentTime, double currentPos, double currentLaneLength, double maxComfySpeed, double routeRemainderLength, double leaderAgentSpeed, double leaderDistance) {
		// TODO also incorporate leader vehicle?
		/*
		 * increase velocity time
		 * lane_remainder                     rest_route_length
		 * ------------------------------- + -----------------   + current_time
		 * (currentSpeed + speedIncrease)     max_comfy_speed
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
		finishTime += routeRemainderLength/maxComfySpeed;
		return finishTime;
	}

	private int getChangeRoadTime() {
		// TODO Auto-generated method stub
		return Integer.MAX_VALUE;
	}

	private double getChangeLaneTime(int currentTime, double meanSpeedNextLane, double currentPos, double currentLaneLength, double maxComfySpeed, double routeRemainderLength) {
		
		/*
		 * change lane time (no knowledge about RN)
		 * 
		 * lane_remainder       rest_route_length
		 * --------------   +   ----------------- + current_time
		 * next_lane_speed      max_comfy_speed
		 */
		// change lane time:
		
		double finishTime = currentTime;
		double meanOrComfySpeedNextLane = Math.min(meanSpeedNextLane,maxComfySpeed);
		double laneDistRemaining = (currentLaneLength-currentPos);
		double timeSpentOnNextLane = laneDistRemaining/meanOrComfySpeedNextLane;
		finishTime += timeSpentOnNextLane;
		finishTime += routeRemainderLength/maxComfySpeed;
		
		return finishTime;
	}

	public void setUtility(double newUtility) {
		utility = newUtility;
	}
	
	public double getUtility(){
		return utility;
	}

	public static int compare(AgentAction action1, AgentAction action2) {
		if(action1.getUtility() > action2.getUtility())
    		return -1;
    	else if(action1.getUtility() < action2.getUtility()) {
    		return 1;
    	} else{
    		
    		
    		if(action1 == AgentAction.ChangeLane) {
    			if(action2 != AgentAction.ChangeRoad) {
    				return 1;
    			} else {
    				return -1;
    			}
    		}
    		else if(action1 == AgentAction.ChangeRoad) {
    			return 1;
    		} 
    		else if(action1 == AgentAction.ChangeVelocity5) {
    			if(action2 == AgentAction.ChangeVelocity10 || action2==AgentAction.ChangeVelocity20 || action2==AgentAction.ChangeVelocityMax) {
    				return 1;
    			} else{
    				return -1;
    			}
    		}
    		
    		else if(action1 == AgentAction.ChangeVelocity10) {
    			if(action2==AgentAction.ChangeVelocity20 || action2==AgentAction.ChangeVelocityMax) {
    				return 1;
    			} else{
    				return -1;
    			}
    		}
    		
    		else if(action1 == AgentAction.ChangeVelocity20) {
    			if(action2==AgentAction.ChangeVelocityMax) {
    				return 1;
    			} else{
    				return -1;
    			}
    		}
    		else if (action1 == AgentAction.ChangeVelocityMax) {
    			return -1;
    		}
    	}
        return 0;
    }
}
