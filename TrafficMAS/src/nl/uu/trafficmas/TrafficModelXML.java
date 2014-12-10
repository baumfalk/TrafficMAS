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
import nl.uu.trafficmas.roadnetwork.Node;
import nl.uu.trafficmas.roadnetwork.RoadNetwork;

public class TrafficModelXML implements TrafficModel {

	private String dir; 
	private String nodesXML;
	private String edgesXML;
	private String agentProfilesXML;
	private String sumoConfigXML;
	
	public TrafficModelXML(String dir, String masXML, String sumoDir) {
		this.dir = dir;
		nodesXML = this.extractFromXML(masXML,"nodes").get(0).get(0).value;
		edgesXML = this.extractFromXML(masXML,"edges").get(0).get(0).value;
		agentProfilesXML = this.extractFromXML(masXML,"agentprofiles").get(0).get(0).value;
		sumoConfigXML = this.extractFromXML(masXML, "sumoconfig").get(0).get(0).value;
	}
	
	private ArrayList<ArrayList<KeyValue<String,String>>> extractFromXML(String xmlLocation, String target) {
		try{
			// open the xml file
			XMLInputFactory inputFactory = XMLInputFactory.newInstance();
			InputStream in = new FileInputStream(dir+"/"+xmlLocation);
			XMLEventReader eventReader = inputFactory.createXMLEventReader(in);

			return extractXMLLoop(eventReader,target);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return null;
	}

	private ArrayList<ArrayList<KeyValue<String,String>>> extractXMLLoop(XMLEventReader eventReader, String target) throws XMLStreamException {
		ArrayList<ArrayList<KeyValue<String,String>>> foundTargetList = new ArrayList<ArrayList<KeyValue<String,String>>>();
		while (eventReader.hasNext()) {
			XMLEvent e = eventReader.nextEvent();
			if(e.isStartElement() && e.asStartElement().getName().getLocalPart().equals(target)) {
				foundTargetList.add(extractXMLAttributes(e));
			}
		}
		
		return foundTargetList;
	}

	private ArrayList<KeyValue<String,String>> extractXMLAttributes(XMLEvent e) {
		StartElement se = e.asStartElement();					
		Iterator<Attribute> it = se.getAttributes();
		ArrayList<KeyValue<String,String>> attributes = new ArrayList<KeyValue<String,String>>();
		while(it.hasNext()) {
			Attribute a = it.next();
			attributes.add(new KeyValue<String,String>(a.getName().getLocalPart(),a.getValue()));
		}
		return attributes;
	}

	@Override
	public RoadNetwork instantiateRoadNetwork() {
		ArrayList<Node> nodes = extractNodes(this.nodesXML);
		
		return null;
	}
		
	private ArrayList<Node> extractNodes(String nodesXML) {
		ArrayList<ArrayList<KeyValue<String,String>>> nodesAttributes = extractFromXML(nodesXML,"node");
		ArrayList<Node> nodes = new ArrayList<Node>();
		for(ArrayList<KeyValue<String,String>> nodeAttributes : nodesAttributes) {
			for(KeyValue<String,String> attr : nodeAttributes) {
				if(attr.key.equals("id")) {
					Node n = new Node(attr.value);
					nodes.add(n);
				}
			}
		}
		
		return nodes;
	}
	
	private ArrayList<Edge> extractEdges(String edgesXML) {
		ArrayList<ArrayList<KeyValue<String,String>>> edgesAttributes = extractFromXML(nodesXML,"node");
		ArrayList<Edge> edges = new ArrayList<Edge>();
		for(ArrayList<KeyValue<String,String>> edgeAttributes : edgesAttributes) {
			extractEdge(edges, edgeAttributes);
		}
		
		return edges;
	}

	private void extractEdge(ArrayList<Edge> edges,
			ArrayList<KeyValue<String, String>> edgeAttributes) {
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
		Edge n = new Edge(null, null, null);
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
	
	@Override
	public ArrayList<AgentPhysical> getAgentPhysical() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HashMap<AgentPhysical, AgentPhysical> getLeadingVehicles() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void executeAgentActions(ArrayList<AgentAction> actions) {
		// TODO Auto-generated method stub
		
	}

}
