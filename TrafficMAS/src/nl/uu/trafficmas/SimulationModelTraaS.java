package nl.uu.trafficmas;

import it.polito.appeal.traci.SumoTraciConnection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.tudresden.sumo.cmd.Simulation;
import de.tudresden.sumo.cmd.Vehicle;
import de.tudresden.ws.container.SumoStringList;
import nl.uu.trafficmas.agent.Agent;
import nl.uu.trafficmas.agent.AgentPhysical;
import nl.uu.trafficmas.agent.actions.AgentAction;
import nl.uu.trafficmas.roadnetwork.Road;
import nl.uu.trafficmas.roadnetwork.RoadNetwork;

public class SimulationModelTraaS implements SimulationModel {

	SumoTraciConnection conn;
	String sumocfg;
	String sumoBin;
	public static final int LOOK_AHEAD_DISTANCE = 100;
	
	public SimulationModelTraaS(String sumoBin, String sumocfg){
		this.sumoBin = sumoBin;
		this.sumocfg = sumocfg;
	}
	
	
	
	@Override
	public void initialize() {
		conn = initialize(sumoBin, sumocfg);     
	}
	public static SumoTraciConnection initialize(String sumoBin, String sumocfg){
		SumoTraciConnection conn = new SumoTraciConnection(sumoBin, sumocfg);
		// Add an extra option.
		try {	
			//start TraCI
			conn.runServer();
	       //load routes and initialize the simulation
			conn.do_timestep();    
       }catch(Exception ex){ex.printStackTrace();}
		
		return conn;
	}
	
	@Override
	public void initializeWithOption(String option, String value) {
		conn = initializeWithOption(option, value, sumoBin, sumocfg);
	}
	public static SumoTraciConnection initializeWithOption(String option, String value, String sumoBin, String sumocfg){
		SumoTraciConnection conn = new SumoTraciConnection(sumoBin, sumocfg);
		// Add an extra option.
		conn.addOption(option, value);
		try {	
			//start TraCI
			conn.runServer();
	       //load routes and initialize the simulation
			conn.do_timestep();    
       }catch(Exception ex){ex.printStackTrace();}
		
		return conn;
	}

	@Override
	public void close() {
		conn.close();
	}
	
