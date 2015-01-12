package nl.uu.trafficmas.agent.actions;

import java.util.ArrayList;

import de.tudresden.sumo.util.SumoCommand;
import nl.uu.trafficmas.organisation.Sanction;

public abstract class AgentAction {
	
	// add new actions here
	// listed in order of priority (descending)
	public static final AgentAction ChangeVelocity1		= new ChangeVelocity1Action(1);
	public static final AgentAction ChangeVelocity5		= new ChangeVelocity5Action(2);
	public static final AgentAction ChangeVelocity10	= new ChangeVelocity10Action(3);
	public static final AgentAction ChangeVelocity20	= new ChangeVelocity20Action(4);
	public static final AgentAction ChangeVelocity50	= new ChangeVelocity50Action(5);
	public static final AgentAction ChangeLane 			= new ChangeLaneAction(6);
	public static final AgentAction ChangeRoad 			= new ChangeRoadAction(7);
	
	public final int priority;
	
	protected double utility;

	public AgentAction(int priority) {
		this.priority = priority;
	}
	
	public static AgentAction[] values() {
		// add new actions here
		AgentAction [] array = {ChangeVelocity1, ChangeVelocity5, ChangeVelocity10,
				ChangeVelocity20, ChangeVelocity50, ChangeLane, ChangeRoad};
		return array;
	}
	
	public abstract double getTime(int currentTime, double currentSpeed, double meanTravelSpeedNextLane, double currentPos, double currentLaneLength, double maxComfySpeed, double routeRemainderLength, double leaderAgentSpeed, double leaderDistance);
	
	public abstract ArrayList<Sanction> getSanctions(double maxComfySpeed, double velocity);
	
	public abstract SumoCommand getCommand(String agentID, byte agentLaneIndex, int maxLaneIndex, int overtakeDuration, double d, double e);

	public void setUtility(double newUtility) {
		utility = newUtility;
	}
	
	public double getUtility(){
		return utility;
	}

	public static int compare(AgentAction action1, AgentAction action2) {
		if(action1.getUtility() > action2.getUtility())
    		return -1;
    	else if(action1.getUtility() < action2.getUtility()) {
    		return 1;
    	} else{
    		// lower priority => more important
    		return action1.priority - action2.priority;
    	}
    }
}
