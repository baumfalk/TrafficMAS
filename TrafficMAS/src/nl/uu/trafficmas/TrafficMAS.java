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
	private ArrayList<Agent> activeAgents;
	private ArrayList<Pair<Integer,Agent>> agentsAndTime;
	private ArrayList<Organisation> organisations;
	private double agentSpawnProbability;
	private ArrayList<Pair<AgentProfileType, Double>> agentTypeDistribution;
	private Random rng;
	private ArrayList<Route> routes;
		
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
		
		agentsAndTime = new ArrayList<Pair<Integer,Agent>>();
		agentSpawnProbability = this.dataModel.getAgentSpawnProbability();
		agentTypeDistribution = this.dataModel.getAgentProfileTypeDistribution();
		routes = this.dataModel.getRoutes(roadNetwork);
		agentsAndTime = this.dataModel.instantiateAgents(rng,routes);
		activeAgents = new ArrayList<Agent>();
				
		organisations = this.dataModel.instantiateOrganisations();
		
		this.view = view;
		updateView();
	}
	
	private void run() {
		int i = 0;
		while(i++ < 100) {
			ArrayList<Agent> currentAgentList = this.simulationModel.updateCurrentAgentList();
			ArrayList<AgentPhysical> aPhys = this.simulationModel.updateAgentsPhys(roadNetwork, currentAgentList);
			HashMap<AgentPhysical, AgentPhysical> leadingVehicles = this.simulationModel.getLeadingVehicles();
			ArrayList<AgentAction> actions = this.getAgentActions(aPhys,leadingVehicles);
			
			this.simulationModel.prepareAgentActions(actions);
		}
		this.simulationModel.close();
	}

	private void updateView() {
		this.view.updateFromRoadNetwork(roadNetwork);
		this.view.updateFromAgents(activeAgents);
		this.view.updateFromOrganisations(organisations);
		this.view.visualize();
	}

	private ArrayList<AgentAction> getAgentActions(ArrayList<AgentPhysical> aPhys,
			HashMap<AgentPhysical, AgentPhysical> leadingVehicles) {
				return null;
	}
}
