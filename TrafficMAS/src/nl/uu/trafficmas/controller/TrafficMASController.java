package nl.uu.trafficmas.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import nl.uu.trafficmas.agent.Agent;
import nl.uu.trafficmas.agent.AgentProfileType;
import nl.uu.trafficmas.agent.actions.AgentAction;
import nl.uu.trafficmas.datamodel.DataModel;
import nl.uu.trafficmas.datamodel.MASData;
import nl.uu.trafficmas.datamodel.Pair;
import nl.uu.trafficmas.organisation.Organisation;
import nl.uu.trafficmas.roadnetwork.RoadNetwork;
import nl.uu.trafficmas.roadnetwork.Route;
import nl.uu.trafficmas.simulationmodel.SimulationModel;
import nl.uu.trafficmas.simulationmodel.StateData;
import nl.uu.trafficmas.view.TrafficView;

public class TrafficMASController {
	private DataModel dataModel;
	private SimulationModel simulationModel;
	private TrafficView view;

	private RoadNetwork roadNetwork;
	private HashMap<Agent, Integer> agentsAndTime;
	private ArrayList<Organisation> organisations;
	private double agentSpawnProbability;
	private ArrayList<Pair<AgentProfileType, Double>> agentTypeDistribution;
	private Random rng;
	
	private HashMap<String,Agent> completeAgentMap = new HashMap<String, Agent>();

	private ArrayList<Route> routes;
	private HashMap<String, Agent> currentAgentMap;
	private int SimulationLength;
		
	public TrafficMASController(DataModel dataModel,SimulationModel simulationModel, TrafficView view) {
		this(dataModel,simulationModel,view,-1);
	}
	
	public TrafficMASController(DataModel dataModel,SimulationModel simulationModel, TrafficView view, long seed) {
		if(seed == -1) {
			this.rng = new Random();
		} else {
			this.rng = new Random(seed);
		}
		this.dataModel = dataModel;
		this.simulationModel = simulationModel;
		this.view = view;
		this.readData();
		this.setupMAS();
		this.setupSimulation();
		this.setupView();
		this.updateView();

		/*this.currentAgentMap = new HashMap<String, Agent>();
		
		routes = this.dataModel.getRoutes(roadNetwork);
		
		completeAgentMap = this.simulationModel.addAgents(agentsAndTime);
*/
	}
	
	public void run() {
		int i = 0;

		while(i++ < SimulationLength) {
			StateData simulationStateData = this.nextSimulationState();
			this.updateMAS(simulationStateData);
			
			StateData MASStateData = this.nextMASState();
			this.updateSimulation(MASStateData);
			
			this.updateView();
			System.out.println(i);
			/*for(Pair<Agent, Integer> val : agentsAndTime) {
				if(val.second == i*1000) {
					System.out.println(val.first.agentID + " is being added on " + i + "!" + "("+val.first.getClass().getCanonicalName() +")");
				}
			}
			currentAgentMap = this.simulationModel.updateCurrentAgentMap(completeAgentMap, currentAgentMap);
			roadNetwork = this.simulationModel.updateRoadNetwork(roadNetwork);
			HashMap<String, Agent> agentMap = this.simulationModel.updateAgents(roadNetwork, currentAgentMap);
			HashMap<String, Agent> leadingVehicles = this.simulationModel.getLeadingVehicles(agentMap);
			
			HashMap<String, AgentAction> actions = getAgentActions(currentAgentMap, leadingVehicles, i);
			
			this.simulationModel.prepareAgentActions(actions, currentAgentMap);
			this.simulationModel.doTimeStep();*/
		}
		this.cleanUp();
	}

	
	// TODO: Write test for this function
	public static HashMap<String, AgentAction> getAgentActions(HashMap<String, Agent> currentAgents, HashMap<String, Agent> leadingVehicles, int currentTime) {
		
		HashMap <String,AgentAction> actions = new HashMap<String, AgentAction>();
		for(Agent agent : currentAgents.values()) {
			actions.put(agent.agentID, agent.doAction(currentTime));
		}
		
		return actions;
	}
	
	private void readData() {
		dataModel.getMASData();
	}
	
	private void setupMAS() {
		roadNetwork = dataModel.getRoadNetwork();
		routes = dataModel.getRoutes(roadNetwork);
		this.instantiateAgents(dataModel);
		this.instantiateOrganisations(dataModel);
	}
	
	private void setupSimulation() {
		// start the simulation
		HashMap<String, String> optionValueMap = new HashMap<String, String>();
		optionValueMap.put("e", Integer.toString(SimulationLength));
		optionValueMap.put("start", "1");
		optionValueMap.put("quit-on-end", "1");
		this.simulationModel.initializeWithOptions(optionValueMap);
		// add the agents
		this.simulationModel.addAgents(agentsAndTime);
		
		
		completeAgentMap = this.simulationModel.addAgents(agentsAndTime);
	}
	
	private void setupView() {
		this.view.initialize();
	}
	
	private void updateMAS(StateData simulationStateData) {
		
	}
	
	private StateData nextMASState() {
		return null;
	}
	
	private void updateSimulation(StateData stateData) {
		this.simulationModel.updateStateData(stateData);
	}
	
	private StateData nextSimulationState() {
		this.simulationModel.doTimeStep();
		return null;
	}
	
	private void updateView() {
		this.view.updateFromRoadNetwork(roadNetwork);
		this.view.updateFromAgents(new ArrayList<Agent>(currentAgentMap.values()));
		this.view.updateFromOrganisations(organisations);
		this.view.visualize();
	}
	
	private void cleanUp() {
		this.simulationModel.close();
	}
	
	private void instantiateAgents(DataModel dataModel) {
		
	}
	
	private void instantiateOrganisations(DataModel dataModel) {
		
	}
}
