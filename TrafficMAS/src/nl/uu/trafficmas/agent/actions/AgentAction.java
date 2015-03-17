package nl.uu.trafficmas.agent.actions;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import nl.uu.trafficmas.agent.Agent;
import nl.uu.trafficmas.norm.NormInstantiation;
import nl.uu.trafficmas.norm.Sanction;
import nl.uu.trafficmas.simulationmodel.AgentData;

public abstract class AgentAction {
	
	// add new actions here
	// listed in order of priority (descending)
	private static int actionPriority = 0;
	public static final AgentAction DoNothingAction				= new DoNothingAction(actionPriority++);
	public static final AgentAction ChangeVelocityMinus1Action	= new ChangeVelocityMinus1Action(actionPriority++);
	public static final AgentAction ChangeVelocityMinus5Action	= new ChangeVelocityMinus5Action(actionPriority++);
	public static final AgentAction ChangeVelocityMinus10Action	= new ChangeVelocityMinus10Action(actionPriority++);
	public static final AgentAction ChangeVelocityMinus20Action	= new ChangeVelocityMinus20Action(actionPriority++);
	public static final AgentAction ChangeVelocityMinus50Action	= new ChangeVelocityMinus50Action(actionPriority++);
	public static final AgentAction ChangeVelocity1				= new ChangeVelocity1Action(actionPriority++);
	public static final AgentAction ChangeVelocity5				= new ChangeVelocity5Action(actionPriority++);
	public static final AgentAction ChangeVelocity10			= new ChangeVelocity10Action(actionPriority++);
	public static final AgentAction ChangeVelocity20			= new ChangeVelocity20Action(actionPriority++);
	public static final AgentAction ChangeVelocity50			= new ChangeVelocity50Action(actionPriority++);
	public static final AgentAction ChangeLane 					= new ChangeLaneAction(actionPriority++);
	public static final AgentAction ChangeRoute 				= new ChangeRouteAction(actionPriority);
	
	public final int priority;
	
	protected double utility;
	protected Map<String, String> parameters;
	private double distance;

	public AgentAction(int priority) {
		this.priority = priority;
	}
	
	public static AgentAction[] values() {
		// add new actions here
		AgentAction [] array = {DoNothingAction,ChangeVelocityMinus50Action,ChangeVelocityMinus20Action,ChangeVelocityMinus10Action,ChangeVelocityMinus5Action,ChangeVelocityMinus1Action,ChangeVelocity1, ChangeVelocity5, ChangeVelocity10,
				ChangeVelocity20, ChangeVelocity50, ChangeLane, ChangeRoute};
		return array;
	}
	
	public abstract double getTime(int currentTime, double currentSpeed, double meanTravelSpeedNextLane, double currentPos, double currentLaneLength, double maxComfySpeed, double routeRemainderLength, double leaderAgentSpeed, double leaderDistance, Agent agent);
	
	public void setUtility(double newUtility) {
		utility = newUtility;
	}
	
	public double getUtility(){
		return utility;
	}

	public abstract AgentData getNewAgentState(AgentData agentData);
	
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
	
	public void setParameters(Map<String,String> parameters) {
		this.parameters = parameters;
	}

	public boolean isRelevant(Agent agent) {
		return true;
	}

}
