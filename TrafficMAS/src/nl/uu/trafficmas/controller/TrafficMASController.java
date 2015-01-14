package nl.uu.trafficmas.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Random;

import nl.uu.trafficmas.agent.Agent;
import nl.uu.trafficmas.agent.AgentProfileType;
import nl.uu.trafficmas.agent.actions.AgentAction;
import nl.uu.trafficmas.datamodel.DataModel;
import nl.uu.trafficmas.datamodel.MASData;
import nl.uu.trafficmas.organisation.Organisation;
import nl.uu.trafficmas.roadnetwork.Edge;
import nl.uu.trafficmas.roadnetwork.Lane;
import nl.uu.trafficmas.roadnetwork.Node;
import nl.uu.trafficmas.roadnetwork.Road;
import nl.uu.trafficmas.roadnetwork.RoadNetwork;
import nl.uu.trafficmas.roadnetwork.Route;
import nl.uu.trafficmas.simulationmodel.AgentData;
import nl.uu.trafficmas.simulationmodel.SimulationModel;
import nl.uu.trafficmas.simulationmodel.StateData;
import nl.uu.trafficmas.view.TrafficView;

public class TrafficMASController {

	private RoadNetwork roadNetwork;
	private HashMap<Agent, Integer> agentsAndTime;
	private ArrayList<Organisation> organisations;
	private Random rng;
	
	private HashMap<String,Agent> completeAgentMap = new LinkedHashMap<String, Agent>();

	private ArrayList<Route> routes;
	private HashMap<String, Agent> currentAgentMap;
	private MASData masData;
		
	public TrafficMASController(DataModel dataModel,SimulationModel simulationModel, TrafficView view) {
		this(dataModel,simulationModel,view,-1);
	}
	
	public TrafficMASController(DataModel dataModel,SimulationModel simulationModel, TrafficView view, long seed) {
		if(seed == -1) {
			this.rng = new Random();
		} else {
			this.rng = new Random(seed);
		}
		view.addMessage("Initializing MAS with seed: "+ seed);
		
		///////////////
		// load data //
		///////////////
		this.masData 		= this.readData(dataModel);
		view.addMessage(masData.toString());
		///////////////
		// setup mas //
		///////////////
		// setup environment
		
		this.roadNetwork 	= TrafficMASController.setupRoadNetwork(dataModel);
		view.addMessage("Initialized roadnetwork");

		this.routes 		= TrafficMASController.setupRoutes(dataModel,roadNetwork);
		view.addMessage("Initialized routes");

		// setup agents & organizations
		this.agentsAndTime 	= TrafficMASController.instantiateAgents(masData, rng, routes);
		view.addMessage("Generated agent spawn times");

		this.organisations 	= TrafficMASController.instantiateOrganisations(masData);
		view.addMessage("Initialized organisations");

		//////////////////////
		// setup simulation //
		//////////////////////
		this.completeAgentMap 	= TrafficMASController.setupSimulation(masData, simulationModel, agentsAndTime);
		view.addMessage("Simulation is set up");

		this.currentAgentMap 	= new LinkedHashMap<String, Agent>();
		
		////////////////
		// setup view //
		////////////////
		TrafficMASController.setupView(view);
		TrafficMASController.updateView(view, roadNetwork, currentAgentMap.values(), organisations);
		view.addMessage("View is set up and updated");

		view.visualize();
	}

	/**
	 * Uses dataModel.instantiateRoadNetwork() to return a RoadNetwork according to the XML files read by Datamodel.
	 * @param dataModel
	 * @return a RoadNetwork filled with all Edge and Node objects
	 */
	public static RoadNetwork setupRoadNetwork(DataModel dataModel) {
		return dataModel.instantiateRoadNetwork();
	}
	
