package nl.uu.trafficmas.norm;

import nl.uu.trafficmas.organisation.InstitutionalState;

public class Sanction implements InstitutionalState {
	public final SanctionType sanctionType;
	public final String agentID;
	
	public Sanction(SanctionType sanctionType, String agentID) {
		this.sanctionType = sanctionType;
		this.agentID	= agentID;
	}
	
}
