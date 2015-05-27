package nl.uu.trafficmas.norm;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import nl.uu.trafficmas.roadnetwork.Lane;
import nl.uu.trafficmas.roadnetwork.RoadNetwork;
import nl.uu.trafficmas.roadnetwork.Sensor;
import nl.uu.trafficmas.simulationmodel.AgentData;

public class SimpleMaxSpeedSensorFreeNormScheme extends NormScheme {


	private double maxSpeed;
	private List<AgentData> goals;
	public Set<String> agentNorms;
	private String firstLaneId;
	private String secondLaneId;
	private String thirdLaneId;
	private String deadlineRoad;
	public SimpleMaxSpeedSensorFreeNormScheme(String id, SanctionType sanctionType,
			List<Sensor> sensorList) {
		super(id, sanctionType, sensorList);
		maxSpeed = 4;
		goals = new ArrayList<AgentData>();
		agentNorms = new HashSet<String>();
		// TODO Auto-generated constructor stub
		goals.add(new AgentData(null, null, 1, -1, null, -1, -1, -1));
	}


	@Override
	public List<NormInstantiation> instantiateNorms(RoadNetwork rn, int currentTime,
			Map<String, AgentData> currentOrgKnowledge) {
		
		List<NormInstantiation> list = new ArrayList<NormInstantiation>();
		
		// count number of agents on first lane
		int agentsFirstLaneCount = 0;
		for (Entry<String, AgentData> entry : currentOrgKnowledge.entrySet()) {
			AgentData agentData = entry.getValue();
			Lane l = rn.getRoadFromID(agentData.roadID).getLanes()[agentData.laneIndex];
			if(l.getID().equals(firstLaneId)) {
				agentsFirstLaneCount++;
			}
		}
		
		// TODO: 2 is arbitrary here
		if(agentsFirstLaneCount > 2) {
			for (Entry<String, AgentData> entry : currentOrgKnowledge.entrySet()) {
				AgentData agentData = entry.getValue();
				Lane l = rn.getRoadFromID(agentData.roadID).getLanes()[agentData.laneIndex];
				if(l.getID().equals(firstLaneId)) {
					NormInstantiation ni = new SimpleMaxSpeedSensorFreeNormInstantiation(this, agentData.id);
					ni.addRoadNetwork(rn);
					list.add(ni);
					agentNorms.add(agentData.id);
				}
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
		
		return false;
	}
	
	@Override
	public void addAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
				
		firstLaneId = (String) attributes.get("firstlane");
		secondLaneId = (String) attributes.get("secondlane");
		thirdLaneId = (String) attributes.get("thirdlane");
		setDeadlineRoad((String) attributes.get("deadlineroad"));
	}


	@Override
	public List<AgentData> getGoals() {
		return goals;
	}


	public String getDeadlineRoad() {
		return deadlineRoad;
	}


	public void setDeadlineRoad(String deadlineRoad) {
		this.deadlineRoad = deadlineRoad;
	}
	
}
