package nl.uu.trafficmas.tests.SimulationModelTraaS;

import static org.junit.Assert.assertEquals;
import it.polito.appeal.traci.SumoTraciConnection;

import java.util.HashMap;

import nl.uu.trafficmas.SimulationModelTraaS;

import org.junit.Test;

import de.tudresden.sumo.cmd.Simulation;

public class InitializeWithOptionTest {

	@Test
	public void initializeWithOption() {
		
		HashMap <String,String> options = new HashMap<>();
		options.put("step-length", "0.1");
		SumoTraciConnection conn = SimulationModelTraaS.initializeWithOptions(options, "sumo", "./tests/ConfigTest.xml");
		try {
            int simtime = (int) conn.do_job_get(Simulation.getCurrentTime());
			conn.do_timestep();
			assertEquals(100, simtime);

			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
