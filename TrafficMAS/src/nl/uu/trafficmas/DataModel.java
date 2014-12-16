package nl.uu.trafficmas;

import java.util.ArrayList;

import nl.uu.trafficmas.agent.Agent;
import nl.uu.trafficmas.agent.AgentProfileType;
import nl.uu.trafficmas.organisation.Organisation;
import nl.uu.trafficmas.roadnetwork.RoadNetwork;

public interface DataModel {
	public RoadNetwork instantiateRoadNetwork();
	public ArrayList<Organisation> instantiateOrganisations();
	public String getSumoConfigPath();
	public double getAgentSpawnProbability();
	public int simulationLength();
	public ArrayList<Pair<AgentProfileType, Double>> getAgentProfileTypeDistribution();
	public ArrayList<Agent> instantiateAgents();
}
