package nl.uu.trafficmas.agent.actions;

import java.util.ArrayList;

import nl.uu.trafficmas.organisation.Sanction;
import de.tudresden.sumo.util.SumoCommand;

public class ChangeRoadAction extends SumoAgentAction {

	public ChangeRoadAction(int priority) {
		super(priority);
	}

	@Override
	public double getTime(int currentTime, double currentSpeed,
			double meanTravelSpeedNextLane, double currentPos,
			double currentLaneLength, double maxComfySpeed,
			double routeRemainderLength, double leaderAgentSpeed,
			double leaderDistance) {
		// TODO implement this
		return Double.POSITIVE_INFINITY;
	}

	@Override
	public ArrayList<Sanction> getSanctions(double maxComfySpeed,
			double velocity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SumoCommand getCommand(String agentID, byte agentLaneIndex,
			int maxLaneIndex, int overtakeDuration, double d, double e) {
		// TODO Auto-generated method stub
		return null;
	}

}
