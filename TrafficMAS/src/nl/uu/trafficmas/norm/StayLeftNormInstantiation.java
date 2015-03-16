package nl.uu.trafficmas.norm;

import nl.uu.trafficmas.simulationmodel.AgentData;

public class StayLeftNormInstantiation extends NormInstantiation {

	private AgentData agentData;
	
	public StayLeftNormInstantiation(NormScheme ns, String agentID) {
		super(ns, agentID);
		// TODO Auto-generated constructor stub
	}

	public void setGoal(AgentData agentData){
		this.agentData = agentData;
	}
	
	@Override
	public AgentData goal() {
		return this.agentData;
	}
	
}
