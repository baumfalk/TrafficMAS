package nl.uu.trafficmas.datamodel;

import java.util.ArrayList;
import java.util.HashMap;

import nl.uu.trafficmas.agent.AgentProfileType;
import nl.uu.trafficmas.roadnetwork.RoadNetwork;
import nl.uu.trafficmas.roadnetwork.Route;

public interface DataModel {
	/**
	 * Instantiates the RoadNetwork according to the XML files read by the Datamodel.
	 * @return a RoadNetwork, if the RoadNetwork is not correctly validated, this method will return null.
	 */
	public RoadNetwork 	instantiateRoadNetwork();
	
	/**
	 * Returns the MASData, which includes simulationLength, sumoConfigPath, spawnProbability and AgentProfileTypeDistribution.
	 * @return the MASData data structure
	 */
	public MASData 	getMASData();
	
	/**
	 * Returns the path to the sumoConfig XML file, which is needed by SUMO.
	 * @return path to sumoConfig XML file.
	 */
	public String getSumoConfigPath();
	/**
	 * Returns the probability of an agent spawning each tick.
	 * @return a value between 0 and 1, including 0 and 1.
	 */
	public double getAgentSpawnProbability();
	
	/**
	 * Extracts the simulation length from the MAS.XML file.
	 * @return an Integer with the time in seconds concerning how long SUMO will run.
	 */
	public int getSimulationLength();
	
	/**
	 * Extracts the agentProfileTypeDistribution and returns it in a HashMap.
	 * @return a map containing the AgentProfile and the chance of it occurring in a value between 0 and 1.
	 */
	public HashMap<AgentProfileType, Double> getAgentProfileTypeDistribution();
	
	/**
	 * Returns a list of all routes that can be used by the agents. These routes are stated in the XML files read by the Datamodel.
	 * @param rn
	 * @return an ArrayList of all routes read from the XML file. 
	 */
	public ArrayList<Route> getRoutes(RoadNetwork rn);
	
	/**
	 * Not yet implemented
	 */
	public void close();
}
