package nl.uu.trafficmas.norm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nl.uu.trafficmas.simulationmodel.AgentData;

public class MergeNormInstantiation extends NormInstantiation {


	private double speed;
	List<AgentData> goals;

	public MergeNormInstantiation(NormScheme ns, String agentID) {
		super(ns, agentID);
		this.speed = 0;
		goals = new ArrayList<AgentData>();
	}

	@Override
	public boolean violated(AgentData ad) {
		//TODO: some leeway, i.e. 3% deviation from the target speed?
		// Agent can only receive a violation while on sensor 3.
		return ad.velocity != speed ;
	}

	@Override
	public boolean deadline(Map<String, AgentData> currentOrgKnowledge,
			int currentTime) {
		
		AgentData agentData = currentOrgKnowledge.get(agentID);
		MergeNormScheme mergeNormScheme = (MergeNormScheme)ns;
		return mergeNormScheme.mergeSensor.readSensor().contains(agentData);
	}


	public void setSpeed(double speed) {
		this.speed = speed;
		goals.add(new AgentData(null, null, -1, speed, null, -1, -1, -1));
	}

	public double getSpeed() {
		return speed;
	}
	
	public List<AgentData> getGoals() {
		return goals;
	}
}