	@Override
	public void doTimeStep() {
		try {
			conn.do_timestep();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	@Override
	public void addAgent(Agent agent, String routeID, int tick) {
		addAgent(agent, routeID, tick, conn);
	}
	public static void addAgent(Agent agent, String routeID, int tick, SumoTraciConnection conn){
		try {
			conn.do_job_set(Vehicle.add(agent.agentID, "Car", routeID, tick, 0.0, 10.0, (byte) 0));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public HashMap<String, Agent> addAgents(ArrayList<Pair<Agent, Integer>> agentPairList) {
		
		return addAgents(agentPairList, conn);
	}
	//TODO: Implement routes.
	public static HashMap<String, Agent> addAgents(ArrayList<Pair<Agent, Integer>> agentPairList, SumoTraciConnection conn){
		HashMap<String, Agent> completeAgentMap = new HashMap<String, Agent>();
		for(Pair<Agent, Integer> agentPair : agentPairList){
			addAgent(agentPair.first, "route0", agentPair.second, conn);
			completeAgentMap.put(agentPair.first.agentID, agentPair.first);
		}
		return completeAgentMap;
	}
	
	
	
	
	@Override
	public HashMap<String, Agent> updateCurrentAgentMap(HashMap<String, Agent> completeAgentMap, HashMap<String, Agent> oldAgentMap) {
		return updateCurrentAgentMap(completeAgentMap, oldAgentMap, conn);
	}
	public static HashMap<String, Agent> updateCurrentAgentMap(HashMap<String, Agent> completeAgentMap, HashMap<String, Agent> oldAgentMap, SumoTraciConnection conn){
		HashMap<String, Agent> currentAgentMap = new HashMap<String, Agent>(oldAgentMap);
		try {
			
			SumoStringList departedVehicleIDList = (SumoStringList) conn.do_job_get(Simulation.getArrivedIDList());
			SumoStringList arrivedVehicleIDList = (SumoStringList) conn.do_job_get(Simulation.getDepartedIDList());			
			
			for(String departedVehicleID: departedVehicleIDList){
				currentAgentMap.remove(departedVehicleID);
			}

			for(String arrivedVehicleID: arrivedVehicleIDList){
				currentAgentMap.put(arrivedVehicleID, completeAgentMap.get(arrivedVehicleID));

			}
			
		} catch (Exception e){
			e.printStackTrace();
		}
		return currentAgentMap;
	}
	
	@Override
	public HashMap<String, AgentPhysical> updateAgentsPhys(RoadNetwork rn, HashMap<String, Agent> currentAgentList) {
		return updateAgentsPhys(rn, currentAgentList, conn);
	}
	public static HashMap<String, AgentPhysical> updateAgentsPhys(RoadNetwork rn, HashMap<String, Agent> currentAgentMap, SumoTraciConnection conn){
		HashMap<String, AgentPhysical> agentPhysMap = new HashMap<String, AgentPhysical>(currentAgentMap);
		try {
			// Loop over all agents, TODO: Only check the agents that passed a sensor
			for(Map.Entry<String, Agent> agentMap : currentAgentMap.entrySet()){
				
				
				AgentPhysical aPhys = agentMap.getValue();
				String agentID = agentMap.getKey();
				
				// Retrieve all physical agent information from SUMO
				double velocity = (double) conn.do_job_get(Vehicle.getSpeed(agentID));
				String roadID = (String) conn.do_job_get(Vehicle.getRoadID(agentID));
				Road road = rn.getRoadFromID(roadID);
				
				int laneIndex = (int) conn.do_job_get(Vehicle.getLaneIndex(agentID));
				double distance = (double) conn.do_job_get(Vehicle.getLanePosition(agentID));
				
				// Update the agent with information
				aPhys.setVelocity(velocity);
				if(road != null) { //TODO: make this hack more robust
					aPhys.setRoad(road);
					aPhys.setLane(road.getLanes()[laneIndex]);
				}
				aPhys.setDistance(distance);
				agentPhysMap.put(agentID, aPhys);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return agentPhysMap;
	}

	@Override
	public HashMap<String, AgentPhysical> getLeadingVehicles(HashMap<String, AgentPhysical> currentAgentPhysMap) {
		return getLeadingVehicles(currentAgentPhysMap, conn);
	}
	// This method returns a HashMap that contains every vehicleID as key, and its leading vehicle as value. 
	public static HashMap<String, AgentPhysical> getLeadingVehicles(HashMap<String, AgentPhysical> currentAgentPhysMap, SumoTraciConnection conn){
		HashMap<String, AgentPhysical> agentLeaderMap = new HashMap<String, AgentPhysical>();
		for(Map.Entry<String, AgentPhysical> entry: currentAgentPhysMap.entrySet()){
			// TODO: Replace hardcoded distance with dynamic distance depending on type of agent.
			try {
				// TODO: What object does this return?
				Object[] leadVehicleArray = (Object[])(conn.do_job_get(Vehicle.getLeader(entry.getValue().agentID, LOOK_AHEAD_DISTANCE)));
				//System.out.println(leadVehicleArray[0].toString());
				//System.out.println(leadVehicleArray[1].toString());
				AgentPhysical leadingAgent = currentAgentPhysMap.get(leadVehicleArray[0].toString());
				agentLeaderMap.put(leadVehicleArray[0].toString(), leadingAgent);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return agentLeaderMap;
	}
	
	@Override
	public void prepareAgentActions(HashMap<String, AgentAction> actions) {
		// TODO Auto-generated method stub
	}
}
