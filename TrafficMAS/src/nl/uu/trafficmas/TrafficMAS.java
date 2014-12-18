package nl.uu.trafficmas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import nl.uu.trafficmas.agent.Agent;
import nl.uu.trafficmas.agent.AgentPhysical;
import nl.uu.trafficmas.agent.AgentProfileType;
import nl.uu.trafficmas.agent.AgentType;
import nl.uu.trafficmas.agent.actions.AgentAction;
import nl.uu.trafficmas.organisation.Organisation;
import nl.uu.trafficmas.roadnetwork.RoadNetwork;
import nl.uu.trafficmas.roadnetwork.Route;

public class TrafficMAS {
	public static void main(String[] args) {
		if(args.length < 3) { 
			System.out.println("At least three arguments needed");
			System.exit(1);
		}
		String dir = args[0];
		String masXML = args[1];
		String sumoBin = args[2];
		long seed = -1;
		if(args.length >= 4) {
			seed = Integer.parseInt(args[3]);
		}
		
		DataModel dataModel 		= new DataModelXML(dir,masXML);
		SimulationModel simModel 	= new SimulationModelTraaS(sumoBin,dataModel.getSumoConfigPath());
		TrafficView view 			= new TrafficViewConsole();
		TrafficMAS trafficMas 		= new TrafficMAS(dataModel, simModel, view,seed);
		trafficMas.run();
	}

	private DataModel dataModel;
	private SimulationModel simulationModel;
	private TrafficView view;

	private RoadNetwork roadNetwork;
	private ArrayList<Pair<Agent,Integer>> agentsAndTime;
	private ArrayList<Organisation> organisations;
	private double agentSpawnProbability;
	private ArrayList<Pair<AgentProfileType, Double>> agentTypeDistribution;
	private Random rng;
	
	private HashMap<String,Agent> completeAgentMap = new HashMap<String, Agent>();

	private ArrayList<Route> routes;
	private HashMap<String, Agent> currentAgentMap;
	private int SimulationLength;
		
	public TrafficMAS(DataModel dataModel,SimulationModel simulationModel, TrafficView view) {
		this(dataModel,simulationModel,view,-1);
	}
	
	public TrafficMAS(DataModel dataModel,SimulationModel simulationModel, TrafficView view, long seed) {
		if(seed == -1) {
			this.rng = new Random();
		} else {
			this.rng = new Random(seed);
		}
		this.dataModel = dataModel;
		this.simulationModel = simulationModel;
		
		roadNetwork = this.dataModel.instantiateRoadNetwork();
		this.currentAgentMap = new HashMap<String, Agent>();

		agentsAndTime = new ArrayList<Pair<Agent,Integer>>();
		SimulationLength = this.dataModel.getSimulationLength();
		agentSpawnProbability = this.dataModel.getAgentSpawnProbability();
		agentTypeDistribution = this.dataModel.getAgentProfileTypeDistribution();
		SimulationLength = this.dataModel.getSimulationLength();
		
		routes = this.dataModel.getRoutes(roadNetwork);
		agentsAndTime = this.dataModel.instantiateAgents(rng,routes);
		organisations = this.dataModel.instantiateOrganisations();
		
		//Start TraaS with options
		HashMap<String, String> optionValueMap = new HashMap<String, String>();
		optionValueMap.put("e", Integer.toString(SimulationLength));
		optionValueMap.put("start", "1");
		optionValueMap.put("quit-on-end", "1");
		this.simulationModel.initializeWithOption(optionValueMap);
		
		completeAgentMap = this.simulationModel.addAgents(agentsAndTime);

		
		this.view = view;
		updateView();
	}
	
	private void run() {
		int i = 0;

		while(i++ < SimulationLength) {
			for(Pair<Agent, Integer> val : agentsAndTime) {
				if(val.second == i*1000) {
					System.out.println(val.first.agentID + " is being added on " + i + "!");
				}
			}
			currentAgentMap = this.simulationModel.updateCurrentAgentMap(completeAgentMap, currentAgentMap);
			
			HashMap<String, AgentPhysical> aPhysMap = this.simulationModel.updateAgentsPhys(roadNetwork, currentAgentMap);
			HashMap<String, AgentPhysical> leadingVehicles = this.simulationModel.getLeadingVehicles(aPhysMap);
			//TODO: fix that we get the next lane thing.
			HashMap<String, AgentAction> actions = this.getAgentActions(currentAgentMap, leadingVehicles, i, null);
			
			this.simulationModel.prepareAgentActions(actions, currentAgentMap);
			this.simulationModel.doTimeStep();
		}
		this.simulationModel.close();
	}

	private void updateView() {
		this.view.updateFromRoadNetwork(roadNetwork);
		this.view.updateFromAgents(new ArrayList<Agent>(currentAgentMap.values()));
		this.view.updateFromOrganisations(organisations);
		this.view.visualize();
	}
	
	// TODO: Write test for this function
	private HashMap<String, AgentAction> getAgentActions(HashMap<String, Agent> currentAgents, HashMap<String, AgentPhysical> leadingVehicles, int currentTime, HashMap<String,Double> agentMeanSpeedNextLane) {
		
		HashMap <String,AgentAction> actions = new HashMap<String, AgentAction>();
		for(Agent agent : currentAgents.values()) {
			actions.put(agent.agentID, agent.doAction(currentTime, agentMeanSpeedNextLane.get(agent.agentID)));
		}
		
		return actions;
	}
}
