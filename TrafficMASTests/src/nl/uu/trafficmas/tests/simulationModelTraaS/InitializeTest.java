package nl.uu.trafficmas.tests.simulationModelTraaS;

import static org.junit.Assert.assertEquals;
import it.polito.appeal.traci.SumoTraciConnection;
import nl.uu.trafficmas.simulationmodel.SimulationModelTraaS;
import nl.uu.trafficmas.simulationmodel.StateData;

import org.junit.Test;

import de.tudresden.sumo.cmd.Simulation;

public class InitializeTest {

	@Test
	public void initialize() {

		
		try {
			SumoTraciConnection conn = SimulationModelTraaS.initialize("sumo", System.getProperty("user.dir")+"/tests/SimulationModelTraaS/Initialize/ConfigTest.xml");
			StateData stateData = SimulationModelTraaS.getStateData(conn, false);
			assertEquals(1000, stateData.currentTimeStep);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
