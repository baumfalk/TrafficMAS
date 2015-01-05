package nl.uu.trafficmas.tests.simulationModelTraaS;

import static org.junit.Assert.assertEquals;
import it.polito.appeal.traci.SumoTraciConnection;
import nl.uu.trafficmas.simulationmodel.SimulationModelTraaS;

import org.junit.Test;

import de.tudresden.sumo.cmd.Simulation;

public class InitializeTest {

	@Test
	public void initialize() {

		
		SumoTraciConnection conn = SimulationModelTraaS.initialize("sumo", "./tests/ConfigTest.xml");
		try {
            int simtime = (int) conn.do_job_get(Simulation.getCurrentTime());
			conn.do_timestep();
			assertEquals(1000, simtime);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
