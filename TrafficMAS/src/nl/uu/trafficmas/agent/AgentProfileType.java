package nl.uu.trafficmas.agent;

import nl.uu.trafficmas.roadnetwork.Edge;
import nl.uu.trafficmas.roadnetwork.Node;

public enum AgentProfileType {
	Normal,
	PregnantWoman,
	OldLady;
	
	public Agent toAgent(String agentID, Node goalNode, Edge[] routeEdges, int goalArrivalTime, double maxSpeed,int currentTime) {
		Agent agent = null;
		double maxComfySpeed = this.getMaxComfortableDrivingSpeed(maxSpeed);
		switch(this) {
		case Normal:
			agent = new NormalAgent(agentID, goalNode, routeEdges, goalArrivalTime, maxSpeed,maxComfySpeed,currentTime);
			break;
		case OldLady:
			agent = new OldLadyAgent(agentID, goalNode, routeEdges, goalArrivalTime, maxSpeed,maxComfySpeed,currentTime);
			break;
		case PregnantWoman:
			agent = new PregnantWomanAgent(agentID, goalNode, routeEdges, goalArrivalTime, maxSpeed,maxComfySpeed,currentTime);
			break;
		}
		return agent;
	}
	
	public double getMaxComfortableDrivingSpeed(double maxSpeed) {
		double maxComfySpeed = 0;
		switch(this) {
		case Normal:
			maxComfySpeed = maxSpeed * 0.6;
			break;
		case OldLady:
			maxComfySpeed = maxSpeed * 0.3;
			break;
		case PregnantWoman:
			maxComfySpeed = maxSpeed;
			break;
		}
		return maxComfySpeed;
	}
	public int goalArrivalTime(int startTime, int minimalTravelTime) {
		int goalArrivalTime = 0;
		switch(this) {
		case Normal:
			// normally we are kinda in a hurry
			goalArrivalTime =  startTime + minimalTravelTime *2;
			break;
		case OldLady:
			// No hurry, my dear
			goalArrivalTime =  startTime + minimalTravelTime *3;

			break;
		case PregnantWoman:
			//OUTTATHEWAY
			goalArrivalTime =  startTime + minimalTravelTime;
		}
		return goalArrivalTime;
	}

}
