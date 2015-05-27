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
	
		
		if(((SimpleMaxSpeedNormScheme)this.ns).agentNorms.contains(this.agentID))
		{
			((SimpleMaxSpeedNormScheme)this.ns).agentNorms.remove(this.agentID);
			return true;
		}
		return false;
	}
}
