package nl.uu.trafficmas.norm;

import java.util.List;
import java.util.Map;

import nl.uu.trafficmas.agent.Agent;
import nl.uu.trafficmas.organisation.BruteState;
import nl.uu.trafficmas.simulationmodel.AgentData;

public class NormInstantiation {
	private NormScheme ns;
	private Agent agent;
	
	private boolean evaluateNorm(List<BruteState> bf, NormScheme ns) {
		return false;
	}
	
	private Sanction instantiateSanction(Agent agent, SanctionType st) {
		return null;
	}

	public String agentID() {
		// TODO Auto-generated method stub
		return null;
	}

	public Agent getAgent() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean violated(AgentData ad) {
		// TODO Auto-generated method stub
		return false;
	}

	public Sanction getSanction() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean deadline(Map<String, AgentData> currentOrgKnowledge,
			int currentTime) {
		// TODO Auto-generated method stub
		return false;
	}
}
