package nl.uu.trafficmas.agent.actions;

import java.util.ArrayList;

import nl.uu.trafficmas.organisation.Sanction;

public enum AgentAction {
	ChangeLane,
	ChangeRoad,
	ChangeVelocity5,
	ChangeVelocity10,
	ChangeVelocity20;
	
	
	
	public int getTime(int currentTime, double meanSpeedNextLane, double currentPos, double laneLength, ArrayList<Double> meanTimeForRouteRoads){ 
		int time;
		switch(this) {
		case ChangeLane:
			time = getChangeLaneTime(currentTime, meanSpeedNextLane,currentPos,laneLength,meanTimeForRouteRoads);
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
			time = -1;
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
			sanctions = getChangeVelocitySanctions(5);
			break;
		case ChangeVelocity20:
			sanctions = getChangeVelocitySanctions(5);
			break;
		default:
			sanctions = null;
			break;
		
		}
		return sanctions;
	}
	
	public String getName(){
		String name;
		switch(this) {
		case ChangeLane:
			name = "ChangeLane";
			break;
		case ChangeRoad:
			name = "ChangeRoad";
			break;
		case ChangeVelocity5:
			name = "ChangeVelocity5";
			break;
		case ChangeVelocity10:
			name = "ChangeVelocity10";
			break;
		case ChangeVelocity20:
			name = "ChangeVelocity20";
			break;
		default:
			name = "No action";
		}
		return name;
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

	private int getChangeLaneTime(int currentTime, double meanSpeedNextLane, double currentPos, double laneLength, ArrayList<Double> meanTimeForRouteRoads) {
		
		double finishTime = currentTime;
		double timeSpentOnNextLane = (laneLength-currentPos)/meanSpeedNextLane;
		finishTime += timeSpentOnNextLane;
		for(Double timeOnEdge : meanTimeForRouteRoads) {
			finishTime += timeOnEdge;
		}
		
		return (int) Math.round(finishTime);
	}
}
