package nl.uu.trafficmas.agent;

import nl.uu.trafficmas.roadnetwork.Node;
import nl.uu.trafficmas.roadnetwork.RoadNetwork;
import nl.uu.trafficmas.roadnetwork.Route;

public enum AgentProfileType {
	Normal,
	HotShot,
	OldLady,
	SUMODefault;
	
	public Agent toAgent(String agentID, Node goalNode, Route route, RoadNetwork roadNetwork, int goalArrivalTime, double maxSpeed) {
		Agent agent = null;
		double maxComfySpeed = this.getMaxComfortableDrivingSpeed(maxSpeed);
		switch(this) {
		case Normal:
			agent = new NormalAgent(agentID, goalNode, route, roadNetwork, goalArrivalTime, maxSpeed,maxComfySpeed);
			break;
		case OldLady:
			agent = new OldLadyAgent(agentID, goalNode, route, roadNetwork, goalArrivalTime, maxSpeed,maxComfySpeed);
			break;
		case HotShot:
			agent = new HotShotAgent(agentID, goalNode, route, roadNetwork, goalArrivalTime, maxSpeed,maxComfySpeed);
			break;
		case SUMODefault:
			agent = new SUMODefaultAgent(agentID, null, route, roadNetwork, 0, Double.MAX_VALUE, Double.MAX_VALUE);
		}
		return agent;
	}
	
	public double getMaxComfortableDrivingSpeed(double maxSpeed) {
		double maxComfySpeed = 0;
		switch(this) {
		case Normal:
			maxComfySpeed = maxSpeed * 0.8;
			break;
		case OldLady:
			maxComfySpeed = maxSpeed * 0.6;
			break;
		case HotShot:
			maxComfySpeed = maxSpeed;
			break;
		case SUMODefault:
			maxComfySpeed = maxSpeed * 20;
			break;
		}
		return maxComfySpeed;
	}
	public int goalArrivalTime(int startTime, int minimalTravelTime) {
		int goalArrivalTime = 0;
		switch(this) {
		case Normal:
			// normally we are kinda in a hurry
			goalArrivalTime =  startTime + minimalTravelTime;
			break;
		case OldLady:
			// No hurry, my dear
			goalArrivalTime =  startTime + minimalTravelTime;
			break;
		case HotShot:
			//OUTTATHEWAY
			goalArrivalTime =  startTime + minimalTravelTime;
		case SUMODefault:
			//DONTCARE
			goalArrivalTime =  0;
		}
		return goalArrivalTime;
	}
	
	public static AgentProfileType getAgentProfileType(String agentProfileTypeString) {
		AgentProfileType agent = null;
		switch(agentProfileTypeString) {
		case "Normal":
			agent = Normal;
			break;
		case "OldLady":
			agent = OldLady;
			break;
		case "HotShot":
			agent = HotShot;
			break;
		case "SUMODefault":
			agent = SUMODefault;
			break;
		}
		return agent;
	}

}
