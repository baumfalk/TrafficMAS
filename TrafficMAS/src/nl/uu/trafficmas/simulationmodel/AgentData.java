package nl.uu.trafficmas.simulationmodel;

public class AgentData implements Data {

	public final String id;
	public final String leaderId;
	public final double leaderDistance;
	public final double position;
	public final double speed;
	public final String roadID;
	public final int laneIndex;

	public AgentData(String id, Object[] leader, double position, double speed, String roadID, int laneIndex) {
		this.id = id;
		if(leader == null) {
			leaderId = null;
			leaderDistance = Double.MIN_VALUE;
			
		} else {
			leaderId = (String) leader[0];
			leaderDistance = (double) leader[1];
		}
		this.position 	= position;
		this.speed		= speed;
		this.roadID 	= roadID;
		this.laneIndex 	= laneIndex;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("ID: "+ id+"\r\n");
		sb.append("Speed: "+ speed+"\r\n");
		sb.append("Position: "+ position+"\r\n");
		sb.append("roadID: "+ roadID+"\r\n");
		sb.append("laneIndex: "+ laneIndex+"\r\n");
		sb.append("LeaderID: "+ leaderId+"\r\n");
		sb.append("LeaderDistance: "+ leaderDistance+"\r\n");

		return sb.toString();
	}
}
