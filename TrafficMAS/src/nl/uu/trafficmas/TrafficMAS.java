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
	private ArrayList<Pair<Agent, Integer>> agents;
	private ArrayList<Organisation> organisations;
	private double agentSpawnProbability;
	private ArrayList<Pair<AgentProfileType, Double>> agentTypeDistribution;
	private Random rng;
	
	private HashMap<String,Agent> completeAgentMap = new HashMap<String, Agent>();

		
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
		this.simulationModel.initialize();
		
		roadNetwork = this.dataModel.instantiateRoadNetwork();
		
		agents = new ArrayList<Pair<Agent, Integer>>();
		agentSpawnProbability = this.dataModel.getAgentSpawnProbability();
		agentTypeDistribution = this.dataModel.getAgentProfileTypeDistribution();
		agents = this.dataModel.instantiateAgents();
		// This should be no problem with next commit pulled.
		completeAgentMap = this.simulationModel.addAgents(agents);
		
		organisations = this.dataModel.instantiateOrganisations();
		
		this.view = view;
		updateView();
	}
	
	private void run() {
		int i = 0;
		HashMap<String, Agent> currentAgentMap = this.simulationModel.updateCurrentAgentMap(completeAgentMap, null);

		while(i++ < 100) {
			currentAgentMap = this.simulationModel.updateCurrentAgentMap(completeAgentMap, currentAgentMap);
			HashMap<String, AgentPhysical> aPhysMap = this.simulationModel.updateAgentsPhys(roadNetwork, currentAgentMap);
			HashMap<String, AgentPhysical> leadingVehicles = this.simulationModel.getLeadingVehicles(aPhysMap);
			HashMap<String, AgentAction> actions = this.getAgentActions(aPhysMap,leadingVehicles);
			
			this.simulationModel.prepareAgentActions(actions);
			this.simulationModel.doTimeStep();
		}
		this.simulationModel.close();
	}

	private void updateView() {
		this.view.updateFromRoadNetwork(roadNetwork);
		//this.view.updateFromAgents(agents);
		this.view.updateFromOrganisations(organisations);
		this.view.visualize();
	}
	private HashMap<String, AgentAction> getAgentActions(HashMap<String, AgentPhysical> aPhys, HashMap<String, AgentPhysical> leadingVehicles) {
		
		return null;
	}
}
