package nl.uu.trafficmas;

import it.polito.appeal.traci.SumoTraciConnection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import de.tudresden.sumo.cmd.Vehicle;
import nl.uu.trafficmas.agent.Agent;
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
	public void addAgent(Agent agent, String routeID) {
		String agentID, vType;
		int depart;
		double pos, speed;
		byte lane;
		
		agentID = agent.getAgentID();
		vType = "Car";
		
		// TODO: Change this accordingly.
		depart = 1;
		
		
		try {
			conn.do_job_set(Vehicle.add(agentID, vType, routeID, depart, 0.0, 0.0, (byte) 0));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public ArrayList<AgentPhysical> getAgentPhysical() {
		ArrayList<AgentPhysical> agentPhysList = new ArrayList<AgentPhysical>();
		// TODO 
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
		conn = initializeWithOption(option, value, sumoBin, sumocfg);
	}
	
	
	public static SumoTraciConnection initializeWithOption(String option, String value, String sumoBin, String sumocfg){
		SumoTraciConnection conn = new SumoTraciConnection(sumoBin, sumocfg);

		
		conn.addOption(option, value);
		try {
			
			//start TraCI
			conn.runServer();
            //load routes and initialize the simulation
	        conn.do_timestep();
	        
        }catch(Exception ex){ex.printStackTrace();}
		return conn;
	}
}
