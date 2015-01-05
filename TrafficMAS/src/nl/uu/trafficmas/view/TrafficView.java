package nl.uu.trafficmas.view;

import java.util.ArrayList;
import java.util.Collection;

import nl.uu.trafficmas.agent.Agent;
import nl.uu.trafficmas.organisation.Organisation;
import nl.uu.trafficmas.roadnetwork.RoadNetwork;

public interface TrafficView {
	public void updateFromRoadNetwork(RoadNetwork rn);
	public void updateFromAgent(Agent agent);
	public void updateFromAgents(Collection<Agent> currentAgents);
	public void updateFromOrganisation(Organisation org);
	public void updateFromOrganisations(Collection<Organisation> organisations);
	public void visualize();
	public void addMessage(String message);
	public void initialize();
	public void close();
}
