package nl.uu.trafficmas.roadnetwork;

import nl.uu.trafficmas.organisation.BruteState;

public class Lane implements BruteState {
		
	public LaneType laneType;
	public byte laneIndex;
	
	public Lane(LaneType laneType, byte laneIndex) {
		this.laneIndex = laneIndex;
		this.laneType = laneType;
	}
	
	public LaneType getLaneType(){
		return laneType;
	}
}
