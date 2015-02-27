package nl.uu.trafficmas.norm;

import java.util.Map;

import nl.uu.trafficmas.agent.Agent;
import nl.uu.trafficmas.simulationmodel.AgentData;

public abstract class NormInstantiation {
	private final NormScheme ns;
	public final String agentID;
	
	public NormInstantiation(NormScheme ns, String agentID) {
		this.ns = ns;
		this.agentID = agentID;
	}
	public String agentID() {
		return agentID;
	}

	public boolean violated(AgentData ad) {
		return ns.violated(ad);
	};

	public Sanction getSanction() {
		return ns.getSanction();
	}

	public boolean deadline(Map<String, AgentData> currentOrgKnowledge, int currentTime) {
		return ns.deadline(currentOrgKnowledge, currentTime);
	}
}