	/**
	 * Uses dataModel.getRoutes() to return an ArrayList<Route> according to the XML files read by Datamodel.
	 * @param dataModel
	 * @return a list of routes which agents can follow.
	 */
	public static ArrayList<Route> setupRoutes(DataModel dataModel, RoadNetwork roadNetwork) {
		return dataModel.getRoutes(roadNetwork);
	}

	/**
	 * Main Loop, updates simulationModel and View.
	 * @param dataModel
	 * @param simulationModel
	 * @param view
	 * @throws Exception 
	 */
	public void run(DataModel dataModel, SimulationModel simulationModel, TrafficView view) throws Exception {
		int i = 1;
		view.addMessage("Starting main loop");
		while(i++ < masData.simulationLength) {
			doStep(simulationModel, view, i);
		}
		
		TrafficMASController.cleanUp(dataModel, simulationModel, view);
	}

	public void doStep(SimulationModel simulationModel, TrafficView view, int i)
			throws Exception {
		long start_time = System.nanoTime();
		long total_start_time = start_time;
		StateData simulationStateData = TrafficMASController.nextSimulationState(simulationModel);
		view.addMessage("+++++++++++++++++++++");
		long end_time = System.nanoTime();
		double difference = (end_time - start_time)/1e6;

		view.addMessage("Timestep: "+i);
		view.addMessage("Simulation timestep: "+simulationStateData.currentTimeStep);
		view.addMessage("sim next state duration:"+difference+"ms");
		view.addMessage("Number of agents in sim:"+simulationStateData.agentsData.size());

		this.updateMAS(simulationStateData); 
		view.addMessage("Number of agents in MAS:"+currentAgentMap.size());

		HashMap<Agent,AgentAction> agentActions = this.nextMASState(simulationStateData.currentTimeStep/1000);
		view.addMessage("Agent actions: "+agentActions);
		
		start_time = System.nanoTime();
		TrafficMASController.updateSimulation(simulationModel, agentActions);
		end_time = System.nanoTime();
		difference = (end_time - start_time)/1e6;
		view.addMessage("sim update duration:"+difference+"ms");

		TrafficMASController.updateView(view, roadNetwork, currentAgentMap.values(), organisations);
		end_time = System.nanoTime();
		difference = (end_time - total_start_time)/1e6;
		view.addMessage("duration:"+difference+"ms");
		view.addMessage("+++++++++++++++++++++");
		
		view.visualize();
	}

	
	// TODO: Write test for this function
	/**
	 * Generates a map containing the best actions for every Agent alive at that timestep.
	 * @param currentAgents
	 * @return a map containing an agent as key and an AgentAction as value.
	 */
	public static HashMap<Agent, AgentAction> getAgentActions(int currentTime, HashMap<String, Agent> currentAgents) {
		
		HashMap <Agent,AgentAction> actions = new LinkedHashMap<Agent, AgentAction>();
		for(Agent agent : currentAgents.values()) {
			actions.put(agent, agent.doAction(currentTime));
		}
		
		return actions;
	}
	

	/**
	 * This function uses dataModel.getMASData() to return  the MASData.
	 * @param dataModel
	 * @return the MASData data structure which contains  includes simulationLength, sumoConfigPath, spawnProbability and AgentProfileTypeDistribution.
	 */
	private MASData readData(DataModel dataModel) {
		return dataModel.getMASData();
	}
	
	/**
	 * Sets up the simulation. The SUMO application is started via TraaS with certain options and all SumoCommands to add Agents are sent.
	 * @param masData
	 * @param simulationModel
	 * @param agentsAndTime
	 * @return a map of all Agent objects that will act in the simulation.
	 */
	public static HashMap<String,Agent> setupSimulation(MASData masData, SimulationModel simulationModel, HashMap<Agent,Integer> agentsAndTime) {
		// start the simulation
		HashMap<String, String> optionValueMap = new LinkedHashMap<String, String>();
		optionValueMap.put("e", Integer.toString(masData.simulationLength));
		optionValueMap.put("start", "1");
		optionValueMap.put("quit-on-end", "1");
		simulationModel.initializeWithOptions(optionValueMap);
		// add the agents
		
		return simulationModel.addAgents(agentsAndTime);
	}
	
