package nl.uu.trafficmas.datamodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import nl.uu.trafficmas.agent.Agent;
import nl.uu.trafficmas.agent.AgentProfileType;
import nl.uu.trafficmas.organisation.Organisation;
import nl.uu.trafficmas.roadnetwork.RoadNetwork;
import nl.uu.trafficmas.roadnetwork.Route;

public interface DataModel {
	public RoadNetwork 	instantiateRoadNetwork();
	public MASData 		getMASData();
	public String getSumoConfigPath();
	public double getAgentSpawnProbability();
	public int getSimulationLength();
	public HashMap<AgentProfileType, Double> getAgentProfileTypeDistribution();
	public ArrayList<Route> getRoutes(RoadNetwork rn);
	public void close();
}
