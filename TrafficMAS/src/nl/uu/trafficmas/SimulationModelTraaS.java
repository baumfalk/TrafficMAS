package nl.uu.trafficmas;

import it.polito.appeal.traci.SumoTraciConnection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import de.tudresden.sumo.cmd.Vehicle;
import de.tudresden.ws.container.SumoStringList;
import nl.uu.trafficmas.agent.Agent;
import nl.uu.trafficmas.agent.AgentPhysical;
import nl.uu.trafficmas.agent.actions.AgentAction;
import nl.uu.trafficmas.roadnetwork.Lane;
import nl.uu.trafficmas.roadnetwork.Road;
import nl.uu.trafficmas.roadnetwork.RoadNetwork;

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
	public ArrayList<AgentPhysical> getAgentsPhysical(RoadNetwork rn) {
		return getAgentsPhysical(rn, conn);
	}
	
	
	// TODO: Optimize (use existing AgentPhysical List to update. In this way, LaneType is missing from the physicalAgent.
	// Maybe LaneType should not be a part of the physical agent, since it does not occur within SUMO.
	public static ArrayList<AgentPhysical> getAgentsPhysical(RoadNetwork rn, SumoTraciConnection conn){
		ArrayList<AgentPhysical> agentPhysList = new ArrayList<AgentPhysical>();
		try {
			SumoStringList agentIDs = (SumoStringList) conn.do_job_get(Vehicle.getIDList());
			
			// Loop over all agents
			for(String agentID : agentIDs){
				
				// Create a new physical agent
				AgentPhysical aPhys = new AgentPhysical();
				
				
				
				// Retrieve all physical agent information from SUMO
				double velocity = (double) conn.do_job_get(Vehicle.getSpeed(agentID));
				String roadID = (String) conn.do_job_get(Vehicle.getRoadID(agentID));
				Road road = rn.getRoadFromID(roadID);
				int laneIndex = (int) conn.do_job_get(Vehicle.getLaneIndex(agentID));
				double distance = (double) conn.do_job_get(Vehicle.getLanePosition(agentID));
				
				// Update the agent with information
				aPhys.setVelocity(velocity);
				aPhys.setRoad(road);
				aPhys.setLane(road.getLanes()[laneIndex]);
				aPhys.setDistance(distance);
				agentPhysList.add(aPhys);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return agentPhysList;
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
