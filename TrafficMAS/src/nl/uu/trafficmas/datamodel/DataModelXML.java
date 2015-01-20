package nl.uu.trafficmas.datamodel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import nl.uu.trafficmas.agent.AgentProfileType;
import nl.uu.trafficmas.roadnetwork.Edge;
import nl.uu.trafficmas.roadnetwork.Lane;
import nl.uu.trafficmas.roadnetwork.LaneType;
import nl.uu.trafficmas.roadnetwork.Node;
import nl.uu.trafficmas.roadnetwork.Road;
import nl.uu.trafficmas.roadnetwork.RoadNetwork;
import nl.uu.trafficmas.roadnetwork.Route;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class DataModelXML implements DataModel {

	private Document masDoc;
	private Document nodesDoc;
	private Document edgesDoc;
	private Document routesDoc;
	private Document agentProfilesDoc;

	public DataModelXML(String dir, String masXML) throws SAXException, IOException, ParserConfigurationException  {
		setup(dir,masXML);
	}
	
	private void setup(String dir, String masXML) throws SAXException, IOException, ParserConfigurationException {
		masDoc = DataModelXML.loadDocument(dir, masXML);
		
		String nodesXML 			= masDoc.getDocumentElement().getElementsByTagName("nodes").item(0).getAttributes().getNamedItem("value").getTextContent();
		String edgesXML 			= masDoc.getDocumentElement().getElementsByTagName("edges").item(0).getAttributes().getNamedItem("value").getTextContent();
		String routesXML 			= masDoc.getDocumentElement().getElementsByTagName("routes").item(0).getAttributes().getNamedItem("value").getTextContent();
		String agentProfilesXML 	= masDoc.getDocumentElement().getElementsByTagName("agentprofiles").item(0).getAttributes().getNamedItem("value").getTextContent();
		
		nodesDoc			= DataModelXML.loadDocument(dir, nodesXML);
		edgesDoc			= DataModelXML.loadDocument(dir, edgesXML);
		routesDoc			= DataModelXML.loadDocument(dir, routesXML);
		agentProfilesDoc	= DataModelXML.loadDocument(dir, agentProfilesXML);

	}

	public static Document loadDocument(String dir, String xml)
			throws ParserConfigurationException, SAXException, IOException {
		File fXmlFile 						= new File(dir+xml);
		DocumentBuilderFactory dbFactory 	= DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder 			= dbFactory.newDocumentBuilder();
		Document doc 						= dBuilder.parse(fXmlFile);
		doc.getDocumentElement().normalize();
		return doc;
	}

	@Override
	public RoadNetwork instantiateRoadNetwork() {
		return instantiateRoadNetwork(this.nodesDoc, this.edgesDoc);
	}
	
	/**
	 * Creates a RoadNetwork in the MAS according to 'nodesXML' and 'edgesXML'.
	 * The RoadNetwork object is also validated by validateRoadNetwork(). 
	 * These xml files must be present in the directory given by the variable 'dir'.
	 * @param dir
	 * @param nodesXML
	 * @param edgesXML
	 * @return a RoadNetwork, if the RoadNetwork is not correctly validated, this method will return null.
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 */
	public static RoadNetwork instantiateRoadNetwork(Document nodeDoc, Document edgeDoc) {
		HashMap<String,Node> nodes 	= DataModelXML.extractNodes(nodeDoc);
		ArrayList<Node> nodeList 	= new ArrayList<Node>(nodes.values());
		ArrayList<Edge> edges 		= extractEdges(edgeDoc,nodes);
		
		RoadNetwork rn = new RoadNetwork(nodeList, edges);
		if(rn.validateRoadNetwork()){
			return rn;
		} else{
			return null;
		}
	}
		
	/**
	 * Extracts all values needed from 'nodesXML' and returns a HashMap filled with Node objects.
	 * The xml file must be present in the directory given by the variable 'dir'.
	 * @param dir
	 * @param nodesXML
	 * @return a map with all Node objects.
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 */
	public static HashMap<String,Node> extractNodes(Document nodeDoc) {
		NodeList nodeList = nodeDoc.getDocumentElement().getElementsByTagName("node");
		
		HashMap<String,Node>nodes = new LinkedHashMap<String,Node>();
		for (int i = 0; i < nodeList.getLength(); i++) {
			org.w3c.dom.Node n 		= nodeList.item(i);
			NamedNodeMap attributes = n.getAttributes();
			String id 				= attributes.getNamedItem("id").getTextContent();
			String x 				= attributes.getNamedItem("x").getTextContent();
			String y 				= attributes.getNamedItem("y").getTextContent();
			
			Node node = new Node(id,Double.parseDouble(x),Double.parseDouble(y));
			nodes.put(id,node);
		}
		
		return nodes;
	}
	
	/**
	 * Extracts all values needed from 'edgesXML' and returns an ArrayList filled with Edge objects. 
	 * The 'nodes' is used, since Edge object need to be connected to Node objects.
	 * The XML file must be present in the directory given by the variable 'dir'.
	 * @param dir
	 * @param edgesXML
	 * @param nodes 
	 * @return a List containing all Edge objects.
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 */
	public static ArrayList<Edge> extractEdges(Document edgeDoc, HashMap<String,Node> nodes) {
		NodeList edgeList= edgeDoc.getElementsByTagName("edge");
		ArrayList<Edge> edges = new ArrayList<Edge>();

		for (int i = 0; i < edgeList.getLength(); i++) {
			org.w3c.dom.Node n 	= edgeList.item(i);
			NamedNodeMap attr 	= n.getAttributes();
			extractEdge(edges, attr,nodes);
		}
		
		return edges;
	}

	/**
	 * Extracts the values from 'edgeAttributes' 
	 * and uses the appropriate Node objects from 'nodes' to add an Edge object to the list 'edges'.
	 * @param edges
	 * @param attr
	 * @param nodes
	 */
	public static void extractEdge(ArrayList<Edge> edges,
			NamedNodeMap attr,
			HashMap<String,Node> nodes) {
		String from 		= attr.getNamedItem("from").getTextContent();
		String to 			= attr.getNamedItem("to").getTextContent();
		String id 			= attr.getNamedItem("id").getTextContent();
		String numberLanes 	= attr.getNamedItem("numLanes").getTextContent();
		String priority		= attr.getNamedItem("priority").getTextContent();		
		
		ArrayList<Lane> lanes = new ArrayList<Lane>();
		int numberLanesInt = Integer.parseInt(numberLanes);

		for (int i = 0; i < numberLanesInt; i++) {
			lanes.add(new Lane(LaneType.Normal,(byte) i)); //TODO: find some way to encode lanetype in xml
			lanes.get(i).setID(id+"_"+i); //TODO: write a test to check this 
		}
		
		for (int i = 0; i < numberLanesInt; i++) {
			if(i!= numberLanesInt-1) {
				lanes.get(i).setLeftLane(lanes.get(i+1));
			}
		}
		
		Node fromNode 	= nodes.get(from);
		Node toNode 	= nodes.get(to);
		
		double distance = Node.nodeDistance(fromNode, toNode);
		
		Road road = new Road(id, distance, lanes, Integer.parseInt(priority));
		
		Edge n = new Edge(fromNode, toNode, road);
		edges.add(n);
	}
	
	@Override
	public double getAgentSpawnProbability() {
		return getAgentSpawnProbability(this.agentProfilesDoc);
	}
	
	/**
	 * Returns the probability for an agent to spawn on each tick,
	 * this value is extracted from 'agentProfilesXML'.
	 * The xml file must be present in the directory given by the variable 'dir'.
	 * @param dir
	 * @param agentProfilesXML
	 * @return a value between 0 and 1, including 0 and 1.
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 */
	public static double getAgentSpawnProbability(Document agentProfileDoc) {
		NamedNodeMap attributes = agentProfileDoc.getElementsByTagName("agents").item(0).getAttributes();
		String spawnProbString = attributes.getNamedItem("spawn-probability").getTextContent();
		
		
		return Double.parseDouble(spawnProbString) ;
	}
	
	@Override
	public HashMap<String, Double> getRouteSpawnProbability() {
		return getRouteSpawnProbability(this.routesDoc);
	}
	
	public static HashMap<String, Double> getRouteSpawnProbability(Document routesDoc){
		NodeList routeList = routesDoc.getElementsByTagName("route");
		HashMap<String, Double> routeIdAndProbability = new LinkedHashMap<String, Double>();
		
		for (int i = 0; i < routeList.getLength(); i++) {
			org.w3c.dom.Node n = routeList.item(i);
			NamedNodeMap attr = n.getAttributes();
			String routeID = attr.getNamedItem("id").getTextContent();
			org.w3c.dom.Node spawnAttr = attr.getNamedItem("spawn-probability");
			String spawnProbabilityString = (spawnAttr != null) ? spawnAttr.getTextContent() : null;

			double spawnProbability = 0;

			if(spawnProbabilityString != null) {
				spawnProbability = Double.parseDouble(spawnProbabilityString);
			}
			routeIdAndProbability.put(routeID, spawnProbability);
		}
		
		return routeIdAndProbability;
	}
	
	@Override
	public LinkedHashMap<AgentProfileType, Double> getAgentProfileTypeDistribution() {
		return getAgentProfileTypeDistribution(this.agentProfilesDoc);
	}
	
	/**
 	 * Extracts the agentProfileTypeDistribution from 'agentProfilesXML' and returns it in a HashMap.
	 * The XML file must be present in the directory given by the variable 'dir'.
	 * @param dir
	 * @param agentProfilesXML
	 * @return a map containing the AgentProfile and the chance of it occurring in a value between 0 and 1.
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 */
	public static LinkedHashMap<AgentProfileType, Double> getAgentProfileTypeDistribution(Document agentProfileDoc) {
		NodeList agentList = agentProfileDoc.getElementsByTagName("agent");
		LinkedHashMap<AgentProfileType, Double> agentTypeAndDist = new LinkedHashMap<AgentProfileType, Double>();
		
		for (int i = 0; i < agentList.getLength(); i++) {
			org.w3c.dom.Node n = agentList.item(i);
			NamedNodeMap attr = n.getAttributes();
			String type = attr.getNamedItem("role").getTextContent();
			String dist = attr.getNamedItem("dist").getTextContent();

			AgentProfileType agentType = AgentProfileType.valueOf(type);
			double distVal = Double.parseDouble(dist);
			agentTypeAndDist.put(agentType, distVal);
		}
		return agentTypeAndDist;
	}

	@Override
	public int getSimulationLength() {
		return simulationLength(this.masDoc);
	}
	
	/**
	 * Extracts the simulation length from 'masXML' and converts it to an Integer value.
	 * The XML file must be present in the directory given by the variable 'dir'.
	 * @param dir
	 * @param masXML
	 * @return an Integer with the time in seconds concerning how long SUMO will run.
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 */
	public static int simulationLength(Document masDoc) {
		NamedNodeMap attributes = masDoc.getElementsByTagName("mas").item(0).getAttributes();
		String simulationLengthString = attributes.getNamedItem("simulationlength").getTextContent();

		return Integer.parseInt(simulationLengthString);
	}
	
	@Override
	public ArrayList<Route> getRoutes(RoadNetwork rn) {
		return getRoutes(rn,this.routesDoc);
	}
	
	/**
	 * Extracts a list of Route objects, by reading 'routesXML'.
	 * The xml file must be present in the directory given by the variable 'dir'.
	 * @param rn
	 * @param dir
	 * @param routesXML
	 * @return an ArrayList of Route objects
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 */
	public static ArrayList<Route> getRoutes(RoadNetwork rn, Document routesDoc) {
		NodeList routeList = routesDoc.getElementsByTagName("route");
		ArrayList<Route> routes = new ArrayList<Route>();

		for (int i = 0; i < routeList.getLength(); i++) {
			org.w3c.dom.Node n = routeList.item(i);
			NamedNodeMap attr = n.getAttributes();
			String id= attr.getNamedItem("id").getTextContent();
			String edgesString= attr.getNamedItem("edges").getTextContent();
			ArrayList<Edge> edges = new ArrayList<Edge>();

			String[] edgesSplitted = edgesString.split(" ");
			for(String roadID : edgesSplitted) {
				edges.add(rn.getEdge(roadID));
			}
			
			Route route = new Route(id,edges);
			routes.add(route);
		}
		
		return routes;
	}

	@Override
	public MASData getMASData() {
		return getMASData(this.masDoc, this.agentProfilesDoc, this.routesDoc);
	}
	
	/**
	 * Returns the MASData data structure which contains data read from several XML files.
	 * MASData contains simulationLength, sumoConfigPath, spawnProbability and agentProfileTypeDistribution.
	 * @param dir
	 * @param masXML
	 * @param sumoConfigXML
	 * @param agentProfilesXML
	 * @return the MASData data structure
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 */
	public static MASData getMASData(Document masDoc, Document agentProfilesDoc, Document routesDoc) {
		int simulationLength 		= DataModelXML.simulationLength(masDoc);
		double spawnProbability 	= DataModelXML.getAgentSpawnProbability(agentProfilesDoc);
		HashMap<AgentProfileType,Double> agentProfileTypeDistribution = DataModelXML.getAgentProfileTypeDistribution(agentProfilesDoc);
		HashMap<String, Double> routeIdAndProbability = DataModelXML.getRouteSpawnProbability(routesDoc);
		return new MASData(simulationLength, spawnProbability, agentProfileTypeDistribution, routeIdAndProbability);
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}
}
