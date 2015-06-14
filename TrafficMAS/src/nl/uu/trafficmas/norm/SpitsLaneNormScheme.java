package nl.uu.trafficmas.norm;

import java.util.List;
import java.util.Map;

import nl.uu.trafficmas.roadnetwork.RoadNetwork;
import nl.uu.trafficmas.roadnetwork.Sensor;
import nl.uu.trafficmas.simulationmodel.AgentData;

public class SpitsLaneNormScheme extends NormScheme {

	public SpitsLaneNormScheme(String id, SanctionType sanctionType,
			List<Sensor> sensorList) {
		super(id, sanctionType, sensorList);
	}

	@Override
	public List<NormInstantiation> instantiateNorms(RoadNetwork rn,
			int currentTime, Map<String, AgentData> currentOrgKnowledge) {
		// TODO Auto-generated method stub
		
		
		// more than 
		return null;
	}

	@Override
	public boolean checkCondition(Map<String, AgentData> currentOrgKnowledge) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean violated(AgentData ad) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deadline(Map<String, AgentData> currentOrgKnowledge,
			int currentTime) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<AgentData> getGoals() {
		// TODO Auto-generated method stub
		return null;
	}

}
