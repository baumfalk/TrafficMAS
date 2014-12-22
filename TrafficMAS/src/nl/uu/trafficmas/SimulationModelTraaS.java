package nl.uu.trafficmas;

import it.polito.appeal.traci.SumoTraciConnection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

import nl.uu.trafficmas.agent.Agent;
import nl.uu.trafficmas.agent.AgentPhysical;
import nl.uu.trafficmas.agent.AgentProfileType;
import nl.uu.trafficmas.agent.actions.AgentAction;
import nl.uu.trafficmas.roadnetwork.Edge;
import nl.uu.trafficmas.roadnetwork.Lane;
import nl.uu.trafficmas.roadnetwork.Road;
import nl.uu.trafficmas.roadnetwork.RoadNetwork;
import de.tudresden.sumo.cmd.Simulation;
import de.tudresden.sumo.cmd.Vehicle;
import de.tudresden.sumo.util.SumoCommand;
import de.tudresden.ws.container.SumoColor;
import de.tudresden.ws.container.SumoStringList;

public class SimulationModelTraaS implements SimulationModel {

	SumoTraciConnection conn;
	String sumocfg;
	String sumoBin;
	public static final int LOOK_AHEAD_DISTANCE = 100;
	public static final int OVERTAKE_DURATION = 5;
	
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
	public void initializeWithOptions(HashMap<String,String> optionValueMap) {
		conn = initializeWithOptions(optionValueMap, sumoBin, sumocfg);
	}
	public static SumoTraciConnection initializeWithOptions(HashMap<String,String> optionValueMap, String sumoBin, String sumocfg){
		SumoTraciConnection conn = new SumoTraciConnection(sumoBin, sumocfg);
		// Add an extra option.
		for(Entry<String, String> keyValue : optionValueMap.entrySet()) {
			conn.addOption(keyValue.getKey(), keyValue.getValue());
		}
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
			conn.do_job_set(addAgentCommand(agent, routeID, tick));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static SumoCommand addAgentCommand(Agent agent, String routeID, int tick) {
		return Vehicle.add(agent.agentID, "Car", routeID, tick, 0.0, Math.min(agent.getMaxComfySpeed(),10), (byte) 0);
	}
	
	@Override
	public HashMap<String, Agent> addAgents(ArrayList<Pair<Agent, Integer>> agentPairList) {
		
		return addAgents(agentPairList, conn);
	}
	//TODO: Implement routes.
	public static HashMap<String, Agent> addAgents(ArrayList<Pair<Agent, Integer>> agentPairList, SumoTraciConnection conn){
		HashMap<String, Agent> completeAgentMap = new HashMap<String, Agent>();
		ArrayList<SumoCommand> cmds = new ArrayList<>();
		for(Pair<Agent, Integer> agentPair : agentPairList){
			cmds.add(addAgentCommand(agentPair.first, "route0", agentPair.second));
			cmds.add(Vehicle.setLaneChangeMode(agentPair.first.agentID, 0b0001000000));
			cmds.add(Vehicle.setSpeedMode(agentPair.first.agentID, 0b00000));
			completeAgentMap.put(agentPair.first.agentID, agentPair.first);
		
		}
		try {
			conn.do_jobs_set(cmds);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			
			ArrayList<SumoCommand> cmdList = new ArrayList<>();
			cmdList.add(Simulation.getArrivedIDList());
			cmdList.add(Simulation.getDepartedIDList());
			ArrayList<Object> responses = conn.do_jobs_get(cmdList);
			
			SumoStringList departedVehicleIDList = (SumoStringList) responses.get(0);
			SumoStringList arrivedVehicleIDList = (SumoStringList) responses.get(1);			
			
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
	public HashMap<String, Agent> updateAgents(RoadNetwork rn, HashMap<String, Agent> currentAgentList) {
		return updateAgents(rn, currentAgentList, conn);
	}
	public static HashMap<String, Agent> updateAgents(RoadNetwork rn, HashMap<String, Agent> currentAgentMap, SumoTraciConnection conn){
		HashMap<String, Agent> agentsMap = new HashMap<String, Agent>(currentAgentMap);
		if(currentAgentMap.size() == 0) 
			return agentsMap;
		try {
			// Loop over all agents, TODO: Only check the agents that passed a sensor
			
			ArrayList<SumoCommand> cmdList = new ArrayList<>();
			for(Map.Entry<String, Agent> agentMap : currentAgentMap.entrySet()){
				String agentID = agentMap.getKey();
				cmdList.add(Vehicle.getSpeed(agentID));
				cmdList.add(Vehicle.getRoadID(agentID));
				cmdList.add(Vehicle.getLaneIndex(agentID));
				cmdList.add(Vehicle.getLanePosition(agentID));
			}
			
			ArrayList<Object> responses = conn.do_jobs_get(cmdList);
			int currentAgent = 0;
			for(Map.Entry<String, Agent> agentMap : currentAgentMap.entrySet()){
				
				int currentResponseForAgent = 0;
				String agentID = agentMap.getKey();
				Agent agent = agentMap.getValue();

				// Retrieve all physical agent information from SUMO
				double velocity = (double) responses.get(currentAgent*4 + currentResponseForAgent++);
				String roadID = (String)responses.get(currentAgent*4 + currentResponseForAgent++);
				Road road = rn.getRoadFromID(roadID);
				int laneIndex = (int) responses.get(currentAgent*4 + currentResponseForAgent++);
				double distance = (double) responses.get(currentAgent*4 + currentResponseForAgent++);
				
				// Update the agent with information
				agent.setVelocity(velocity);
				
				if(road != null) { //TODO: make this hack more robust
					agent.setRoad(road);
					agent.setLane(road.getLanes()[laneIndex]);
				}
				
				agent.setDistance(distance);
				double expectedArrivalTime = 0;
				Edge [] route = agent.getRoute();
				for(Edge edge: route) {
					if(edge.getRoad().equals(agent.getRoad())) {
						double distRemains = edge.getRoad().length - agent.getDistance();
						double averageSpeedEdge = edge.getRoad().length/edge.getRoad().getMeanTravelTime();
						expectedArrivalTime += distRemains/averageSpeedEdge;
					}
					expectedArrivalTime += edge.getRoad().getMeanTravelTime(); 
				}
				
				agent.setExpectedArrivalTime((int) Math.round(expectedArrivalTime));
				agentsMap.put(agentID, agent);
				currentAgent++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return agentsMap;
	}

	@Override
	public HashMap<String, Agent> getLeadingVehicles(HashMap<String, Agent> currentAgentPhysMap) {
		return getLeadingVehicles(currentAgentPhysMap, conn);
	}
	// This method returns a HashMap that contains every vehicleID as key, and its leading vehicle as value. 
	public static HashMap<String, Agent> getLeadingVehicles(HashMap<String, Agent> agents, SumoTraciConnection conn){
		HashMap<String, Agent> agentLeaderMap = new HashMap<String, Agent>();

		
		if(agents.size() == 0)
			return agentLeaderMap;
		ArrayList<SumoCommand> cmdList = new ArrayList<SumoCommand>();
		for(Map.Entry<String, Agent> entry: agents.entrySet()){
			cmdList.add(Vehicle.getLeader(entry.getValue().agentID, LOOK_AHEAD_DISTANCE));
		}
		ArrayList<Object> responses= null;
		try {
			responses = conn.do_jobs_get(cmdList);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// Loop through every agent currently in the simulation
		int currentEntry = 0;
		for(Map.Entry<String, Agent> entry: agents.entrySet()){
			// TODO: Replace hardcoded distance with dynamic distance depending on type of agent.
			Object[] leadVehicleArray = (Object[]) responses.get(currentEntry++);
			
			if(!(leadVehicleArray[1].equals(-1.0))){
				// If leadVehicleArray[1] is not -1.0, the agent has a leading vehicle. 
				Agent leadingAgent = agents.get(leadVehicleArray[0].toString());
				agentLeaderMap.put(entry.getKey(), leadingAgent);
			} else{
				// If the agent has no leading vehicle, it will return null
				agentLeaderMap.put(entry.getKey(), null);
			}
				
		}
		return agentLeaderMap;
	}
	
	@Override
	public void prepareAgentActions(HashMap<String, AgentAction> actions, HashMap<String, Agent> currentAgentMap) {
		prepareAgentActions(actions, currentAgentMap, conn);
	}
	
	public static void prepareAgentActions(HashMap<String, AgentAction> actions, HashMap<String, Agent> currentAgentMap, SumoTraciConnection conn){
		if(actions.size() == 0 && currentAgentMap.size()==0) {
			return;
		}
		
		ArrayList<SumoCommand> cmdList = new ArrayList<SumoCommand>();
		
		
		for(Map.Entry<String, AgentAction> entry: actions.entrySet()){
			
			Agent currentAgent = currentAgentMap.get(entry.getKey());
			byte agentLaneIndex = currentAgent.getLane().laneIndex;
			int maxLaneIndex = currentAgent.getRoad().laneList.size()-1;
		
			if(entry.getValue() == null)
				continue;
			switch(entry.getValue()) {
			case ChangeLane:
				if(agentLaneIndex < maxLaneIndex){
					cmdList.add(Vehicle.changeLane(entry.getKey(), (byte) (agentLaneIndex+1) , OVERTAKE_DURATION));
				} else {
					//TODO exceptions.
				}
				break;
			case ChangeRoad:
				// TODO
				break;
			case ChangeVelocity5:
				cmdList.add(Vehicle.slowDown(entry.getKey(), currentAgent.getVelocity()+5.0,5));
				break;
			case ChangeVelocity10:
				cmdList.add(Vehicle.slowDown(entry.getKey(), currentAgent.getVelocity()+10.0,10));
				break;
			case ChangeVelocity20:
				cmdList.add(Vehicle.slowDown(entry.getKey(), currentAgent.getVelocity()+20.0,15));
				break;
			default:
				System.out.println("Error on action name, no action executed");			
		
			}
		}
		
	
		try {
			if(cmdList.size() != 0) {
				conn.do_jobs_set(cmdList);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Override
	public RoadNetwork updateRoadNetwork(RoadNetwork roadNetwork) {
		return updateRoadNetwork(roadNetwork,conn);
	}
	
	//TODO write a test for this
	public RoadNetwork updateRoadNetwork(RoadNetwork roadNetwork, SumoTraciConnection conn) {
		ArrayList<SumoCommand> cmdList = new ArrayList<SumoCommand>();
		for(Edge edge: roadNetwork.getEdges()) {
			cmdList.add(de.tudresden.sumo.cmd.Edge.getTraveltime(edge.getID()));
			for(Lane lane :edge.getRoad().getLanes()) {
				cmdList.add(de.tudresden.sumo.cmd.Lane.getTraveltime(lane.getID()));
				cmdList.add(de.tudresden.sumo.cmd.Lane.getLastStepVehicleNumber(lane.getID()));
			}
		}
		ArrayList<Object> responses = null;
		try {
			 responses = conn.do_jobs_get(cmdList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		int currentIndex = 0;
		for(Edge edge: roadNetwork.getEdges()) {
			double roadMeanTravelTime = (double) responses.get(currentIndex++);
			edge.getRoad().setMeanTravelTime(roadMeanTravelTime);
			for(Lane lane : edge.getRoad().getLanes()) {
				double laneMeanTravelTime = (double) responses.get(currentIndex++);
				int laneGetVehicleNumber = (int) responses.get(currentIndex++);
				//empty lane, then sumo is unreliable
				if(laneGetVehicleNumber == 0) {
					laneMeanTravelTime = 0;
				}
				lane.setMeanTravelTime(laneMeanTravelTime);	
			}
		}
		
		return roadNetwork;//TODO update the road network
	}
}
