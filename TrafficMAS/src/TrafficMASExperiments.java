import java.util.Random;

import nl.uu.trafficmas.controller.TrafficMASController;
import nl.uu.trafficmas.datamodel.DataModel;
import nl.uu.trafficmas.datamodel.DataModelXML;
import nl.uu.trafficmas.simulationmodel.SimulationModel;
import nl.uu.trafficmas.simulationmodel.SimulationModelTraaS;
import nl.uu.trafficmas.view.DummyView;
import nl.uu.trafficmas.view.TrafficView;


public class TrafficMASExperiments {
	public static void main(String[] args) throws Exception {
		if(args.length < 4) { 
			System.out.println("At least four arguments needed");
			System.exit(1);
		}
		String dir		= args[0];
		String masXML 	= args[1];
		String sumoBin 	= args[2];
		String sumoXML	= args[3];
		int numberOfRuns= Integer.parseInt(args[4]);
		Random rand = new Random();
		long seed = rand.nextLong();

		for(int i = 0; i < numberOfRuns; i++) {
			long start_time = System.nanoTime();
			DataModel dataModel 			= new DataModelXML(dir,masXML);
			SimulationModel simModel 		= new SimulationModelTraaS(sumoBin,dir+sumoXML);
			TrafficView view 				= new DummyView();
			boolean noDelay = true;
			TrafficMASController trafficMas	= new TrafficMASController(dataModel, simModel, view,seed);
			long end_time = System.nanoTime();
			double difference = (end_time - start_time)/1e6;
			System.out.println("Run #"+(i+1)+" with seed: " + seed);
			System.out.println("init time:" + difference + "ms");
			
			start_time = System.nanoTime();
			try {
			trafficMas.run(dataModel, simModel, view);
			} catch(Exception exception) {
				exception.printStackTrace();
			}
			end_time = System.nanoTime();
			difference = (end_time - start_time)/1e6;
			System.out.println("run time:" + difference + "ms");
			
			seed = rand.nextLong();
		}
	}
}
