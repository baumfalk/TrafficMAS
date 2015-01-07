package nl.uu.trafficmas.controller;

import it.polito.appeal.traci.SumoTraciConnection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import de.tudresden.sumo.cmd.Vehicle;
import de.tudresden.sumo.util.SumoCommand;
import nl.uu.trafficmas.agent.Agent;
import nl.uu.trafficmas.agent.AgentProfileType;
import nl.uu.trafficmas.agent.actions.AgentAction;
import nl.uu.trafficmas.datamodel.DataModel;
import nl.uu.trafficmas.datamodel.MASData;
import nl.uu.trafficmas.organisation.Organisation;
import nl.uu.trafficmas.roadnetwork.Edge;
import nl.uu.trafficmas.roadnetwork.Node;
import nl.uu.trafficmas.roadnetwork.Road;
import nl.uu.trafficmas.roadnetwork.RoadNetwork;
import nl.uu.trafficmas.roadnetwork.Route;
import nl.uu.trafficmas.simulationmodel.SimulationModel;
import nl.uu.trafficmas.simulationmodel.StateData;
import nl.uu.trafficmas.view.TrafficView;

public class TrafficMASController {

	private RoadNetwork roadNetwork;
	private HashMap<Agent, Integer> agentsAndTime;
	private ArrayList<Organisation> organisations;
	private Random rng;
	
	private HashMap<String,Agent> completeAgentMap = new HashMap<String, Agent>();

	private ArrayList<Route> routes;
	private HashMap<String, Agent> currentAgentMap;
	private int SimulationLength;
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
	
		///////////////
		// load data //
		///////////////
		this.masData 		= this.readData(dataModel);
		
		///////////////
		// setup mas //
		///////////////
		// setup environment
		this.roadNetwork 	= this.setupRoadNetwork(dataModel);
		this.routes 		= this.setupRoutes(dataModel,roadNetwork);
		// setup agents & organizations
		this.agentsAndTime 	= TrafficMASController.instantiateAgents(masData, rng, routes);
		this.organisations 	= TrafficMASController.instantiateOrganisations(masData);
		
		//////////////////////
		// setup simulation //
		//////////////////////
		this.completeAgentMap 	= TrafficMASController.setupSimulation(masData, simulationModel, agentsAndTime);
		this.currentAgentMap 	= new HashMap<String, Agent>();
		
