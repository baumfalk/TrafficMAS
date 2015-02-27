package nl.uu.trafficmas.norm;

import java.util.List;
import java.util.Map;

import nl.uu.trafficmas.organisation.Expression;
import nl.uu.trafficmas.roadnetwork.RoadNetwork;
import nl.uu.trafficmas.roadnetwork.Sensor;
import nl.uu.trafficmas.simulationmodel.AgentData;

public abstract class NormScheme {
	protected Expression precondition;
	protected Expression trigger;
	protected Sanction sanction;
	protected List<Sensor> sensorList;
	public final String id;
	public NormScheme(String id, List<Sensor> sensorList) {
		this.id = id;
		this.sensorList = sensorList;
	}
	
	public abstract List<NormInstantiation> instantiateNorms(RoadNetwork rn, Map<String, AgentData> currentOrgKnowledge);
	
	public abstract boolean checkCondition(Map<String, AgentData> currentOrgKnowledge);
	
	protected abstract void runAlgorithm(RoadNetwork rn);
	
	public abstract boolean violated(AgentData ad);

	public abstract Sanction getSanction();

	public abstract boolean deadline(Map<String, AgentData> currentOrgKnowledge, int currentTime);
}
