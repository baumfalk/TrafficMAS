package nl.uu.trafficmas.datamodel;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.junit.Test;

import nl.uu.trafficmas.agent.Agent;
import nl.uu.trafficmas.agent.AgentProfileType;
import nl.uu.trafficmas.organisation.Organisation;
import nl.uu.trafficmas.roadnetwork.Edge;
import nl.uu.trafficmas.roadnetwork.Lane;
import nl.uu.trafficmas.roadnetwork.LaneType;
import nl.uu.trafficmas.roadnetwork.Node;
import nl.uu.trafficmas.roadnetwork.Road;
import nl.uu.trafficmas.roadnetwork.RoadNetwork;
import nl.uu.trafficmas.roadnetwork.Route;

public class DataModelXML implements DataModel {

	private String dir; 
	private String masXML;
	private String nodesXML;
	private String routesXML;
	private String edgesXML;
	private String agentProfilesXML;
	private String sumoConfigXML;
	
	public DataModelXML() {
		
	}
	
	public DataModelXML(String dir, String masXML)  {
		setup(dir,masXML);
	}
	
	private void setup(String dir, String masXML) {
		this.dir = dir;
		this.masXML = masXML;
		nodesXML = SimpleXMLReader.extractFromXML(dir,masXML,"nodes").get(0).get(0).second;
		edgesXML = SimpleXMLReader.extractFromXML(dir,masXML,"edges").get(0).get(0).second;
		routesXML = SimpleXMLReader.extractFromXML(dir,masXML,"routes").get(0).get(0).second; 
		agentProfilesXML = SimpleXMLReader.extractFromXML(dir,masXML,"agentprofiles").get(0).get(0).second;
		sumoConfigXML = dir+SimpleXMLReader.extractFromXML(dir,masXML, "sumoconfig").get(0).get(0).second;
	}

	@Override
	public RoadNetwork instantiateRoadNetwork() {
		return instantiateRoadNetwork(this.dir,this.nodesXML,this.edgesXML);
	}
	
	/**
	 * Creates a RoadNetwork in the MAS according to 'nodesXML' and 'edgesXML'.
	 * The RoadNetwork object is also validated by validateRoadNetwork(). 
	 * These xml files must be present in the directory given by the variable 'dir'.
	 * @param dir
	 * @param nodesXML
	 * @param edgesXML
	 * @return a RoadNetwork, if the RoadNetwork is not correctly validated, this method will return null.
	 */
	public static RoadNetwork instantiateRoadNetwork(String dir, String nodesXML, String edgesXML) {
		HashMap<String,Node> nodes = extractNodes(dir,nodesXML);
		ArrayList<Node> nodeList = new ArrayList<Node>(nodes.values());
		ArrayList<Edge> edges = extractEdges(dir, edgesXML,nodes);
		
		RoadNetwork rn = new RoadNetwork(nodeList, edges);
		if(rn.validateRoadNetwork()){
			return rn;
		} else{
			System.out.println("RoadNetwork is invalid!");
			return null;
		}
	}
		
