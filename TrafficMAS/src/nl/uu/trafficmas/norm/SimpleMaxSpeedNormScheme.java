package nl.uu.trafficmas.norm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nl.uu.trafficmas.roadnetwork.RoadNetwork;
import nl.uu.trafficmas.roadnetwork.Sensor;
import nl.uu.trafficmas.simulationmodel.AgentData;

public class SimpleMaxSpeedNormScheme extends NormScheme {


	public SimpleMaxSpeedNormScheme(String id, SanctionType sanctionType,
			List<Sensor> sensorList) {
		super(id, sanctionType, sensorList);
		// TODO Auto-generated constructor stub
	}


	@Override
	public List<NormInstantiation> instantiateNorms(RoadNetwork rn,
			Map<String, AgentData> currentOrgKnowledge) {
		
		List<NormInstantiation> list = new ArrayList<NormInstantiation>();
		for(Sensor s : sensorList) {
			for(AgentData agentData : s.readSensor()) {
				list.add(new SimpleMaxSpeedNormInstantiation(this, agentData.id));
			}
		}
		
		return list;
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
		return ad.velocity > 2;
	}


	@Override
	public boolean deadline(Map<String, AgentData> currentOrgKnowledge,
			int currentTime) {
		// TODO Auto-generated method stub
		return false;
	}
}
