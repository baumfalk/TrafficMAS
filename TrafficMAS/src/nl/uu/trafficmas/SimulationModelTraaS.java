package nl.uu.trafficmas;

import it.polito.appeal.traci.SumoTraciConnection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import nl.uu.trafficmas.agent.AgentAction;
import nl.uu.trafficmas.agent.AgentPhysical;

public class SimulationModelTraaS implements SimulationModel {

	SumoTraciConnection conn;
	String sumocfg;
	String sumoBin;
	
	public SimulationModelTraaS(String sumoBin, String sumocfg){
		this.sumoBin = sumoBin;
		this.sumocfg = sumocfg;
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

	@Override
	public void initialize() {
		try {
			conn = initialize(sumoBin, sumocfg);
	        
        }catch(Exception ex){ex.printStackTrace();}
	}
	
	
	public static SumoTraciConnection initialize(String sumoBin, String sumocfg){
		SumoTraciConnection conn = new SumoTraciConnection(sumoBin, sumocfg);
		
		try {

			//start TraCI
			conn.runServer();
            //load routes and initialize the simulation
	        conn.do_timestep();
	        
        }catch(Exception ex){ex.printStackTrace();}
		
		return conn;
	}
	
	@Override
	public void initializeWithOption(String option, String value) {
		conn = new SumoTraciConnection(this.sumoBin, this.sumocfg);

		
		conn.addOption(option, value);
		try {
			
			//start TraCI
			conn.runServer();
            //load routes and initialize the simulation
	        conn.do_timestep();
	        
        }catch(Exception ex){ex.printStackTrace();}
	}
}