	/**
	 * Extracts all values needed from 'nodesXML' and returns a HashMap filled with Node objects.
	 * The xml file must be present in the directory given by the variable 'dir'.
	 * @param dir
	 * @param nodesXML
	 * @return a map with all Node objects.
	 */
	public static HashMap<String,Node> extractNodes(String dir,String nodesXML) {
		ArrayList<ArrayList<Pair<String,String>>> nodesAttributes = SimpleXMLReader.extractFromXML(dir,nodesXML,"node");
		HashMap<String,Node>nodes = new HashMap<String,Node>();
		for(ArrayList<Pair<String,String>> nodeAttributes : nodesAttributes) {
			String id = null;
			String x = null;
			String y = null;
			for(Pair<String,String> attr : nodeAttributes) {
				switch(attr.first) {
				case "id":
					id = attr.second;
					break;
				case "x":
					x = attr.second;
					break;
				case "y":
					y = attr.second;
					break;
				}
			}
			
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
	 */
	public static ArrayList<Edge> extractEdges(String dir, String edgesXML,HashMap<String,Node> nodes) {
		ArrayList<ArrayList<Pair<String,String>>> edgesAttributes = SimpleXMLReader.extractFromXML(dir, edgesXML,"edge");
		ArrayList<Edge> edges = new ArrayList<Edge>();
		for(ArrayList<Pair<String,String>> edgeAttributes : edgesAttributes) {
			extractEdge(edges, edgeAttributes,nodes);
		}
		
		return edges;
	}

	/**
	 * Extracts the values from 'edgeAttributes' 
	 * and uses the appropriate Node objects from 'nodes' to add an Edge object to the list 'edges'.
	 * @param edges
	 * @param edgeAttributes
	 * @param nodes
	 */
	public static void extractEdge(ArrayList<Edge> edges,
			ArrayList<Pair<String, String>> edgeAttributes,
			HashMap<String,Node> nodes) {
		String from 		= null;
		String to 			= null;
		String id 			= null;
		String numberLanes 	= null;
		String priority		= null;		
		for(Pair<String,String> attr : edgeAttributes) {
			switch(attr.first){
			case "from":
				from = attr.second;
				break;
			case "to":
				to = attr.second;
				break;
			case "id":
				id = attr.second;
				break;
			case "numLanes":
				numberLanes = attr.second;
				break;
			case "priority":
				priority = attr.second;
				break;
			}
		}
		
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
		
		Node fromNode = nodes.get(from);
		Node toNode = nodes.get(to);
		
		double distance = Node.nodeDistance(fromNode, toNode);
		
		Road road = new Road(id, distance, lanes, Integer.parseInt(priority));
		
		Edge n = new Edge(fromNode, toNode, road);
		edges.add(n);
	}
	
	@Override
	public String getSumoConfigPath() {
		return sumoConfigXML;
	}

	@Override
	public double getAgentSpawnProbability() {
		return getAgentSpawnProbability(dir,agentProfilesXML);
	}
	
	/**
	 * Returns the probability for an agent to spawn on each tick,
	 * this value is extracted from 'agentProfilesXML'.
	 * The xml file must be present in the directory given by the variable 'dir'.
	 * @param dir
	 * @param agentProfilesXML
	 * @return a value between 0 and 1, including 0 and 1.
	 */
	public static double getAgentSpawnProbability(String dir, String agentProfilesXML) {
		ArrayList<ArrayList<Pair<String, String>>> agentsAttributes = SimpleXMLReader.extractFromXML(dir, agentProfilesXML,"agents");
		
		for (Pair<String,String> attribute : agentsAttributes.get(0)) {
			switch(attribute.first) {
			case "spawn-probability":
				return Double.parseDouble(agentsAttributes.get(0).get(0).second);
			}
		}
		
		return 0;
	}
	

	@Override
	public LinkedHashMap<AgentProfileType, Double> getAgentProfileTypeDistribution() {
		return getAgentProfileTypeDistribution(dir,agentProfilesXML);
	}
	
	/**
 	 * Extracts the agentProfileTypeDistribution from 'agentProfilesXML' and returns it in a HashMap.
	 * The XML file must be present in the directory given by the variable 'dir'.
	 * @param dir
	 * @param agentProfilesXML
	 * @return a map containing the AgentProfile and the chance of it occurring in a value between 0 and 1.
	 */
	public static LinkedHashMap<AgentProfileType, Double> getAgentProfileTypeDistribution(String dir, String agentProfilesXML) {
		ArrayList<ArrayList<Pair<String, String>>> agentAttributes = SimpleXMLReader.extractFromXML(dir, agentProfilesXML,"agent");
		LinkedHashMap<AgentProfileType, Double> agentTypeAndDist = new LinkedHashMap<AgentProfileType, Double>();
		for(ArrayList<Pair<String, String>> attributes : agentAttributes) {
			String type = null;
			String dist = null;
			for(Pair<String, String> attribute : attributes) {
				switch(attribute.first) {
				case "role":
					type = attribute.second;
				case "dist":
					dist = attribute.second;
				}
			}
			
			AgentProfileType agentType = AgentProfileType.valueOf(type);
			double distVal = Double.parseDouble(dist);
			agentTypeAndDist.put(agentType, distVal);
		}
		
		return agentTypeAndDist;
	}

	@Override
	public int getSimulationLength() {
		return simulationLength(dir, masXML);
	}
	
	/**
	 * Extracts the simulation length from 'masXML' and converts it to an Integer value.
	 * The XML file must be present in the directory given by the variable 'dir'.
	 * @param dir
	 * @param masXML
	 * @return an Integer with the time in seconds concerning how long SUMO will run.
	 */
	public static int simulationLength(String dir, String masXML) {
		ArrayList<ArrayList<Pair<String, String>>> masAttributes = SimpleXMLReader.extractFromXML(dir,masXML,"mas");
		int simulationLength = 0;
		for(ArrayList<Pair<String, String>> attributes : masAttributes ) {
			for(Pair<String, String> attribute: attributes) {
				switch(attribute.first) {
				case "simulationlength":
					simulationLength =  Integer.parseInt(attribute.second);
					break;
				}
			}
		}
		return simulationLength;
	}
	
	@Override
	public ArrayList<Route> getRoutes(RoadNetwork rn) {
		return getRoutes(rn,dir,routesXML);
	}
	
	/**
	 * Extracts a list of Route objects, by reading 'routesXML'.
	 * The xml file must be present in the directory given by the variable 'dir'.
	 * @param rn
	 * @param dir
	 * @param routesXML
	 * @return an ArrayList of Route objects
	 */
	public static ArrayList<Route> getRoutes(RoadNetwork rn, String dir, String routesXML) {
		ArrayList<ArrayList<Pair<String, String>>> routesAttributes = SimpleXMLReader.extractFromXML(dir,routesXML,"route");
		ArrayList<Route> routes = new ArrayList<Route>();
		for(ArrayList<Pair<String, String>> routeAttributes : routesAttributes) {
			String id= null;
			ArrayList<Edge> edges = new ArrayList<Edge>();
			String edgesString = null;
			for(Pair<String, String> attribute: routeAttributes) {
				switch(attribute.first) {
				case "id":
					id = attribute.second;
					break;
				case "edges":
					edgesString = attribute.second;
					String[] edgesSplitted = edgesString.split(" ");
					for(String roadID : edgesSplitted) {
						edges.add(rn.getEdge(roadID));
					}
					break;
				}
			}
			Route route = new Route(id,edges);
			routes.add(route);
		}
		
		return routes;
	}

	@Override
	public MASData getMASData() {
		return getMASData(dir, masXML, sumoConfigXML, agentProfilesXML);
	}
	
	/**
	 * Returns the MASData data structure which contains data read from several XML files.
	 * MASData contains simulationLength, sumoConfigPath, spawnProbability and agentProfileTypeDistribution.
	 * @param dir
	 * @param masXML
	 * @param sumoConfigXML
	 * @param agentProfilesXML
	 * @return the MASData data structure
	 */
	public static MASData getMASData(String dir, String masXML, String sumoConfigXML, String agentProfilesXML){
		int simulationLength 		= DataModelXML.simulationLength(dir, masXML);
		String sumoConfigPath 		= sumoConfigXML;
		double spawnProbability 	= DataModelXML.getAgentSpawnProbability(dir, agentProfilesXML);
		HashMap<AgentProfileType,Double> agentProfileTypeDistribution = DataModelXML.getAgentProfileTypeDistribution(dir, agentProfilesXML);
		
		return new MASData(simulationLength, sumoConfigPath, spawnProbability, agentProfileTypeDistribution);
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}
	
}
