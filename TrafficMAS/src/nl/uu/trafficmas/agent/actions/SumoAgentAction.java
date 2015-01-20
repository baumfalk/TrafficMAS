package nl.uu.trafficmas.agent.actions;

import de.tudresden.sumo.util.SumoCommand;

public abstract class SumoAgentAction extends AgentAction {

	public SumoAgentAction(int priority) {
		super(priority);
	}
	
	public abstract SumoCommand getCommand(String agentID, byte agentLaneIndex, int maxLaneIndex, int overtakeDuration, double d, double e);

}
