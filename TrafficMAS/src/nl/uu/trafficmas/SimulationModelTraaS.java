package nl.uu.trafficmas;

import it.polito.appeal.traci.SumoTraciConnection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import de.tudresden.sumo.cmd.Vehicle;
import de.tudresden.ws.container.SumoStringList;
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
	public void addAgent(Agent agent, String routeID, int tick) {
		addAgent(agent, routeID, tick, conn);
	}
	
	public static void addAgent(Agent agent, String routeID, int tick, SumoTraciConnection conn){
		try {
			conn.do_job_set(Vehicle.add(agent.getAgentID(), "Car", routeID, tick, 0.0, 10.0, (byte) 0));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public ArrayList<AgentPhysical> getAgentPhysical() {
		return getAgentPhysical(conn);
	}
	
	public static ArrayList<AgentPhysical> getAgentPhysical(SumoTraciConnection conn){
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
		conn = initialize(sumoBin, sumocfg);     
	}
	
	
	public static SumoTraciConnection initialize(String sumoBin, String sumocfg){
		SumoTraciConnection conn = new SumoTraciConnection(sumoBin, sumocfg);
		// Add an extra option.
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
		// Add an extra option.
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
