package nl.uu.trafficmas.simulationmodel;

import java.util.HashMap;

import nl.uu.trafficmas.agent.Agent;
import nl.uu.trafficmas.agent.actions.AgentAction;

public interface SimulationModel {
	public void initialize(); 
	public void initializeWithOptions(HashMap<String, String> optionValueMap);
	
	public void close();
	public void doTimeStep();
	
	public StateData getNewStateData();
	
	public HashMap<String, Agent> addAgents(HashMap<Agent, Integer> agentPairList);
	public void addAgent(Agent agent, String routeID, int tick);

	/**
	 * Sends a list of commands to SUMO. The commands are generated from he hashmap 'agentActions' and are all actions the vehicle can take.
	 * The following actions can be send to SUMO:
	 * ChangeLane,
	 * ChangeRoad,
	 * ChangeVelocity5,
	 * ChangeVelocity10,
	 * ChangeVelocity20.
	 * @param actions
	 * @param conn
	 */
	public void simulateAgentActions(HashMap<Agent, AgentAction> agentActions);
	public HashMap<String, Agent> updateCurrentAgentMap(HashMap<String, Agent> completeAgentMap, HashMap<String, Agent> oldAgentMap);
}