	/**
	 * Sets up the GUI by calling view.initialize().
	 * @param view
	 */
	public static void setupView(TrafficView view) {
		view.initialize();
	}
	
	/**
	 * Updates the MAS with the 'simulationStateData'. Agents, Edges and Lanes are updated.
	 * @param simulationStateData
	 */
	private void updateMAS(StateData simulationStateData) {
		roadNetwork 	= TrafficMASController.updateRoadNetwork(roadNetwork, simulationStateData);
		currentAgentMap = TrafficMASController.updateAgents(completeAgentMap, roadNetwork, simulationStateData);
	}
	
	/**
	 * Updates the Road Network 'roadNetwork' with information from 'simulationStateData. 
	 * For both Road and Lane objects, the meanTravelTime and meanSpeed are updated
	 * @param roadNetwork
	 * @param simulationStateData
	 * @return an updated RoadNetwork object.
	 */
	public static RoadNetwork updateRoadNetwork(RoadNetwork roadNetwork,
			StateData simulationStateData) {
		
		for(Edge edge : roadNetwork.getEdges()) {
			//simulationStateData.edgesData.
			double meanTravelTimeEdge = simulationStateData.edgesData.get(edge.getID()).meanTime;
			double meanSpeedEdge = simulationStateData.edgesData.get(edge.getID()).meanSpeed;

			edge.getRoad().setMeanTravelTime(meanTravelTimeEdge); 
			edge.getRoad().setMeanSpeedEdge(meanSpeedEdge); 
			
			for(Lane lane: edge.getRoad().getLanes()) {
				double meanTravelTimeLane = simulationStateData.lanesData.get(lane.getID()).meanTime;
				double meanSpeedLane = simulationStateData.lanesData.get(lane.getID()).meanSpeed;

				lane.setMeanTravelTime(meanTravelTimeLane);
				lane.setLaneMeanSpeed(meanSpeedLane);
			}
		}
		return roadNetwork;
	}

	/**
	 * Calculates the next step for the MAS. Organisations execute their actions, Agent actions are sent to SUMO.
	 * Not yet completely implemented.	 
	 * @return 
	 */
	private HashMap<Agent, AgentAction> nextMASState(int currentTime) {
		//TODO: Organization sanctions
		
		return  getAgentActions(currentTime, this.currentAgentMap);
	}
	
