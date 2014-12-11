package nl.uu.trafficmas;

import java.util.ArrayList;

import nl.uu.trafficmas.agent.AgentType;
import nl.uu.trafficmas.organisation.Organisation;
import nl.uu.trafficmas.roadnetwork.RoadNetwork;

public interface DataModel {
	public RoadNetwork instantiateRoadNetwork();
	public ArrayList<Organisation> instantiateOrganisations();
	public String getSumoConfigPath();
	public double getAgentSpawnProbability();
	public ArrayList<KeyValue<AgentType, Double>> getAgentTypeDistribution();
}
