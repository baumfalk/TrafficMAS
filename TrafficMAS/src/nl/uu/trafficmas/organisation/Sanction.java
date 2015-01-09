package nl.uu.trafficmas.organisation;

import nl.uu.trafficmas.agent.Agent;

public class Sanction implements InstitutionalState {
	private Agent agent;
	private SanctionType sanctionType;
	
	public SanctionType getSanctionType() {
		return sanctionType;
	}
	public void setSanctionType(SanctionType sanctionType) {
		this.sanctionType = sanctionType;
	}
	
	public Agent getAgent() {
		return agent;
	}
	public void setAgent(Agent agent) {
		this.agent = agent;
	}
}
