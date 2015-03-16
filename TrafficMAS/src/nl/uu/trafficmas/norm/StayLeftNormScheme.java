package nl.uu.trafficmas.norm;

import java.util.List;
import java.util.Map;

import nl.uu.trafficmas.roadnetwork.RoadNetwork;
import nl.uu.trafficmas.roadnetwork.Sensor;
import nl.uu.trafficmas.simulationmodel.AgentData;

public class StayLeftNormScheme extends NormScheme {

	public StayLeftNormScheme(String id, SanctionType sanctionType,
			List<Sensor> sensorList) {
		super(id, sanctionType, sensorList);
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<NormInstantiation> instantiateNorms(RoadNetwork rn,
			int currentTime, Map<String, AgentData> currentOrgKnowledge) {
		// TODO Auto-generated method stub
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
	public AgentData goal() {
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
