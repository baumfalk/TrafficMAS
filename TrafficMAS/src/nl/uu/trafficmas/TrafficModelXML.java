package nl.uu.trafficmas;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import nl.uu.trafficmas.agent.Agent;
import nl.uu.trafficmas.agent.AgentAction;
import nl.uu.trafficmas.agent.AgentPhysical;
import nl.uu.trafficmas.organisation.Organisation;
import nl.uu.trafficmas.roadnetwork.Edge;
import nl.uu.trafficmas.roadnetwork.Lane;
import nl.uu.trafficmas.roadnetwork.LaneType;
import nl.uu.trafficmas.roadnetwork.Node;
import nl.uu.trafficmas.roadnetwork.Road;
import nl.uu.trafficmas.roadnetwork.RoadNetwork;

public class TrafficModelXML implements DataModel {

	private String dir; 
	private String nodesXML;
	private String edgesXML;
	private String agentProfilesXML;
	private String sumoConfigXML;
	
	public TrafficModelXML() {
		
	}
	
	public TrafficModelXML(String dir, String masXML, String sumoDir)  {
		setup(dir,masXML,sumoDir);
	}
	
	public void setup(String dir, String masXML, String sumoDir) {
		this.dir = dir;
		nodesXML = SimpleXMLReader.extractFromXML(dir,masXML,"nodes").get(0).get(0).value;
		edgesXML = SimpleXMLReader.extractFromXML(dir,masXML,"edges").get(0).get(0).value;
		agentProfilesXML = SimpleXMLReader.extractFromXML(dir,masXML,"agentprofiles").get(0).get(0).value;
		sumoConfigXML = SimpleXMLReader.extractFromXML(dir,masXML, "sumoconfig").get(0).get(0).value;
	}

	@Override
	public RoadNetwork instantiateRoadNetwork() {
		HashMap<String,Node> nodes = extractNodes(dir,this.nodesXML);
		ArrayList<Edge> edges = extractEdges(dir, this.edgesXML,nodes);
		
		return null;
	}
		
	public HashMap<String,Node> extractNodes(String dir,String nodesXML) {
		ArrayList<ArrayList<KeyValue<String,String>>> nodesAttributes = SimpleXMLReader.extractFromXML(dir,nodesXML,"node");
		HashMap<String,Node>nodes = new HashMap<String,Node>();
		for(ArrayList<KeyValue<String,String>> nodeAttributes : nodesAttributes) {
			String id = null;
			String x = null;
			String y = null;
			for(KeyValue<String,String> attr : nodeAttributes) {
				switch(attr.key) {
				case "id":
					id = attr.value;
					break;
				case "x":
					x = attr.value;
					break;
				case "y":
					y = attr.value;
					break;
				}
			}
			
			Node node = new Node(id,Double.parseDouble(x),Double.parseDouble(y));
			nodes.put(id,node);
		}
		
		return nodes;
	}
	
	public ArrayList<Edge> extractEdges(String dir, String edgesXML,HashMap<String,Node> nodes) {
		ArrayList<ArrayList<KeyValue<String,String>>> edgesAttributes = SimpleXMLReader.extractFromXML(dir, edgesXML,"edge");
		ArrayList<Edge> edges = new ArrayList<Edge>();
		for(ArrayList<KeyValue<String,String>> edgeAttributes : edgesAttributes) {
			extractEdge(edges, edgeAttributes,nodes);
		}
		
		return edges;
	}

	public void extractEdge(ArrayList<Edge> edges,
			ArrayList<KeyValue<String, String>> edgeAttributes,
			HashMap<String,Node> nodes) {
		String from 		= null;
		String to 			= null;
		String id 			= null;
		String numberLanes 	= null;
		String priority		= null;		
		for(KeyValue<String,String> attr : edgeAttributes) {
			switch(attr.key){
			case "from":
				from = attr.value;
				break;
			case "to":
				to = attr.value;
				break;
			case "id":
				id = attr.value;
				break;
			case "numLanes":
				numberLanes = attr.value;
				break;
			case "priority":
				priority = attr.value;
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
	public ArrayList<Agent> instantiateAgents() {

		return null;
	}

	@Override
	public ArrayList<Organisation> instantiateOrganisations() {
		// TODO Auto-generated method stub
		return null;
	}
}
