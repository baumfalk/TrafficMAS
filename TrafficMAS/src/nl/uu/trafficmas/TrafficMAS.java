package nl.uu.trafficmas;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Iterator;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public class TrafficMAS {
	public static void main(String[] args) {
		if(args.length < 2) { 
			System.out.println("At least two arguments needed");
			System.exit(1);
		}
		String configXML 	= args[0];
		String sumoLoc		= args[1];
		TrafficMAS trafficMas = new TrafficMAS(configXML,sumoLoc);
		trafficMas.run();		
	}

	private String nodesXML;
	private String edgesXML;
	private String sensorXML;
	private String agentsXML;
	
	private String sumoXML;
	
	public TrafficMAS(String configXML, String sumoLoc) {
		initializeRoadNetwork();
		initializeAgents();
		initializeOrganisations();
	}
	
	private void initializeRoadNetwork() {
		readXML(nodesXML);
		
	}

	private void initializeAgents() {
		
	}

	private void initializeOrganisations() {
		
	}

	@SuppressWarnings("unchecked")
	public void readXML(String xmlLocation) {
		
		try{
			// open the xml file
			XMLInputFactory inputFactory = XMLInputFactory.newInstance();
			InputStream in = new FileInputStream(xmlLocation);
			XMLEventReader eventReader = inputFactory.createXMLEventReader(in);

			readXMLLoop(eventReader);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	private void readXMLLoop(XMLEventReader eventReader)
			throws XMLStreamException {
		while (eventReader.hasNext()) {
			XMLEvent e = eventReader.nextEvent();
			readXMLElement(e);
		}
	}

	private void readXMLElement(XMLEvent e) {
		if(e.isStartElement()) {
			StartElement se = e.asStartElement();					
			System.out.println(se.getName());
			Iterator<Attribute> it = se.getAttributes();
			while(it.hasNext()) {
				Attribute a = it.next();
				   			
			}
		}
	}
	
	public void run() {
		// TODO Auto-generated method stub
		
	}

}
