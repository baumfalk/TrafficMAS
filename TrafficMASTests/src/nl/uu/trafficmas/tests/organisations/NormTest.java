package nl.uu.trafficmas.tests.organisations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import nl.uu.trafficmas.agent.Agent;
import nl.uu.trafficmas.agent.HotShotAgent;
import nl.uu.trafficmas.datamodel.DataModelXML;
import nl.uu.trafficmas.norm.NormInstantiation;
import nl.uu.trafficmas.norm.NormScheme;
import nl.uu.trafficmas.roadnetwork.Edge;
import nl.uu.trafficmas.roadnetwork.Node;
import nl.uu.trafficmas.roadnetwork.RoadNetwork;
import nl.uu.trafficmas.roadnetwork.Route;

import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class NormTest {

	@Test
	public void test() throws ParserConfigurationException, SAXException, IOException {
		fail("Not yet implemented");
		String dir 			= System.getProperty("user.dir")+"/tests/Organisations/Norms/";
		String normsXML		= "orgnormtest.norm.xml";
		Document normsDoc	= DataModelXML.loadDocument(dir, normsXML);
		
		Map<String,NormScheme> normList = DataModelXML.getNorms(normsDoc);
		
		
		assertNotNull(normList);
		assertEquals(false, normList.isEmpty());
		assertEquals(true,normList.containsKey("norm1"));
		String nodeXML 		= "orgnormtest.node.xml";
		String edgeXML 		= "orgnormtest.node.xml";
		
		Document nodeDoc 	= DataModelXML.loadDocument(dir, nodeXML);
		Document edgeDoc 	= DataModelXML.loadDocument(dir, edgeXML);
		RoadNetwork rn = DataModelXML.instantiateRoadNetwork(nodeDoc, edgeDoc);
		
		Node goalNode = rn.getNodes()[0];
		ArrayList<Edge> routeEdges = new ArrayList<Edge>();
		routeEdges.add(rn.getEdge("A28Tot350"));
		
		Route r = new Route("route1", routeEdges);
		Agent agent = new HotShotAgent("agent1", goalNode, r, rn, 100, 20.0, 20.0);
		agent.setDistance(5);
		agent.setVelocity(500);
		
		NormScheme normScheme 		= normList.get("norm1");
		double maxSpeed 			= 50;
		boolean changeLane 			= false;
		double sanction				= 100;
		NormInstantiation normInst 	= normScheme.instantiateNorm(maxSpeed,changeLane, sanction);
		normInst.checkViolation(agent.getVelocity(),agent.getRoad(),agent.getLane(),agent.getDistance());
	}
}
