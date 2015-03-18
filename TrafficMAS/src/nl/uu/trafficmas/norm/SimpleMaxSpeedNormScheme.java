package nl.uu.trafficmas.norm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nl.uu.trafficmas.roadnetwork.RoadNetwork;
import nl.uu.trafficmas.roadnetwork.Sensor;
import nl.uu.trafficmas.simulationmodel.AgentData;

public class SimpleMaxSpeedNormScheme extends NormScheme {


	private double maxSpeed;
	private List<AgentData> goals;

	public SimpleMaxSpeedNormScheme(String id, SanctionType sanctionType,
			List<Sensor> sensorList) {
		super(id, sanctionType, sensorList);
		maxSpeed = 4;
		goals = new ArrayList<AgentData>();
		// TODO Auto-generated constructor stub
	}


	@Override
	public List<NormInstantiation> instantiateNorms(RoadNetwork rn, int currentTime,
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
	public boolean violated(AgentData ad) {
		// TODO Auto-generated method stub
		return ad.velocity > maxSpeed;
	}


	@Override
	public boolean deadline(Map<String, AgentData> currentOrgKnowledge,
			int currentTime) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public void addAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
		if(attributes.containsKey("maxspeed")) {
			maxSpeed = Double.parseDouble(attributes.get("maxspeed"));
			goals.add(new AgentData(null, null, -1, maxSpeed, null, -1, -1, -1));
		}
	}


	@Override
	public List<AgentData> getGoals() {
		return goals;
	}
	
}