	/**
	 * Updates the information all agents in 'completeAgentMap' of which data was returned in 'stateData'. 
	 * @param completeAgentMap
	 * @param roadNetwork
	 * @param stateData
	 * @return a map of every agent in the simulation, some of which have been updated.
	 */
	public static HashMap<String, Agent> updateAgents(HashMap<String,Agent> completeAgentMap, RoadNetwork roadNetwork, StateData stateData){
		HashMap<String, Agent> agentsMap = new LinkedHashMap<String, Agent>();
		for(String agentID : stateData.agentsData.keySet()) {
			if(!agentsMap.containsKey(agentID)) {
				Agent agent = completeAgentMap.get(agentID);
				updateAgent(roadNetwork, stateData, agentID, agent,completeAgentMap);
				agentsMap.put(agentID, agent);
			}
		}
		return agentsMap;
	}
	/**
	 * Updates the agent 'agent' with id 'agentID', 'stateData' contains the new information with which the agent is updated. 
	 * @param roadNetwork
	 * @param stateData
	 * @param agentID
	 * @param agent
	 */
	public static void updateAgent(RoadNetwork roadNetwork,
			StateData stateData, String agentID, Agent agent, HashMap<String,Agent> completeAgentMap  ) {
		AgentData agentData 	= stateData.agentsData.get(agentID);
		double velocity 		= agentData.speed;
		String roadID 			= agentData.roadID;
		String leaderID			= agentData.leaderId;
		Agent leaderAgent		= null;
		double leaderVelocity	= -1;
		double leaderDistance	= agentData.leaderDistance;
		
		if(leaderDistance >= 0) {
			leaderAgent = completeAgentMap.get(leaderID);
			leaderVelocity = leaderAgent.getVelocity();
		}
		
		Road road 		= roadNetwork.getRoadFromID(roadID);
		
		int laneIndex 	= agentData.laneIndex;
		double distance = agentData.position;
	
		// Update the agent with information
		agent.setVelocity(velocity);
		agent.setRoad(road);
		agent.setLane(road.getLanes()[laneIndex]);
		
		agent.setLeader(leaderVelocity, leaderDistance);
		agent.setDistance(distance);
		
		//TODO review this
		//TODO review test (expectedArrivalTime etc)
		double roadLengthRemaining		= agent.getRoad().length - agent.getDistance();
		double timeLeftOnCurrentRoad 	= roadLengthRemaining/agent.getVelocity();
		double routeRemainderLength 	= Route.getRouteRemainderLength(agent.getRoute(), agent.getRoad());
		double expectedArrivalTime 		= stateData.currentTimeStep/1000 + timeLeftOnCurrentRoad + routeRemainderLength / agent.getMaxComfySpeed();
		
		agent.setExpectedArrivalTime(expectedArrivalTime);
	}

	/**
	 * Calls the method	simulationModel.simulateAgentActions(), if the map 'agentActions' is not empty.
	 * @param simulationModel
	 * @param agentActions   
	 * @throws Exception 
	 */
	public static void updateSimulation(SimulationModel simulationModel, HashMap<Agent, AgentAction> agentActions) throws Exception {
		if(agentActions != null) {
			simulationModel.simulateAgentActions(agentActions);
		}
	}
	
	public static StateData nextSimulationState(SimulationModel simulationModel) {
		long start_time = System.nanoTime();
		long end_time = System.nanoTime();
		double difference = (end_time - start_time)/1e6;
		System.out.println("Simulation timestep:"+difference);
		// do step and get new data
		return simulationModel.getNewStateData();
	}
	
	public static void updateView(TrafficView view, RoadNetwork roadNetwork, Collection<Agent> currentAgents, Collection<Organisation> organisations ) {
		view.updateFromRoadNetwork(roadNetwork);
		view.updateFromAgents(currentAgents);
		view.updateFromOrganisations(organisations);
		view.visualize();
	}
	
	public static void cleanUp(DataModel dataModel, SimulationModel simulationModel, TrafficView view) {
		dataModel.close();
		simulationModel.close();
		view.close();
	}
	
