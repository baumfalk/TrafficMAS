package nl.uu.trafficmas.norm;

import nl.uu.trafficmas.simulationmodel.AgentData;

public class StayInLaneNormInstantiation extends NormInstantiation {

	private AgentData agentData;
	
	public StayInLaneNormInstantiation(NormScheme ns, String agentID) {
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
