import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Iterator;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public class LoadXML {

	public static void main(String[] args) {
		try{
		  // First, create a new XMLInputFactory
	      XMLInputFactory inputFactory = XMLInputFactory.newInstance();
	      // Setup a new eventReader
	      InputStream in = new FileInputStream("sim/hello.mas.xml");
	      XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
	      while (eventReader.hasNext()) {
	    	XMLEvent e = eventReader.nextEvent();
	    	if(e.isStartElement()) {
	    		StartElement se = e.asStartElement();
	    		System.out.println(se.getName());
	    		Iterator<Attribute> it = se.getAttributes();
	    		while(it.hasNext()) {
	    			Attribute a = it.next();
	    			System.out.println("\t"+a.getValue());	    			
	    		}
	    	}
	      
	      }
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

}
