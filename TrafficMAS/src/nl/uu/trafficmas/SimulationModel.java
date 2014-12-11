package nl.uu.trafficmas;

import java.util.ArrayList;
import java.util.HashMap;

import nl.uu.trafficmas.agent.AgentAction;
import nl.uu.trafficmas.agent.AgentPhysical;

public interface SimulationModel {
	public void initialize(); 
	public void initializeWithOption(String option, String value);
	public ArrayList<AgentPhysical> getAgentPhysical();
	public HashMap<AgentPhysical, AgentPhysical> getLeadingVehicles();
	public void executeAgentActions(ArrayList<AgentAction> actions);
}
