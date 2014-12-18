package nl.uu.trafficmas.agent.actions;

import java.util.ArrayList;

import nl.uu.trafficmas.organisation.Sanction;

public enum AgentAction {
	ChangeLane,
	ChangeRoad,
	ChangeVelocity;
	
	
	public int getTime(int currentTime, double meanSpeedNextLane, double currentPos, double laneLength, ArrayList<Double> meanTimeForRouteRoads){ 
		int time;
		switch(this) {
		case ChangeLane:
			time = getChangeLaneTime(currentTime, meanSpeedNextLane,currentPos,laneLength,meanTimeForRouteRoads);
			break;
		case ChangeRoad:
			time = getChangeRoadTime();
			break;
		case ChangeVelocity:
			time = getChangeVelocityTime();
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
		case ChangeVelocity:
			sanctions = getChangeVelocitySanctions();
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
		case ChangeVelocity:
			name = "ChangeVelocity";
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

	private ArrayList<Sanction> getChangeVelocitySanctions() {
		// TODO Auto-generated method stub
		return null;
	}

	private int getChangeVelocityTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	private int getChangeRoadTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	private int getChangeLaneTime(int currentTime, double meanSpeedNextLane, double currentPos, double laneLength, ArrayList<Double> meanTimeForRouteRoads) {
		
		double time = currentTime + (laneLength-currentPos)/meanSpeedNextLane;
		
		for(Double timeOnEdge : meanTimeForRouteRoads) {
			time += timeOnEdge;
		}
		
		return (int) Math.round(time);
	}
}
