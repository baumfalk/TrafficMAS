package nl.uu.trafficmas.tests.agents.action;

import static org.junit.Assert.*;
import it.polito.appeal.traci.SumoTraciConnection;

import java.awt.List;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import javax.xml.parsers.ParserConfigurationException;

import nl.uu.trafficmas.agent.Agent;
import nl.uu.trafficmas.agent.actions.AgentAction;
import nl.uu.trafficmas.controller.TrafficMASController;
import nl.uu.trafficmas.datamodel.DataModel;
import nl.uu.trafficmas.datamodel.DataModelXML;
import nl.uu.trafficmas.datamodel.MASData;
import nl.uu.trafficmas.roadnetwork.RoadNetwork;
import nl.uu.trafficmas.roadnetwork.Route;
import nl.uu.trafficmas.simulationmodel.SimulationModelTraaS;

import org.junit.Test;
import org.xml.sax.SAXException;

import de.tudresden.sumo.cmd.Vehicle;
import de.tudresden.ws.container.SumoStringList;

public class ChangeRoadTest {

	@Test
	public void changeRoad() throws SAXException, IOException, ParserConfigurationException {
		//fail("not implemented");
		Random random 	= new Random(1337);
		String dir 		= System.getProperty("user.dir")+"/tests/AgentActions/ChangeRoad/";
		String sumocfg 	= System.getProperty("user.dir")+"/tests/AgentActions/ChangeRoad/ConfigTest.xml";
		String masXML 	= "MASTest.xml";
		
		DataModel dataModel = new DataModelXML(dir,masXML);
		MASData masData		= dataModel.getMASData();
		
		HashMap<String, String> options = new LinkedHashMap<String, String>();
		options.put("e", Integer.toString(masData.simulationLength));
		options.put("start", "1");
		options.put("quit-on-end", "1");
		
		SumoTraciConnection conn = SimulationModelTraaS.initializeWithOptions(options,"sumo", sumocfg);				
		RoadNetwork rn = dataModel.instantiateRoadNetwork();
		ArrayList<Route> routes = dataModel.getRoutes(rn);
		
		HashMap<Agent,Integer> agentPairList 	= TrafficMASController.instantiateAgents(masData, random, routes);
		HashMap<String, Agent> completeAgentMap = SimulationModelTraaS.addAgents(agentPairList, conn);
		HashMap<String, Agent> currentAgentMap 	= SimulationModelTraaS.updateCurrentAgentMap(completeAgentMap, new LinkedHashMap<String, Agent>(), conn);
	
		try {
			int i = 0;
			while (i++ < masData.simulationLength) {
				boolean timeStep = true;
				SimulationModelTraaS.getStateData(conn, timeStep);
				currentAgentMap = SimulationModelTraaS.updateCurrentAgentMap(completeAgentMap, currentAgentMap, conn);
				
				SumoStringList route = new SumoStringList();
				
				//route.add("edge3");
				//route.add("edge0");
				if(i==10){
					HashMap<Agent, AgentAction> actions = new HashMap<Agent, AgentAction>();
					for(Entry<String, Agent> entry : currentAgentMap.entrySet()){
						route = (SumoStringList) conn.do_job_get(Vehicle.getRoute(entry.getValue().agentID));
						System.out.println(route.get(0));
						conn.do_job_set(Vehicle.setRoute(entry.getValue().agentID, route));
						
					}
					SimulationModelTraaS.simulateAgentActions(actions, conn);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
