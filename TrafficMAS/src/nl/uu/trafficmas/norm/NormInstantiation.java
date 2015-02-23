package nl.uu.trafficmas.norm;

import java.util.List;

import nl.uu.trafficmas.agent.Agent;
import nl.uu.trafficmas.organisation.BruteState;
import nl.uu.trafficmas.organisation.Sanction;
import nl.uu.trafficmas.organisation.SanctionType;

public class NormInstantiation {
	private NormScheme ns;
	private Agent agent;
	
	private boolean evaluateNorm(List<BruteState> bf, NormScheme ns) {
		return false;
	}
	
	private Sanction instantiateSanction(Agent agent, SanctionType st) {
		return null;
	}
}
