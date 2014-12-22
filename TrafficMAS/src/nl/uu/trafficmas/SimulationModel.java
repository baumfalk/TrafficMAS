package nl.uu.trafficmas;

import java.util.ArrayList;
import java.util.HashMap;

import nl.uu.trafficmas.agent.Agent;
import nl.uu.trafficmas.agent.AgentPhysical;
import nl.uu.trafficmas.agent.actions.AgentAction;
import nl.uu.trafficmas.roadnetwork.RoadNetwork;

public interface SimulationModel {
	public void initialize(); 
	public void initializeWithOptions(HashMap<String, String> optionValueMap);
	public void close();
	public void doTimeStep();
	
	public HashMap<String, Agent> addAgents(ArrayList<Pair<Agent, Integer>> agentList);
	public void addAgent(Agent agent, String routeID, int tick);

	public HashMap<String, Agent> updateCurrentAgentMap(HashMap<String, Agent> completeAgentMap, HashMap<String, Agent> oldAgentMap);
	public HashMap<String, Agent> updateAgents(RoadNetwork rn, HashMap<String, Agent> currentAgentList);
	public HashMap<String, Agent> getLeadingVehicles(HashMap<String, Agent> currentAgentPhysList);
	public void prepareAgentActions(HashMap<String, AgentAction> actions, HashMap<String, Agent> currentAgentMap);
	public RoadNetwork updateRoadNetwork(RoadNetwork roadNetwork);
}
