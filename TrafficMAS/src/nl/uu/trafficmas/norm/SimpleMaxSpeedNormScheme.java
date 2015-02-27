package nl.uu.trafficmas.norm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nl.uu.trafficmas.roadnetwork.RoadNetwork;
import nl.uu.trafficmas.roadnetwork.Sensor;
import nl.uu.trafficmas.simulationmodel.AgentData;

public class SimpleMaxSpeedNormScheme extends NormScheme {

	public SimpleMaxSpeedNormScheme(String id, List<Sensor> sensorList) {
		super(id, sensorList);
	}

	@Override
	public List<NormInstantiation> instantiateNorms(RoadNetwork rn,
			Map<String, AgentData> currentOrgKnowledge) {
		
		List<NormInstantiation> list = new ArrayList<NormInstantiation>();
		for(Sensor s : sensorList) {
			for(AgentData agentData : s.readSensor()) {
				
			}
		}
		
		
		return null;
	}

	
	@Override
	public boolean checkCondition(Map<String, AgentData> currentOrgKnowledge) {
		return true;
	}

	@Override
	protected void runAlgorithm(RoadNetwork rn) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean violated(AgentData ad) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Sanction getSanction() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean deadline(Map<String, AgentData> currentOrgKnowledge,
			int currentTime) {
		// TODO Auto-generated method stub
		return false;
	}
}