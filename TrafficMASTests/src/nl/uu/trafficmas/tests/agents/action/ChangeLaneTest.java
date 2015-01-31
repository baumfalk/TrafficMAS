package nl.uu.trafficmas.tests.agents.action;

import static org.junit.Assert.*;
import it.polito.appeal.traci.SumoTraciConnection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Random;
import java.util.Map.Entry;

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

public class ChangeLaneTest {

	@Test
	public void test() throws SAXException, IOException, ParserConfigurationException {
		//fail("Not implemented");
		Random random 	= new Random(1337);
		String dir 		= System.getProperty("user.dir")+"/tests/AgentActions/ChangeLane/";
		String sumocfg 	= System.getProperty("user.dir")+"/tests/AgentActions/ChangeLane/ConfigTest.xml";
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
				if(i==10){
					HashMap<Agent, AgentAction> actions = new HashMap<Agent, AgentAction>();
					for(Entry<String, Agent> entry : currentAgentMap.entrySet()){
	
					}
				}
			}
			assertEquals(4, currentAgentMap.size());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
