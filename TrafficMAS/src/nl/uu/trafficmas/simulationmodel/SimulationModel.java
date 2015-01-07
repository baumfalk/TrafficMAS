package nl.uu.trafficmas.simulationmodel;

import java.util.HashMap;

import nl.uu.trafficmas.agent.Agent;
import nl.uu.trafficmas.agent.actions.AgentAction;
import nl.uu.trafficmas.roadnetwork.RoadNetwork;

public interface SimulationModel {
	public void initialize(); 
	public void initializeWithOptions(HashMap<String, String> optionValueMap);
	
	public void close();
	public void doTimeStep();
	
	public StateData getNewStateData();
	public void simulateAgentActions(HashMap<Agent, AgentAction> agentActions);
	
	public HashMap<String, Agent> addAgents(HashMap<Agent, Integer> agentPairList);
	public void addAgent(Agent agent, String routeID, int tick);

	public void prepareAgentActions(HashMap<String, AgentAction> actions, HashMap<String, Agent> currentAgentMap);
	public RoadNetwork updateRoadNetwork(RoadNetwork roadNetwork);
	public HashMap<String, Agent> updateCurrentAgentMap(HashMap<String, Agent> completeAgentMap, HashMap<String, Agent> oldAgentMap);
}
