package nl.uu.trafficmas.roadnetwork;

import nl.uu.trafficmas.organisation.BruteState;

public class Lane implements BruteState {
	public final LaneType laneType;
	
	public Lane(LaneType laneType) {
		this.laneType = laneType;
	}
}
