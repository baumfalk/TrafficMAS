package nl.uu.trafficmas.agent.actions;

import java.util.ArrayList;

import nl.uu.trafficmas.organisation.Sanction;

public enum AgentAction {
	ChangeLane,
	ChangeRoad,
	ChangeVelocity5,
	ChangeVelocity10,
	ChangeVelocity20;
	
	
	
	public double getTime(int currentTime, double meanTravelSpeedNextLane, double currentPos, double currentLaneLength, double maxComfySpeed, double routeRemainderLength){ 
		double time;
		switch(this) {
		case ChangeLane:
			time = getChangeLaneTime(currentTime, meanTravelSpeedNextLane, currentPos, currentLaneLength, maxComfySpeed, routeRemainderLength);
			break;
		case ChangeRoad:
			time = getChangeRoadTime();
			break;
		case ChangeVelocity5:
			time = getChangeVelocityTime(5);
			break;
		case ChangeVelocity10:
			time = getChangeVelocityTime(10);
			break;
		case ChangeVelocity20:
			time = getChangeVelocityTime(20);
			break;
		default:
			throw new Error("unsupported action:"+ this);
		}
		return time;
	}

	public ArrayList<Sanction> getSanctions() {
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
		default:
			sanctions = null;
			break;
		
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

	private ArrayList<Sanction> getChangeVelocitySanctions(int speedIncrease) {
		// TODO Auto-generated method stub
		return null;
	}

	private int getChangeVelocityTime(int speedIncrease) {
		// TODO Auto-generated method stub
		return Integer.MAX_VALUE;
	}

	private int getChangeRoadTime() {
		// TODO Auto-generated method stub
		return Integer.MAX_VALUE;
	}

	private double getChangeLaneTime(int currentTime, double meanSpeedNextLane, double currentPos, double currentLaneLength, double maxComfySpeed, double routeRemainderLength) {
		
		/*
		 * change lane time (no knowledge about RN)
		 * 
		 * lane_remainder     rest_route_length
		 * --------------   + ----------------- + current_time
		 * next_lane_speed    max_comfy_speed
		 */
		// change lane time:
		
		double finishTime = currentTime;
		double meanOrComfySpeedNextLane = Math.min(meanSpeedNextLane,maxComfySpeed);
		double timeSpentOnNextLane = (currentLaneLength-currentPos)/meanOrComfySpeedNextLane;
		finishTime += timeSpentOnNextLane;
		finishTime += routeRemainderLength/maxComfySpeed;
		
		return finishTime;
	}
}