		////////////////
		// setup view //
		////////////////
		TrafficMASController.setupView(view);
		TrafficMASController.updateView(view, roadNetwork, currentAgentMap.values(), organisations);
	}

	private RoadNetwork setupRoadNetwork(DataModel dataModel) {
		return dataModel.instantiateRoadNetwork();
	}
	
	private ArrayList<Route> setupRoutes(DataModel dataModel, RoadNetwork roadNetwork) {
		return dataModel.getRoutes(roadNetwork);
	}

	public void run(DataModel dataModel, SimulationModel simulationModel, TrafficView view) {
		int i = 0;

		while(i++ < masData.simulationLength) {
			StateData simulationStateData = TrafficMASController.nextSimulationState(simulationModel);
			this.updateMAS(simulationStateData); 
			
			HashMap<Agent,AgentAction> agentActions = this.nextMASState();
			TrafficMASController.updateSimulation(simulationModel, agentActions);
			
			TrafficMASController.updateView(view, roadNetwork, currentAgentMap.values(), organisations);
		}
		TrafficMASController.cleanUp(dataModel, simulationModel, view);
	}

	
	// TODO: Write test for this function
	public static HashMap<Agent, AgentAction> getAgentActions(HashMap<String, Agent> currentAgents) {
		
		HashMap <Agent,AgentAction> actions = new HashMap<Agent, AgentAction>();
		for(Agent agent : currentAgents.values()) {
			actions.put(agent, agent.doAction());
		}
		
		return actions;
	}
	
	private MASData readData(DataModel dataModel) {
		return dataModel.getMASData();
	}
	
	public static HashMap<String,Agent> setupSimulation(MASData masData, SimulationModel simulationModel, HashMap<Agent,Integer> agentsAndTime) {
		// start the simulation
		HashMap<String, String> optionValueMap = new HashMap<String, String>();
		optionValueMap.put("e", Integer.toString(masData.simulationLength));
		optionValueMap.put("start", "1");
		optionValueMap.put("quit-on-end", "1");
		simulationModel.initializeWithOptions(optionValueMap);
		// add the agents
		
		return simulationModel.addAgents(agentsAndTime);
	}
	
	public static void setupView(TrafficView view) {
		view.initialize();
	}
	
	private  void updateMAS(StateData simulationStateData) {
		//TODO update Agents (currentTime etc)
		currentAgentMap = TrafficMASController.updateAgents(currentAgentMap, roadNetwork, simulationStateData);
	}
	
	private HashMap<Agent, AgentAction> nextMASState() {
		//TODO: Organization sanctions
		getAgentActions(this.currentAgentMap);
		
		return  getAgentActions(this.currentAgentMap);
	}
	
	public static HashMap<String, Agent> updateAgents(HashMap<String, Agent> currentAgentMap, RoadNetwork roadNetwork, StateData stateData){
		HashMap<String, Agent> agentsMap = new HashMap<String, Agent>(currentAgentMap);
		if(currentAgentMap.size() == 0) 
			return agentsMap;
		
			for(Map.Entry<String, Agent> agentMap : currentAgentMap.entrySet()){
				String agentID 	= agentMap.getKey();
				Agent agent 	= agentMap.getValue();
	
				// Retrieve all physical agent information from SUMO
				double velocity = stateData.agentsData.get(agentID).speed;
				String roadID 	= stateData.agentsData.get(agentID).roadID;
				Road road 		= roadNetwork.getRoadFromID(roadID);
				int laneIndex 	= stateData.agentsData.get(agentID).laneIndex;
				double distance = stateData.agentsData.get(agentID).position;
				
				// Update the agent with information
				agent.setVelocity(velocity);
				agent.setRoad(road);
				agent.setLane(road.getLanes()[laneIndex]);
				
				agent.setDistance(distance);
				double expectedArrivalTime = 0;
				Edge [] route = agent.getRoute();
				
				//TODO review this
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
			}
		return agentsMap;
	}

	public static void updateSimulation(SimulationModel simulationModel, HashMap<Agent, AgentAction> agentActions) {
		if(agentActions != null) {
			simulationModel.updateStateData(agentActions);
		}
	}
	
	public static StateData nextSimulationState(SimulationModel simulationModel) {
		simulationModel.doTimeStep();
		return simulationModel.getStateData();
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
	
	public static HashMap<Agent, Integer> instantiateAgents(MASData masData, Random rng, ArrayList<Route> routes){
		LinkedHashMap<Agent, Integer> agentsAndTimes = new LinkedHashMap<Agent, Integer>();
		int simulationLength = masData.simulationLength;
		double agentSpawnProbability = masData.spawnProbability;
		HashMap<AgentProfileType, Double> agentProfileDistribution = masData.agentProfileTypeDistribution;
		for (int currentTime = 1; currentTime <= simulationLength; currentTime++) {
			double coinFlip = rng.nextDouble();
			if(coinFlip < agentSpawnProbability) {
				coinFlip = rng.nextDouble();
				createAgent(routes, agentsAndTimes, agentProfileDistribution, currentTime,
						coinFlip);
			}
		}
		return agentsAndTimes;
	}

	private static void createAgent(ArrayList<Route> routes,
			LinkedHashMap<Agent, Integer> agentsAndTimes,
			HashMap<AgentProfileType, Double> agentProfileDistribution, int currentTime,
			double coinFlip) {
		AgentProfileType agentProfileType = selectAgentProfileType(coinFlip, agentProfileDistribution);
		int minimalTravelTime = 0;
		Edge[] routeEdges = routes.get(0).getRoute();
		double maxComfySpeed = agentProfileType.getMaxComfortableDrivingSpeed(Agent.DEFAULT_MAX_SPEED);
		for(Edge routeEdge : routeEdges) {
			minimalTravelTime += Math.round(routeEdge.getRoad().length/maxComfySpeed);
		}
		int goalArrivalTime = agentProfileType.goalArrivalTime(currentTime, minimalTravelTime);
		
		Node goalNode = routeEdges[routeEdges.length-1].getToNode();
		Agent agent = agentProfileType.toAgent(Agent.getNextAgentID(), goalNode, routeEdges,  goalArrivalTime, Agent.DEFAULT_MAX_SPEED,currentTime); //TODO: change this default max speed
		agentsAndTimes.put(agent,currentTime*1000); //*1000 because sumo counts in ms, not s.
	}
	
	public static AgentProfileType selectAgentProfileType(double coinFlip, HashMap<AgentProfileType, Double> agentProfileDistribution) {
		for(Entry<AgentProfileType, Double> entry : agentProfileDistribution.entrySet()) {
			if(coinFlip < entry.getValue()) {
				return entry.getKey();
			}
			coinFlip -= entry.getValue();
		}
		return null;
	}
	
	
	public static ArrayList<Organisation> instantiateOrganisations(MASData masData) {
		return null;
	}
}
