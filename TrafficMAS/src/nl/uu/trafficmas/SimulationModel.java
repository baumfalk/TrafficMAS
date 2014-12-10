package nl.uu.trafficmas;

import java.util.ArrayList;
import java.util.HashMap;

import nl.uu.trafficmas.agent.AgentAction;
import nl.uu.trafficmas.agent.AgentPhysical;

public interface SimulationModel {
	public ArrayList<AgentPhysical> getAgentPhysical();
	public HashMap<AgentPhysical, AgentPhysical> getLeadingVehicles();
	public void executeAgentActions(ArrayList<AgentAction> actions);
}
