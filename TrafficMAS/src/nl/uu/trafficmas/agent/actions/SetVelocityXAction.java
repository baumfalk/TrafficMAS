package nl.uu.trafficmas.agent.actions;

import java.util.Map;

import nl.uu.trafficmas.agent.Agent;
import nl.uu.trafficmas.simulationmodel.AgentData;
import de.tudresden.sumo.cmd.Vehicle;
import de.tudresden.sumo.util.SumoCommand;


public class SetVelocityXAction extends SumoAgentAction {

	private double X;

	public SetVelocityXAction(int priority) {
		super(priority);
	}

	@Override
	public SumoCommand getCommand(Agent agent) {
		double maxComfySpeed = agent.getMaxComfySpeed();
		return Vehicle.setMaxSpeed(agent.agentID, Math.max(0.01,Math.min(X, maxComfySpeed)));
	}

	@Override
	public double getTime(int currentTime, double currentSpeed,
			double meanTravelSpeedNextLane, double currentPos,
			double currentLaneLength, double maxComfySpeed,
			double routeRemainderLength, double leaderAgentSpeed,
			double leaderDistance, Agent agent) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public AgentData getNewAgentState(AgentData agentData) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void setParameters(Map<String,String> parameters) {
		this.parameters = parameters;
		X = Double.parseDouble(parameters.get("X"));
	}

}
