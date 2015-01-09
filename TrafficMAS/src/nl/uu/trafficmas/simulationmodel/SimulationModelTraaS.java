package nl.uu.trafficmas.simulationmodel;

import it.polito.appeal.traci.SumoTraciConnection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import nl.uu.trafficmas.agent.Agent;
import nl.uu.trafficmas.agent.AgentProfileType;
import nl.uu.trafficmas.agent.actions.AgentAction;
import de.tudresden.sumo.cmd.Simulation;
import de.tudresden.sumo.cmd.Vehicle;
import de.tudresden.sumo.util.SumoCommand;
import de.tudresden.ws.container.SumoColor;
import de.tudresden.ws.container.SumoStringList;

public class SimulationModelTraaS implements SimulationModel {

	SumoTraciConnection conn;
	String sumocfg;
	String sumoBin;
	public static final int LOOK_AHEAD_DISTANCE = 100;
	public static final int OVERTAKE_DURATION = 5;
	
	public SimulationModelTraaS(String sumoBin, String sumocfg){
		this.sumoBin = sumoBin;
		this.sumocfg = sumocfg;
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
	public void initializeWithOptions(HashMap<String,String> optionValueMap) {
		conn = initializeWithOptions(optionValueMap, sumoBin, sumocfg);
	}
	public static SumoTraciConnection initializeWithOptions(HashMap<String,String> optionValueMap, String sumoBin, String sumocfg){
		SumoTraciConnection conn = new SumoTraciConnection(sumoBin, sumocfg);
		// Add an extra option.
		for(Entry<String, String> keyValue : optionValueMap.entrySet()) {
			conn.addOption(keyValue.getKey(), keyValue.getValue());
		}
		try {	
			//start TraCI
			conn.runServer();
	       //load routes and initialize the simulation
			conn.do_timestep();    
       }catch(Exception ex){ex.printStackTrace();}
		
		return conn;
	}

	@Override
	public void close() {
		conn.close();
	}
	
	@Override
	public void doTimeStep() {
		try {
			conn.do_timestep();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void addAgent(Agent agent, String routeID, int tick) {
		addAgent(agent, routeID, tick, conn);
	}
	
	public static void addAgent(Agent agent, String routeID, int tick, SumoTraciConnection conn){
		try {
			conn.do_job_set(addAgentCommand(agent, routeID, tick));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static SumoCommand addAgentCommand(Agent agent, String routeID, int tick) {
		if( agent.getAgentType() == AgentProfileType.Normal){
			return Vehicle.add(agent.agentID, "Car", routeID, tick, 0.0, Math.min(agent.getMaxComfySpeed(),10), (byte) 0);
		} else if( agent.getAgentType() == AgentProfileType.OldLady){
			return Vehicle.add(agent.agentID, "Car", routeID, tick, 0.0, Math.min(agent.getMaxComfySpeed(),10), (byte) 0);
		} else if( agent.getAgentType() == AgentProfileType.PregnantWoman){
			return Vehicle.add(agent.agentID, "Car", routeID, tick, 0.0, Math.min(agent.getMaxComfySpeed(),10), (byte) 0);
		}
		return Vehicle.add(agent.agentID, "Car", routeID, tick, 0.0, Math.min(agent.getMaxComfySpeed(),10), (byte) 0);
	}
	
	@Override
	public HashMap<String, Agent> addAgents(HashMap<Agent, Integer> agentPairList) {
		
		return addAgents(agentPairList, conn);
	}
	//TODO: Implement routes.
	public static HashMap<String, Agent> addAgents(HashMap<Agent, Integer> agentPairList, SumoTraciConnection conn){
		HashMap<String, Agent> completeAgentMap = new LinkedHashMap<String, Agent>();
		ArrayList<SumoCommand> cmds = new ArrayList<>();
		
		try {
			for( Entry<Agent, Integer> agentPair : agentPairList.entrySet()){
				cmds.add(addAgentCommand(agentPair.getKey(), "route0", agentPair.getValue()));
				cmds.add(Vehicle.setLaneChangeMode(agentPair.getKey().agentID, 0b0001000000));
				cmds.add(Vehicle.setSpeedMode(agentPair.getKey().agentID, 0b00000));
				cmds.add(Vehicle.setMaxSpeed(agentPair.getKey().agentID, agentPair.getKey().getMaxComfySpeed()));
				
				// TODO: Add color property to Agents.
				if( agentPair.getKey().getAgentType() == AgentProfileType.Normal){
					// Normal is red
					cmds.add(Vehicle.setColor(agentPair.getKey().agentID, new SumoColor(255,0,0,255)));
				} else if( agentPair.getKey().getAgentType() == AgentProfileType.OldLady){
					// OldLady is green
					cmds.add(Vehicle.setColor(agentPair.getKey().agentID, new SumoColor(0,255,0,255)));
				} else if( agentPair.getKey().getAgentType() == AgentProfileType.PregnantWoman){
					// PregnantWoman is blue
					cmds.add(Vehicle.setColor(agentPair.getKey().agentID, new SumoColor(0,0,255,255)));
				}
				completeAgentMap.put(agentPair.getKey().agentID, agentPair.getKey());
			}
	
			if(agentPairList.size() > 0){
				conn.do_jobs_set(cmds);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return completeAgentMap;
	}
	
	@Override
	public HashMap<String, Agent> updateCurrentAgentMap(HashMap<String, Agent> completeAgentMap, HashMap<String, Agent> oldAgentMap) {
		return updateCurrentAgentMap(completeAgentMap, oldAgentMap, conn);
	}
	public static HashMap<String, Agent> updateCurrentAgentMap(HashMap<String, Agent> completeAgentMap, HashMap<String, Agent> oldAgentMap, SumoTraciConnection conn){
		HashMap<String, Agent> currentAgentMap = new HashMap<String, Agent>(oldAgentMap);
		try {
			
			ArrayList<SumoCommand> cmdList = new ArrayList<>();
			cmdList.add(Simulation.getArrivedIDList());
			cmdList.add(Simulation.getDepartedIDList());
			ArrayList<Object> responses = conn.do_jobs_get(cmdList);
			
			SumoStringList departedVehicleIDList = (SumoStringList) responses.get(0);
			SumoStringList arrivedVehicleIDList = (SumoStringList) responses.get(1);			
			
			for(String departedVehicleID: departedVehicleIDList){
				currentAgentMap.remove(departedVehicleID);
			}

			for(String arrivedVehicleID: arrivedVehicleIDList){
				currentAgentMap.put(arrivedVehicleID, completeAgentMap.get(arrivedVehicleID));

			}
			
		} catch (Exception e){
			e.printStackTrace();
		}
		return currentAgentMap;
	}
	
	@Override
	public void prepareAgentActions(HashMap<String, AgentAction> actions, HashMap<String, Agent> currentAgentMap) {
		prepareAgentActions(actions, currentAgentMap, conn);
	}
	
	public static void prepareAgentActions(HashMap<String, AgentAction> actions, HashMap<String, Agent> currentAgentMap, SumoTraciConnection conn){
		if(actions.size() == 0 && currentAgentMap.size()==0) {
			return;
		}
		
		ArrayList<SumoCommand> cmdList = new ArrayList<SumoCommand>();
		
		
		for(Map.Entry<String, AgentAction> entry: actions.entrySet()){
			
			Agent currentAgent = currentAgentMap.get(entry.getKey());
			byte agentLaneIndex = currentAgent.getLane().laneIndex;
			int maxLaneIndex = currentAgent.getRoad().laneList.size()-1;
		
			if(entry.getValue() == null)
				continue;
			switch(entry.getValue()) {
			case ChangeLane:
				if(agentLaneIndex < maxLaneIndex){
					cmdList.add(Vehicle.changeLane(entry.getKey(), (byte) (agentLaneIndex+1) , OVERTAKE_DURATION));
				} else {
					//TODO exceptions.
				}
				break;
			case ChangeRoad:
				// TODO
				break;
			case ChangeVelocity5:
				cmdList.add(Vehicle.slowDown(entry.getKey(), currentAgent.getVelocity()+5.0,5));
				break;
			case ChangeVelocity10:
				cmdList.add(Vehicle.slowDown(entry.getKey(), currentAgent.getVelocity()+10.0,10));
				break;
			case ChangeVelocity20:
				cmdList.add(Vehicle.slowDown(entry.getKey(), currentAgent.getVelocity()+20.0,15));
				break;
			default:
				System.out.println("Error on action name, no action executed");			
		
			}
		}
		
	
		try {
			if(cmdList.size() != 0) {
				conn.do_jobs_set(cmdList);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public StateData getNewStateData() {
		return getStateData(conn,true);
	}

	public static StateData getStateData(SumoTraciConnection conn, boolean timeStep) {
		StateData stateData = null;
		QueryBuilder qb = new QueryBuilder();
		if(timeStep) {
			qb.doNextTimeStep();
		}
		qb.addQuerySubject(QuerySubject.Vehicle);
		qb.addQuerySubject(QuerySubject.Edge);
		qb.addQuerySubject(QuerySubject.Lane);

		try {
			qb.addQueryField(QuerySubject.Vehicle, QueryField.Position);
			qb.addQueryField(QuerySubject.Vehicle, QueryField.Speed);
			qb.addQueryField(QuerySubject.Vehicle, QueryField.LeadingVehicle);
			qb.addQueryField(QuerySubject.Vehicle, QueryField.EdgeId);
			qb.addQueryField(QuerySubject.Vehicle, QueryField.LaneIndex);
	
			qb.addQueryField(QuerySubject.Edge, QueryField.MeanSpeed);
			qb.addQueryField(QuerySubject.Edge, QueryField.MeanTime);
			qb.addQueryField(QuerySubject.Edge, QueryField.MeanTime);

			qb.addQueryField(QuerySubject.Lane, QueryField.MeanSpeed);
			qb.addQueryField(QuerySubject.Lane, QueryField.EdgeId);

			qb.executeQuery(conn);
			stateData = qb.getStateData();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return stateData;
	}

	@Override
	public void simulateAgentActions(HashMap<Agent, AgentAction> agentActions) {
		
	}
}
