package nl.uu.trafficmas.norm;

import java.util.Map;

import nl.uu.trafficmas.agent.Agent;
import nl.uu.trafficmas.simulationmodel.AgentData;

public class MergeNormInstantiation extends NormInstantiation {


	public MergeNormInstantiation(NormScheme ns, String agentID) {
		super(ns, agentID);
		// TODO Auto-generated constructor stub
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

}
