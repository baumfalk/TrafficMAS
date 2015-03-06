package nl.uu.trafficmas.tests.norms;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import it.polito.appeal.traci.SumoTraciConnection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.xml.parsers.ParserConfigurationException;

import nl.uu.trafficmas.agent.Agent;
import nl.uu.trafficmas.agent.actions.AgentAction;
import nl.uu.trafficmas.controller.TrafficMASController;
import nl.uu.trafficmas.datamodel.DataModel;
import nl.uu.trafficmas.datamodel.DataModelXML;
import nl.uu.trafficmas.datamodel.MASData;
import nl.uu.trafficmas.norm.MergeNormScheme;
import nl.uu.trafficmas.roadnetwork.Node;
import nl.uu.trafficmas.roadnetwork.Road;
import nl.uu.trafficmas.roadnetwork.RoadNetwork;
import nl.uu.trafficmas.roadnetwork.Route;
import nl.uu.trafficmas.simulationmodel.AgentData;
import nl.uu.trafficmas.simulationmodel.SimulationModelTraaS;
import nl.uu.trafficmas.simulationmodel.StateData;

import org.junit.Test;
import org.xml.sax.SAXException;

import com.sun.swing.internal.plaf.synth.resources.synth_sv;

public class MergeNormTest {

	
	//@Test
	public void calcTest2() {
		
		double lambda		= 2;
		double c_zero_pos 	= 10;
		double c_one_pos 	= 20;
		double c_zero_v		= (100/3.6);
		double c_one_v		= (80/3.6);
		double accel		= Double.POSITIVE_INFINITY;
		double decel		= -5;
		double vprime1; 
		double vprime2;
		
		
		double test;
		
		double firstPartSqrt =  Math.pow((2*accel*c_zero_pos)+(2*lambda*accel*c_zero_v)+(2*c_zero_v*c_one_v),2);
		double secndPartSqrt = 2*lambda*c_zero_v*(-(2*accel*c_one_pos*c_zero_v)-(2*lambda*accel*c_zero_v*c_zero_v)-(c_zero_v*c_one_v*c_one_v));
		double afterSqrtPos	 = (2*accel*c_zero_pos)+(2*lambda*accel*c_zero_v)+(2*c_zero_v*c_one_v);
		double afterSqrtNeg	 = (2*accel*c_zero_pos)-(2*lambda*accel*c_zero_v)-(2*c_zero_v*c_one_v);

		
		
		vprime1 = (Math.sqrt(
				accel*accel*c_zero_pos*c_zero_pos
					+2*accel*c_zero_pos*c_zero_v*c_one_v
						-(2*accel*c_one_pos*c_zero_v*c_zero_v))
					+(accel*c_zero_pos)+(c_zero_v*c_one_v))/c_zero_v;
		
		vprime1 = (Math.sqrt(firstPartSqrt+secndPartSqrt)+afterSqrtPos)/(2*c_zero_v);
		vprime2 = -(Math.sqrt(firstPartSqrt+secndPartSqrt)-afterSqrtNeg)/(2*c_zero_v);
		
		//System.out.println("Test: "+ test);

		System.out.println("New speed car one for positive sqrt: "+ vprime1);
		System.out.println("New speed car one for negative sqrt: "+ vprime2);

		/*
		System.out.println("Current speed car zero: "+ c_zero_v);
		System.out.println("Current speed car one: "+ c_one_v);
		
		double c_one_v_new	=	(1/lambda*c_one_pos+c_zero_v)/(1+(1/lambda)*(c_zero_pos/c_zero_v));
		System.out.println("Speed for car one with " +lambda+ " seconds before collission at merge point: "+c_one_v_new+"m/s");
		System.out.println("New speed in km/h: " +c_one_v_new*3.6);
		System.out.println("Difference:" + (c_one_v_new-c_one_v));
		*/
		fail("Not implemented yet");
	}
	
	
	//@Test
	public void calcTest() {
		
		double lambda 		= Double.POSITIVE_INFINITY; 
		double c_zero_pos 	= 10;
		double c_one_pos 	= 20;
		double c_zero_v		= (100/3.6);
		double c_one_v		= (80/3.6);
		
		System.out.println("Current speed car zero: "+ c_zero_v);
		System.out.println("Current speed car one: "+ c_one_v);
		
		double c_one_v_new	=	(1/lambda*c_one_pos+c_zero_v)/(1+(1/lambda)*(c_zero_pos/c_zero_v));
		System.out.println("Speed for car one with " +lambda+ " seconds before collission at merge point: "+c_one_v_new+"m/s");
		System.out.println("New speed in km/h: " +c_one_v_new*3.6);
		System.out.println("Difference:" + (c_one_v_new-c_one_v));
		
		fail("Not implemented yet");
	}
	
