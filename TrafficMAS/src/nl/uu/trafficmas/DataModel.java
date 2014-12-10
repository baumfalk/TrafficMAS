package nl.uu.trafficmas;

import java.util.ArrayList;
import java.util.HashMap;

import nl.uu.trafficmas.agent.Agent;
import nl.uu.trafficmas.agent.AgentAction;
import nl.uu.trafficmas.agent.AgentPhysical;
import nl.uu.trafficmas.organisation.Organisation;
import nl.uu.trafficmas.roadnetwork.RoadNetwork;

public interface DataModel {
	public RoadNetwork instantiateRoadNetwork();
	public ArrayList<Agent> instantiateAgents();
	public ArrayList<Organisation> instantiateOrganisations();
}
