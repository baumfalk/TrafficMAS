package nl.uu.trafficmas.simulationmodel;

public class AgentData implements Data {

	public final String id;
	public final String leaderId;
	public final double leaderDistance;
	public final double position;
	public final double speed;
	public final String laneID;

	public AgentData(String id, Object[] leader, double position, double speed, String laneID) {
		this.id = id;
		this.laneID = laneID;
		if(leader == null) {
			leaderId = null;
			leaderDistance = Double.MIN_VALUE;
			
		} else {
			leaderId = (String) leader[0];
			leaderDistance = (double) leader[1];
		}
		this.position = position;
		this.speed = speed;
	}
}
