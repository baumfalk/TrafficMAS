package nl.uu.trafficmas.norm;

import java.util.Map;

import nl.uu.trafficmas.roadnetwork.Sensor;
import nl.uu.trafficmas.simulationmodel.AgentData;



public class SimpleMaxSpeedSensorFreeNormInstantiation extends NormInstantiation {

	public SimpleMaxSpeedSensorFreeNormInstantiation(NormScheme ns, String agentID) {
		super(ns, agentID);
	}
	
	@Override
	public boolean deadline(Map<String, AgentData> currentOrgKnowledge, int currentTime) {
	
		// TODO: here something for checking if the agent is on the correct lane?
		AgentData ad = currentOrgKnowledge.get(this.agentID);
		SimpleMaxSpeedSensorFreeNormScheme castedNS = (SimpleMaxSpeedSensorFreeNormScheme)this.ns;
		if(castedNS.agentNorms.contains(this.agentID) && castedNS.getDeadlineRoad().equals(ad.roadID))
		{
			castedNS.agentNorms.remove(this.agentID);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean violated(AgentData ad) {
		SimpleMaxSpeedSensorFreeNormScheme castedNS = (SimpleMaxSpeedSensorFreeNormScheme)this.ns;
		
		return ad.laneIndex != 1;
	}
}
