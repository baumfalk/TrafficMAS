package nl.uu.trafficmas.norm;

import java.util.List;

import nl.uu.trafficmas.agent.Agent;
import nl.uu.trafficmas.organisation.Expression;
import nl.uu.trafficmas.roadnetwork.RoadNetwork;
import nl.uu.trafficmas.roadnetwork.Sensor;

public abstract class NormScheme {
	protected Expression precondition;
	protected Expression trigger;
	protected Sanction sanction;
	protected List<Sensor> sensorList;
	
	public NormScheme(List<Sensor> sensorList) {
		this.sensorList = sensorList;
	}
	
	public abstract List<NormInstantiation> instantiateNorms(List<Agent> agents,RoadNetwork rn);
	
	
	public abstract boolean checkCondition();
	
	protected abstract void runAlgorithm(RoadNetwork rn);
}
