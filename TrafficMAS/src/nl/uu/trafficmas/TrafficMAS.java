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
		TrafficModel model 	= new TrafficModelXML(args[0],args[1],args[2]);
		TrafficView view 	= new TrafficViewConsole();
		TrafficMAS trafficMas = new TrafficMAS(model, view);
		trafficMas.run();		
	}

	private TrafficModel model;
	private RoadNetwork roadNetwork;
	private ArrayList<Agent> agents;
	private ArrayList<Organisation> organisations;
	private TrafficView view;
	
	public TrafficMAS(TrafficModel model, TrafficView view) {
		this.model = model;
		roadNetwork = this.model.instantiateRoadNetwork();
		agents = this.model.instantiateAgents();
		organisations = this.model.instantiateOrganisations();
		
		this.view = view;
		updateView();
	}
	
	private void run() {
		int i = 0;
		while(i++ < 1000) {
			ArrayList<AgentPhysical> aPhys = this.model.getAgentPhysical();
			HashMap<AgentPhysical, AgentPhysical> leadingVehicles = this.model.getLeadingVehicles();
			ArrayList<AgentAction> actions = this.getAgentActions(aPhys,leadingVehicles);
			this.model.executeAgentActions(actions);
			
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
