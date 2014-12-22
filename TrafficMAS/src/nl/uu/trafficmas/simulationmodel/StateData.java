package nl.uu.trafficmas.simulationmodel;

import java.util.HashMap;

public class StateData {
	public final HashMap<String, AgentData> agentsData;
	public final HashMap<String, EdgeData> edgesData;
	public final HashMap<String, LaneData> lanesData;
	public final int currentTimeStep;
	
	public StateData(HashMap<String, AgentData> agentsData,
			HashMap<String, EdgeData> edgesData,
			HashMap<String, LaneData> lanesData, int currentTimeStep) {
		this.agentsData 		= agentsData;
		this.edgesData 			= edgesData;
		this.lanesData 			= lanesData;
		this.currentTimeStep 	= currentTimeStep;
	}
}