	public double calcLambda(double v0, double v1, double vprime, double s0, double s1, double a1 ) {
		System.out.println("CalcLambdaFunction\nv0:"+v0+"\nv1:"+v1+"\nvprime:"+vprime+"\ns0:"+s0+"\ns1:"+s1+"\na1:"+a1);
		double decelTime = (vprime - v1)/a1;
		double decelAvrgSpeed = (v1+vprime)/2;
		double decelDist = decelAvrgSpeed * decelTime;
		
		double distAfterDecel = s1-decelDist;
		double c0TravelTime	= s0/v0;
		double timeAfterDecel = c0TravelTime - decelTime;
		double distNewSpeed	= timeAfterDecel * vprime;
		
		double diffDist = distAfterDecel - distNewSpeed;
		double diffVel	= vprime - v0;
		
		double lambda = diffDist/diffVel;
		
		System.out.println("\t deceltime:"+decelTime);
		System.out.println("\t decelAvrgSpeed:"+ decelAvrgSpeed);
		System.out.println("\t decelDist:"+ decelDist);
		System.out.println("\t distAfterDecel:"+ distAfterDecel);
		System.out.println("\t c0TravelTime:"+ c0TravelTime);
		System.out.println("\t timeAfterDecel:"+ timeAfterDecel);
		System.out.println("\t distNewSpeed:"+ distNewSpeed);
		System.out.println("\t diffDist:"+ diffDist);
		System.out.println("\t diffVel:"+ diffVel);
		System.out.println("\t lambda:"+lambda);
		System.out.println();
		return lambda;
	}
	
	//@Test
	public void test() {
		
		
		RoadNetwork rn = new RoadNetwork();
		Node n1 		= new Node("n1",0,0);
		Node crossing 	= new Node("crossing",100,0);
		Node n2 		= new Node("n2",0,-50);
		Node exit 		= new Node("exit",200,0);
		Road r1 = new Road("n1_crossing",100,null,1);
		Road r2 = new Road("n2_crossing",100,null,1);
		Road r3 = new Road("crossing_exit",50,null,1);

		rn.addEdge(n1, crossing, r1);
		rn.addEdge(n2, crossing, r2);
		rn.addEdge(crossing, exit, r3);

		List<AgentData>mainList = new ArrayList<AgentData>();
		
		int positionMainCar 	= 25;
		int speedMainCar 		= 22;
		int laneIndexMainCar 	= 0;
		
		mainList.add(new AgentData("main_1", null, positionMainCar, speedMainCar, r1.id, laneIndexMainCar));
		
		positionMainCar 	= 1;
		speedMainCar 		= 50;
		laneIndexMainCar 	= 0;
		
		mainList.add(new AgentData("main_2", null, positionMainCar, speedMainCar, r1.id, laneIndexMainCar));

		
		List<AgentData>rampList = new ArrayList<AgentData>();
		int positionRampCar 	= 50;
		int speedRampCar 		= 30;
		int laneIndexRampCar 	= 0;

		rampList.add(new AgentData("merge_1", null, positionRampCar, speedRampCar, r2.id, laneIndexRampCar));

		positionRampCar 	= 3;
		speedRampCar 		= 55;
		laneIndexRampCar 	= 0;

		rampList.add(new AgentData("merge_2", null, positionRampCar, speedRampCar, r2.id, laneIndexRampCar));
		
		
		
		double mainRoadLength 	= r1.length; 
		double rampLength 		= r2.length;
		
		List<AgentData> outputList = MergeNormScheme.mergeTrafficStreams(mainList, rampList, mainRoadLength, rampLength);
		for(AgentData agentdata : outputList) {
			System.out.println(agentdata.id);
		}
		
		List<Double> speeds=MergeNormScheme.calculateNewSpeeds(outputList, rn);
		for(int i = 0; i< speeds.size();i++) {
			AgentData agentData = outputList.get(i);
			System.out.println("Car: "+agentData.id+",curSpeed:"+agentData.velocity +", newVel:"+speeds.get(i));
		}
		
		fail("Not yet implemented");
	}
	
	@Test
	public void mergeTest() throws SAXException, IOException, ParserConfigurationException{
		Random random = new Random(1337);

		String dir 			= System.getProperty("user.dir")+"/tests/Organisations/Norms/";
		DataModel dataModel = new DataModelXML(dir,"orgnormtest.mas.xml");
		MASData masData 	= dataModel.getMASData(); 
		
		HashMap<String, String> options = new HashMap<String, String>();
		options.put("e", Integer.toString(masData.simulationLength));
		options.put("start", "1");
		options.put("quit-on-end", "1");
		
		SumoTraciConnection conn 	= SimulationModelTraaS.initializeWithOptions(options,"sumo-gui", System.getProperty("user.dir")+"/tests/Organisations/Norms/orgnormtest.cfg.xml");
		RoadNetwork rn 				= dataModel.instantiateRoadNetwork();
		ArrayList<Route> routes 	= dataModel.getRoutes(rn);
		System.out.println("hallo");
		HashMap<Agent,Integer> agentPairList 	= TrafficMASController.instantiateAgents(masData, random, routes, rn);
		HashMap<String, Agent> completeAgentMap = SimulationModelTraaS.addAgents(agentPairList, conn);	
		System.out.println(completeAgentMap.size());
		HashMap<String, Agent> currentAgentMap 	= SimulationModelTraaS.updateCurrentAgentMap(completeAgentMap, new HashMap<String, Agent>(), conn);
		
		boolean timeStep = false;
		StateData stateData = SimulationModelTraaS.getStateData(conn, timeStep);
		
		int i = 0;
		while(i++ < masData.simulationLength){
			System.out.println(completeAgentMap.size());

			currentAgentMap = SimulationModelTraaS.updateCurrentAgentMap(completeAgentMap, currentAgentMap, conn);
			stateData 		= SimulationModelTraaS.getStateData(conn, true);
			System.out.println("Whats the time?: "+stateData.currentTimeStep);
			currentAgentMap = TrafficMASController.updateAgents(completeAgentMap, rn, stateData);
			rn = TrafficMASController.updateRoadNetwork(rn, stateData);
			try {
			
			} catch (Exception e) {
				e.printStackTrace();
			}	
		}
	}
}





