package nl.uu.trafficmas.view;

import java.util.ArrayList;

import nl.uu.trafficmas.agent.Agent;
import nl.uu.trafficmas.organisation.Organisation;
import nl.uu.trafficmas.roadnetwork.RoadNetwork;

public interface TrafficView {
	public void updateFromRoadNetwork(RoadNetwork rn);
	public void updateFromAgent(Agent agent);
	public void updateFromAgents(ArrayList<Agent> agents);
	public void updateFromOrganisation(Organisation org);
	public void updateFromOrganisations(ArrayList<Organisation> orgs);
	public void visualize();
	
}
