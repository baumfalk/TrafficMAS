package nl.uu.trafficmas;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public class SimpleXMLReader {
	public static ArrayList<ArrayList<Pair<String,String>>> extractFromXML(String dir, String xmlLocation, String target) {
		try{
			XMLInputFactory inputFactory = XMLInputFactory.newInstance();
			InputStream in = new FileInputStream(dir+xmlLocation);
			XMLEventReader eventReader = inputFactory.createXMLEventReader(in);

			return extractXMLLoop(eventReader,target);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return null;
	}

	public static ArrayList<ArrayList<Pair<String,String>>> extractXMLLoop(XMLEventReader eventReader, String target) throws XMLStreamException {
		ArrayList<ArrayList<Pair<String,String>>> foundTargetList = new ArrayList<ArrayList<Pair<String,String>>>();
		while (eventReader.hasNext()) {
			XMLEvent e = eventReader.nextEvent();
			if(e.isStartElement() && e.asStartElement().getName().getLocalPart().equals(target)) {
				foundTargetList.add(extractXMLAttributes(e));
			}
		}
		
		return foundTargetList;
	}

	public static  ArrayList<Pair<String,String>> extractXMLAttributes(XMLEvent e) {
		StartElement se = e.asStartElement();					
		Iterator<Attribute> it = se.getAttributes();
		ArrayList<Pair<String,String>> attributes = new ArrayList<Pair<String,String>>();
		while(it.hasNext()) {
			Attribute a = it.next();
			attributes.add(new Pair<String,String>(a.getName().getLocalPart(),a.getValue()));
		}
		return attributes;
	}
}
