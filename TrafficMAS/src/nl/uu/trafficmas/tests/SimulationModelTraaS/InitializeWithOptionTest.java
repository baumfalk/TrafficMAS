package nl.uu.trafficmas.tests.SimulationModelTraaS;

import static org.junit.Assert.*;
import it.polito.appeal.traci.SumoTraciConnection;
import nl.uu.trafficmas.SimulationModelTraaS;

import org.junit.Test;

import de.tudresden.sumo.cmd.Simulation;

public class InitializeWithOptionTest {

	@Test
	public void initializeWithOption() {
		SumoTraciConnection conn = SimulationModelTraaS.initializeWithOption("step-length", "0.1", "sumo", "./tests/ConfigTest.xml");
		try {
            int simtime = (int) conn.do_job_get(Simulation.getCurrentTime());
			conn.do_timestep();
			assertEquals(100, simtime);

			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
