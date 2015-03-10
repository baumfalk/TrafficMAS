package nl.uu.trafficmas.norm;

import java.util.Map;

import nl.uu.trafficmas.roadnetwork.Sensor;
import nl.uu.trafficmas.simulationmodel.AgentData;

public class MergeNormInstantiation extends NormInstantiation {


	private double speed;


	public MergeNormInstantiation(NormScheme ns, String agentID) {
		super(ns, agentID);
		this.speed = 0;
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean violated(AgentData ad) {
		//TODO: some leeway, i.e. 3% deviation from the target speed?
		// Agent can only receive a violation while on sensor 3.
		return !(ad.velocity > speed*0.85 && ad.velocity < speed*1.15);
	}

	@Override
	public boolean deadline(Map<String, AgentData> currentOrgKnowledge,
			int currentTime) {
		// TODO Auto-generated method stub
		return false;
	}


	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public double getSpeed() {
		return speed;
	}
	
	public AgentData goal() {
		AgentData ad = new AgentData(null, null, -1, speed, null, -1, -1, -1);
		return ad;
	}
}
