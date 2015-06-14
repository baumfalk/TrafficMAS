package nl.uu.trafficmas.norm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nl.uu.trafficmas.roadnetwork.RoadNetwork;
import nl.uu.trafficmas.roadnetwork.Sensor;
import nl.uu.trafficmas.simulationmodel.AgentData;

public class SpitsLaneNormScheme extends NormScheme {

	private List<AgentData> goals;
	public SpitsLaneNormScheme(String id, SanctionType sanctionType,
			List<Sensor> sensorList) {
		super(id, sanctionType, sensorList);
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
		// TODO Auto-generated method stub
		
		boolean spitsModus = false; // TODO: put function here
		
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
			int position = 1;
			int speed = -1;
			int laneIndex = 1;
			int deceleration = -1;
			int acceleration = -1;
			goals.remove(1);
		}
		
		// default (i.e. spitsmodus is niet aan: verplicht rechts rijden)
		// als meer dan X mensen gedetecteerd op weg: spitsmodus aan, je mag nu ook links rijden,
		// dus obligatie om links óf rechts te rijden.
		List<NormInstantiation> instances = new ArrayList<NormInstantiation>();
		
			List<AgentData> agentsOnCheckedLane = null;
			for(AgentData ad : agentsOnCheckedLane) {
				SpitsLaneNormInstantiation ni = new SpitsLaneNormInstantiation(this, ad.id);
				instances.add(ni);
			}
			
		return instances;
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