	/**
	 * Generates a Map of Agents and their respective starting times according to 'masData'. 
	 * An agent will be spawned on each route with chance masData.spawnProbability if > 0.
	 * Otherwise the individual spawn probabilities of the routes will be used.
	 * Randomness is determined by 'rng'.  
	 * @param masData
	 * @param rng
	 * @param routes
	 * @return a LinkedHashMap which contains all agents who will spawn during the simulation, and their respective spawn times, in order. 
	 */
	public static HashMap<Agent, Integer> instantiateAgents(MASData masData, Random rng, ArrayList<Route> routes){
		LinkedHashMap<Agent, Integer> agentsAndTimes = new LinkedHashMap<Agent, Integer>();
		int simulationLength = masData.simulationLength;
		
		// If spawnProbability is 0.0, the spawn probabilities of the roads is checked. 
		// Otherwise, the spawn probability is the same for every road.
		double agentSpawnProbability = masData.spawnProbability;
		
		
		// TODO adapt InstantiateAgentsTest
		// TODO write test for Vehicle.setRoute .
		HashMap<AgentProfileType, Double> agentProfileDistribution = masData.agentProfileTypeDistribution;
		for (int currentTime = 1; currentTime <= simulationLength; currentTime++) {
			double coinFlip = rng.nextDouble();
			if(agentSpawnProbability == 0.0){
				int i =0;
				for(Entry<String, Double> entry : masData.routeIdAndProbability.entrySet()){
					if(coinFlip < entry.getValue()){
						coinFlip = rng.nextDouble();
						createAgent(routes.get(i), agentsAndTimes, agentProfileDistribution, currentTime, coinFlip);
						i++;
					}
				}
			} else{
				if(coinFlip < agentSpawnProbability) {
					int i =0;
					for(Entry<String, Double> entry : masData.routeIdAndProbability.entrySet()){
						coinFlip = rng.nextDouble();
						createAgent(routes.get(i), agentsAndTimes, agentProfileDistribution, currentTime, coinFlip);
						i++;
					}
					/*
					coinFlip = rng.nextDouble();
					Route route = pickRoute(coinFlip, agentSpawnProbability, masData.routeIdAndProbability);
					createAgent(route, agentsAndTimes, agentProfileDistribution, currentTime, coinFlip);
					*/
				}
			}
		
		}
		return agentsAndTimes;
	}
	
	/**
	 * Creates an Agent object with its respective spawn time and puts it in the hashMap 'agentsAndTimes'.
	 * Agent is instantiated with an String ID in the form of "Agent n", a goalNode, a route from 'routes', a goal arrival time, a maximum speed, and the 'currentTime' (in seconds)
	 * The AgentProfileType is determined with by 'coinflip' and 'agentProfileDistribution'.
	 * @param routes
	 * @param agentsAndTimes
	 * @param agentProfileDistribution
	 * @param currentTime
	 * @param coinFlip
	 */
	public static void createAgent(Route route,
			LinkedHashMap<Agent, Integer> agentsAndTimes,
			HashMap<AgentProfileType, Double> agentProfileDistribution, int currentTime,
			double coinFlip) {
		AgentProfileType agentProfileType = selectAgentProfileType(coinFlip, agentProfileDistribution);
		int minimalTravelTime = 0;
		Edge[] routeEdges = route.getRoute();
		double maxComfySpeed = agentProfileType.getMaxComfortableDrivingSpeed(Agent.DEFAULT_MAX_SPEED);
		for(Edge routeEdge : routeEdges) {
			minimalTravelTime += Math.round(routeEdge.getRoad().length/maxComfySpeed);
		}
		int goalArrivalTime = agentProfileType.goalArrivalTime(currentTime, minimalTravelTime);
		
		Node goalNode = routeEdges[routeEdges.length-1].getToNode();
		Agent agent = agentProfileType.toAgent(Agent.getNextAgentID(), goalNode, route,  goalArrivalTime, Agent.DEFAULT_MAX_SPEED); //TODO: change this default max speed
		agentsAndTimes.put(agent,currentTime*1000); //*1000 because sumo counts in ms, not s.
	}
	
	/**
	 * Selects a certain AgentProfile according to the 'coinflip' (which is determined by an earlier rng value) and the 'agentProfileDistribution'.
	 * @param coinFlip
	 * @param agentProfileDistribution
	 * @return a AgentProfileType, currently either Normal, OldLady or HotShot.
	 */
	public static AgentProfileType selectAgentProfileType(double coinFlip, HashMap<AgentProfileType, Double> agentProfileDistribution) {
		for(Entry<AgentProfileType, Double> entry : agentProfileDistribution.entrySet()) {
			if(coinFlip < entry.getValue()) {
				return entry.getKey();
			}
			coinFlip -= entry.getValue();
		}
		return null;
	}
	
	/**
	 * Not yet Implemented.
	 * @param masData
	 * @return
	 */
	public static ArrayList<Organisation> instantiateOrganisations(MASData masData) {
		return null;
	}
}
