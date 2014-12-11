package nl.uu.trafficmas;

import java.util.ArrayList;
import java.util.HashMap;

import nl.uu.trafficmas.agent.Agent;
import nl.uu.trafficmas.agent.AgentAction;
import nl.uu.trafficmas.agent.AgentPhysical;
import nl.uu.trafficmas.organisation.Organisation;
import nl.uu.trafficmas.roadnetwork.RoadNetwork;

public class TrafficMAS {
	public static void main(String[] args) {
		if(args.length < 3) { 
			System.out.println("At least three arguments needed");
			System.exit(1);
		}
		DataModel dataModel 		= new DataModelXML(args[0],args[1],args[2]);
		SimulationModel simModel 	= new SimulationModelTraaS();
		TrafficView view 			= new TrafficViewConsole();
		TrafficMAS trafficMas 		= new TrafficMAS(dataModel, simModel, view);
		trafficMas.run();		
	}

	private DataModel dataModel;
	private SimulationModel simulationModel;
	private TrafficView view;

	private RoadNetwork roadNetwork;
	private ArrayList<Agent> agents;
	private ArrayList<Organisation> organisations;
	
	public TrafficMAS(DataModel dataModel,SimulationModel simulationModel, TrafficView view) {
		this.dataModel = dataModel;
		this.simulationModel = simulationModel;
		roadNetwork = this.dataModel.instantiateRoadNetwork();
		agents = this.dataModel.instantiateAgents();
		organisations = this.dataModel.instantiateOrganisations();
		
		this.view = view;
		updateView();
	}
	
	private void run() {
		int i = 0;
		while(i++ < 1000) {
			ArrayList<AgentPhysical> aPhys = this.simulationModel.getAgentPhysical();
			HashMap<AgentPhysical, AgentPhysical> leadingVehicles = this.simulationModel.getLeadingVehicles();
			ArrayList<AgentAction> actions = this.getAgentActions(aPhys,leadingVehicles);
			this.simulationModel.executeAgentActions(actions);
			
			updateView();
		}
	}

	private void updateView() {
		this.view.updateFromRoadNetwork(roadNetwork);
		this.view.updateFromAgents(agents);
		this.view.updateFromOrganisations(organisations);
		this.view.visualize();
	}

	private ArrayList<AgentAction> getAgentActions(ArrayList<AgentPhysical> aPhys,
			HashMap<AgentPhysical, AgentPhysical> leadingVehicles) {
				return null;
	}

}
