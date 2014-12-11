package nl.uu.trafficmas;

import java.util.ArrayList;
import java.util.HashMap;

import nl.uu.trafficmas.agent.AgentProfileType;
import nl.uu.trafficmas.agent.AgentType;
import nl.uu.trafficmas.organisation.Organisation;
import nl.uu.trafficmas.roadnetwork.Edge;
import nl.uu.trafficmas.roadnetwork.Lane;
import nl.uu.trafficmas.roadnetwork.LaneType;
import nl.uu.trafficmas.roadnetwork.Node;
import nl.uu.trafficmas.roadnetwork.Road;
import nl.uu.trafficmas.roadnetwork.RoadNetwork;

public class DataModelXML implements DataModel {

	private String dir; 
	private String nodesXML;
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
		nodesXML = SimpleXMLReader.extractFromXML(dir,masXML,"nodes").get(0).get(0).second;
		edgesXML = SimpleXMLReader.extractFromXML(dir,masXML,"edges").get(0).get(0).second;
		agentProfilesXML = SimpleXMLReader.extractFromXML(dir,masXML,"agentprofiles").get(0).get(0).second;
		sumoConfigXML = dir+SimpleXMLReader.extractFromXML(dir,masXML, "sumoconfig").get(0).get(0).second;
	}

	@Override
	public RoadNetwork instantiateRoadNetwork() {
		return instantiateRoadNetwork(this.dir,this.nodesXML,this.edgesXML);
	}
	
	public static RoadNetwork instantiateRoadNetwork(String dir, String nodesXML, String edgesXML) {
		HashMap<String,Node> nodes = extractNodes(dir,nodesXML);
		ArrayList<Node> nodeList = new ArrayList<Node>(nodes.values());
		ArrayList<Edge> edges = extractEdges(dir, edgesXML,nodes);
		
		RoadNetwork rn = new RoadNetwork(nodeList, edges);
		
		return rn;
	}
		
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
	
	public static ArrayList<Edge> extractEdges(String dir, String edgesXML,HashMap<String,Node> nodes) {
		ArrayList<ArrayList<Pair<String,String>>> edgesAttributes = SimpleXMLReader.extractFromXML(dir, edgesXML,"edge");
		ArrayList<Edge> edges = new ArrayList<Edge>();
		for(ArrayList<Pair<String,String>> edgeAttributes : edgesAttributes) {
			extractEdge(edges, edgeAttributes,nodes);
		}
		
		return edges;
	}

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
			lanes.add(new Lane(LaneType.Normal)); //Todo: find some way to encode lanetype in xml
		}
		Node fromNode = nodes.get(from);
		Node toNode = nodes.get(to);
		
		double distance = Node.nodeDistance(fromNode, toNode);
		
		Road road = new Road(distance, lanes, Integer.parseInt(priority));
		
		Edge n = new Edge(fromNode, toNode, road);
		edges.add(n);
	}
	
	@Override
	public ArrayList<Organisation> instantiateOrganisations() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSumoConfigPath() {
		return sumoConfigXML;
	}

	@Override
	public double getAgentSpawnProbability() {
		return getAgentSpawnProbability(dir,agentProfilesXML);
	}
	
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
	public ArrayList<Pair<AgentProfileType, Double>> getAgentProfileTypeDistribution() {
		return getAgentProfileTypeDistribution(dir,agentProfilesXML);
	}
	
	public static ArrayList<Pair<AgentProfileType, Double>> getAgentProfileTypeDistribution(String dir, String agentProfilesXML) {
		ArrayList<ArrayList<Pair<String, String>>> agentAttributes = SimpleXMLReader.extractFromXML(dir, agentProfilesXML,"agent");
		ArrayList<Pair<AgentProfileType, Double>> agentTypeAndDist = new ArrayList<Pair<AgentProfileType,Double>>();
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
			agentTypeAndDist.add(new Pair<AgentProfileType, Double>(agentType, distVal));
		}
		
		return agentTypeAndDist;
	}
}
