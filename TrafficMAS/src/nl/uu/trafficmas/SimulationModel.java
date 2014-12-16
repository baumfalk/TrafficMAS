package nl.uu.trafficmas;

import java.util.ArrayList;
import java.util.HashMap;

import nl.uu.trafficmas.agent.Agent;
import nl.uu.trafficmas.agent.AgentPhysical;
import nl.uu.trafficmas.agent.actions.AgentAction;
import nl.uu.trafficmas.roadnetwork.RoadNetwork;

public interface SimulationModel {
	public void initialize(); 
	public void initializeWithOption(String option, String value);
	public void close();
	
	public void addAgents(ArrayList<Pair<Agent, Integer>> agentList);
	public void addAgent(Agent agent, String routeID, int tick);
	public ArrayList<Agent> updateCurrentAgentList();
	
	public ArrayList<AgentPhysical> updateAgentsPhys(RoadNetwork rn, ArrayList<Agent> currentAgentList);
	public HashMap<AgentPhysical, AgentPhysical> getLeadingVehicles();
	public void prepareAgentActions(ArrayList<AgentAction> actions);
	public void doTimeStep();
}
