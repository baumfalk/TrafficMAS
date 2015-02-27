package nl.uu.trafficmas.norm;

import nl.uu.trafficmas.agent.Agent;
import nl.uu.trafficmas.organisation.InstitutionalState;

public class Sanction implements InstitutionalState {
	private SanctionType sanctionType;
	private String agentID;
	
	public SanctionType getSanctionType() {
		return sanctionType;
	}
	public void setSanctionType(SanctionType sanctionType) {
		this.sanctionType = sanctionType;
	}
	
	public String agentID() {
		return agentID;
	}
	public void setAgentID(String agentID) {
		this.agentID = agentID;
	}
}
