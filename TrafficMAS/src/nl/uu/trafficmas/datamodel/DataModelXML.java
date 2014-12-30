package nl.uu.trafficmas.datamodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

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
	//TODO: replace ArrayList<Pair> By HashMa[
	//TODO: split this class in several smaller classes
	
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
	public RoadNetwork getRoadNetwork() {
		return instantiateRoadNetwork(this.dir,this.nodesXML,this.edgesXML);
	}
	
	public static RoadNetwork instantiateRoadNetwork(String dir, String nodesXML, String edgesXML) {
		HashMap<String,Node> nodes = extractNodes(dir,nodesXML);
		ArrayList<Node> nodeList = new ArrayList<Node>(nodes.values());
		ArrayList<Edge> edges = extractEdges(dir, edgesXML,nodes);
		
		RoadNetwork rn = new RoadNetwork(nodeList, edges);
		rn.validateRoadNetwork();
		
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
	public LinkedHashMap<AgentProfileType, Double> getAgentProfileTypeDistribution() {
		return getAgentProfileTypeDistribution(dir,agentProfilesXML);
	}
	
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
	
	public static HashMap<Agent, Integer> instantiateAgents(Random rng, ArrayList<Route> routes, int simulationLength, double agentSpawnProbability, HashMap<AgentProfileType, Double> dist) {
		HashMap<Agent, Integer> agentsAndTimes = new HashMap<Agent, Integer>(); 
		for (int i = 1; i <= simulationLength; i++) {
			double coinFlip = rng.nextDouble();
			if(coinFlip < agentSpawnProbability) {
				coinFlip = rng.nextDouble();
				AgentProfileType agentProfileType = selectAgentProfileType(coinFlip, dist);
				
				int currentTime = i;
				int minimalTravelTime = 0;
				Edge[] routeEdges = routes.get(0).getRoute();
				double maxComfySpeed = agentProfileType.getMaxComfortableDrivingSpeed(Agent.DEFAULT_MAX_SPEED);
				for(Edge routeEdge : routeEdges) {
					minimalTravelTime += Math.round(routeEdge.getRoad().length/maxComfySpeed);
				}
				int goalArrivalTime = agentProfileType.goalArrivalTime(currentTime, minimalTravelTime);
				
				Node goalNode = routeEdges[routeEdges.length-1].getToNode();
				Agent agent = agentProfileType.toAgent(Agent.getNextAgentID(), goalNode, routeEdges,  goalArrivalTime, Agent.DEFAULT_MAX_SPEED); //TODO: change this default max speed
				agentsAndTimes.put(agent,i*1000); //*1000 because sumo counts in ms, not s.
			}
		}
		
		return agentsAndTimes;
	}
	
	public static AgentProfileType selectAgentProfileType(double coinFlip, HashMap<AgentProfileType, Double> dist) {
	
		for( Entry<AgentProfileType, Double> pair : dist.entrySet()) {
			if(coinFlip < pair.getValue()) {
				return pair.getKey();
			}
			coinFlip -= pair.getValue();
		}
		
		return null;
	}

	@Override
	public ArrayList<Route> getRoutes(RoadNetwork rn) {
		return getRoutes(rn,dir,routesXML);
	}
	
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
		int simulationLength 		= DataModelXML.simulationLength(dir, masXML);
		String sumoConfigPath 		= this.sumoConfigXML;
		double spawnProbability 	= DataModelXML.getAgentSpawnProbability(dir, masXML);
		HashMap<AgentProfileType,Double> agentProfileTypeDistribution = DataModelXML.getAgentProfileTypeDistribution(dir, masXML);
		
		return new MASData(simulationLength, sumoConfigPath, spawnProbability, agentProfileTypeDistribution);
	}
}
