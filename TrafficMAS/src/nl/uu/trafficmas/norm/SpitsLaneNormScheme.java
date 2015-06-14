package nl.uu.trafficmas.norm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nl.uu.trafficmas.roadnetwork.RoadNetwork;
import nl.uu.trafficmas.roadnetwork.Sensor;
import nl.uu.trafficmas.simulationmodel.AgentData;

public class SpitsLaneNormScheme extends NormScheme {

	private Sensor main0Sensor;
	private Sensor main1Sensor;
	public Sensor control0Sensor;
	public Sensor control1Sensor;
	public boolean spitsModus;

	
	private List<AgentData> goals;
	public SpitsLaneNormScheme(String id, SanctionType sanctionType,
			List<Sensor> sensorList) {
		super(id, sanctionType, sensorList);
		main0Sensor 	= sensorList.get(0);
		main1Sensor 	= sensorList.get(1);
		control0Sensor	= sensorList.get(2);
		control1Sensor	= sensorList.get(3);
		goals = new ArrayList<AgentData>();
		int position = 1;
		int speed = -1;
		int laneIndex = 0;
		int deceleration = -1;
		int acceleration = -1;
		goals.add(new AgentData(null, null, position, speed, null, laneIndex, deceleration, acceleration));
	}

	@Override
	public List<NormInstantiation> instantiateNorms(RoadNetwork rn,
			int currentTime, Map<String, AgentData> currentOrgKnowledge) {
		
		List<AgentData> main0List = main0Sensor.readSensor();
		List<AgentData> main1List = main1Sensor.readSensor();

		spitsModus = (main0List.size() > 2); 	
		
		// what to do if spitsmode is turned on
		if(spitsModus && goals.size() == 1) {
			int position = 1;
			int speed = -1;
			int laneIndex = 1;
			int deceleration = -1;
			int acceleration = -1;
			goals.add(new AgentData(null, null, position, speed, null, laneIndex, deceleration, acceleration));
		}
		// what to do if spitsmode is turned off
		if(!spitsModus && goals.size() == 2) {
			goals.remove(1);
		}
		
		// default (i.e. spitsmodus is niet aan: verplicht rechts rijden)
		// als meer dan X mensen gedetecteerd op weg: spitsmodus aan, je mag nu ook links rijden,
		// dus obligatie om links of rechts te rijden.
		List<NormInstantiation> instances = new ArrayList<NormInstantiation>();
		
		main0List.addAll(main1List);
		
		List<AgentData> agentsOnCheckedLane = main0List;
		for(AgentData ad : agentsOnCheckedLane) {
			SpitsLaneNormInstantiation ni = new SpitsLaneNormInstantiation(this, ad.id);
			instances.add(ni);
		}
		
		return instances;
	}

	
	
	
	@Override
	public boolean checkCondition(Map<String, AgentData> currentOrgKnowledge) {
		return true;
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
		return goals;
	}

}
