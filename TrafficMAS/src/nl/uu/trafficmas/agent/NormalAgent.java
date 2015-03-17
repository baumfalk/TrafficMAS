package nl.uu.trafficmas.agent;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import nl.uu.trafficmas.norm.Sanction;
import nl.uu.trafficmas.norm.SanctionType;
import nl.uu.trafficmas.roadnetwork.Node;
import nl.uu.trafficmas.roadnetwork.RoadNetwork;
import nl.uu.trafficmas.roadnetwork.Route;
import de.tudresden.ws.container.SumoColor;

public class NormalAgent extends AgentSumo {

	public NormalAgent(String agentID, Node goalNode, Route route, RoadNetwork roadNetwork, int goalArrivalTime, double maxSpeed, double maxComfySpeed) {
		super(agentID, goalNode, route, roadNetwork, goalArrivalTime, maxSpeed, maxComfySpeed);
	}
	

	@Override
	public SumoColor getColor() {
		//red
		return new SumoColor(255,0,0,255);
	}


	@Override
	protected Map<SanctionType, Double> sanctionIntensity(
			Set<SanctionType> sanctionsTypes, List<Sanction> currentSanctionList) {
		// TODO Auto-generated method stub
		return null;
	}
}
