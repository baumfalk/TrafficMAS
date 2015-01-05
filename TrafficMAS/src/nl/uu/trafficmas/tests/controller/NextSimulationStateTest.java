package nl.uu.trafficmas.tests.controller;

import static org.junit.Assert.fail;
import nl.uu.trafficmas.controller.TrafficMASController;
import nl.uu.trafficmas.simulationmodel.SimulationModel;
import nl.uu.trafficmas.simulationmodel.SimulationModelTraaS;
import nl.uu.trafficmas.simulationmodel.StateData;

import org.junit.Test;

public class NextSimulationStateTest {

	@Test
	public void test() {
		fail("Not yet implemented");
		SimulationModel simulationModel = new SimulationModelTraaS(null, null); 
		StateData simulationStateData =  TrafficMASController.nextSimulationState(simulationModel);
	}

}
