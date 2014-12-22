package nl.uu.trafficmas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import nl.uu.trafficmas.agent.Agent;
import nl.uu.trafficmas.agent.AgentPhysical;
import nl.uu.trafficmas.agent.AgentProfileType;
import nl.uu.trafficmas.agent.AgentType;
import nl.uu.trafficmas.agent.actions.AgentAction;
import nl.uu.trafficmas.datamodel.DataModel;
import nl.uu.trafficmas.datamodel.DataModelXML;
import nl.uu.trafficmas.datamodel.Pair;
import nl.uu.trafficmas.organisation.Organisation;
import nl.uu.trafficmas.roadnetwork.RoadNetwork;
import nl.uu.trafficmas.roadnetwork.Route;
import nl.uu.trafficmas.simulationmodel.SimulationModel;
import nl.uu.trafficmas.simulationmodel.SimulationModelTraaS;
import nl.uu.trafficmas.view.TrafficView;
import nl.uu.trafficmas.view.TrafficViewConsole;

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
		this.simulationModel.initializeWithOptions(optionValueMap);
		
		completeAgentMap = this.simulationModel.addAgents(agentsAndTime);

		
		this.view = view;
		updateView();
	}
	
	private void run() {
		int i = 0;

		while(i++ < SimulationLength) {
			System.out.println(i);
			/*for(Pair<Agent, Integer> val : agentsAndTime) {
				if(val.second == i*1000) {
					System.out.println(val.first.agentID + " is being added on " + i + "!" + "("+val.first.getClass().getCanonicalName() +")");
				}
			}*/
			currentAgentMap = this.simulationModel.updateCurrentAgentMap(completeAgentMap, currentAgentMap);
			roadNetwork = this.simulationModel.updateRoadNetwork(roadNetwork);
			HashMap<String, Agent> agentMap = this.simulationModel.updateAgents(roadNetwork, currentAgentMap);
			HashMap<String, Agent> leadingVehicles = this.simulationModel.getLeadingVehicles(agentMap);
			
			HashMap<String, AgentAction> actions = getAgentActions(currentAgentMap, leadingVehicles, i);
			
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
	public static HashMap<String, AgentAction> getAgentActions(HashMap<String, Agent> currentAgents, HashMap<String, Agent> leadingVehicles, int currentTime) {
		
		HashMap <String,AgentAction> actions = new HashMap<String, AgentAction>();
		for(Agent agent : currentAgents.values()) {
			actions.put(agent.agentID, agent.doAction(currentTime));
		}
		
		return actions;
	}
}
