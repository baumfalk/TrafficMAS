package nl.uu.trafficmas.norm;

import nl.uu.trafficmas.agent.Agent;
import nl.uu.trafficmas.organisation.InstitutionalState;

public class Sanction implements InstitutionalState {
	private Agent agent;
	private SanctionType sanctionType;
	
	public SanctionType getSanctionType() {
		return sanctionType;
	}
	
	public Agent getAgent() {
		return agent;
	}
	public void setAgent(Agent agent) {
		this.agent = agent;
	}
}
