package nl.uu.trafficmas;

import it.polito.appeal.traci.SumoTraciConnection;

import java.util.ArrayList;
import java.util.HashMap;

import nl.uu.trafficmas.agent.AgentAction;
import nl.uu.trafficmas.agent.AgentPhysical;

public class SimulationModelTraaS implements SimulationModel {

	SumoTraciConnection conn;
	String dir;
	String masXML;
	String sumoBin;
	
	public SimulationModelTraaS(String dir, String sumoBin){
		this.dir = dir;
		this.sumoBin = sumoBin;
		
		//conn = new SumoTraciConnection(this.sumoBin, config_file);
	}
	
	@Override
	public ArrayList<AgentPhysical> getAgentPhysical() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HashMap<AgentPhysical, AgentPhysical> getLeadingVehicles() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void executeAgentActions(ArrayList<AgentAction> actions) {
		// TODO Auto-generated method stub
		
	}
	
}
