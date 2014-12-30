package nl.uu.trafficmas.simulationmodel;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import de.tudresden.sumo.util.SumoCommand;

public enum QuerySubject {
	Vehicle,
	Lane,
	Edge;

	public boolean hasField(QueryField sf) throws Exception {
		boolean hasField = false;
		switch(this) {
		case Edge:
			hasField = hasFieldEdge(sf);
			break;
		case Lane:
			hasField = hasFieldLane(sf);
			break;
		case Vehicle:
			hasField = hasFieldVehicle(sf);
			break;
		}
		
		return hasField;
	}

	private boolean hasFieldEdge(QueryField sf) throws Exception {
		boolean hasField = false;
		switch(sf) {
		case EdgeId:
			hasField = false;
			break;
		case MeanSpeed:
			hasField = true;
			break;
		case MeanTime:
			hasField = true;
			break;
		case Position:
			hasField = false;
			break;
		case Speed:
			hasField = false;
			break;
		case LeadingVehicle:
			hasField = false;
			break;
		case LaneId:
			hasField = false;
			break;
		default:
			throw new Exception("Case not handled!");	
		}
		return hasField;
	}

	private boolean hasFieldLane(QueryField sf) throws Exception {
		boolean hasField = false;
		switch(sf) {
		case EdgeId:
			hasField = true;
			break;
		case MeanSpeed:
			hasField = true;
			break;
		case MeanTime:
			hasField = true;
			break;
		case Position:
			hasField = false;
			break;
		case Speed:
			hasField = false;
			break;
		case LeadingVehicle:
			hasField = false;
			break;
		case LaneId:
			hasField = false;
			break;
		default:
			throw new Exception("Case not handled!");	
		}
		return hasField;
	}

	private boolean hasFieldVehicle(QueryField sf) throws Exception {
		boolean hasField = false;
		switch(sf) {
		case EdgeId:
			hasField = false;
			break;
		case MeanSpeed:
			hasField = false;
			break;
		case MeanTime:
			hasField = false;
			break;
		case Position:
			hasField = true;
			break;
		case Speed:
			hasField = true;
			break;
		case LeadingVehicle:
			hasField = true;
			break;
		case LaneId:
			hasField = true;
			break;
		default:
			throw new Exception("Case not handled!");
		}
		return hasField;
	}

	public SumoCommand getIDCountCommand() {
		SumoCommand cmd = null;
		switch(this) {
		case Edge:
			cmd = de.tudresden.sumo.cmd.Edge.getIDCount();
			break;
		case Lane:
			cmd = de.tudresden.sumo.cmd.Lane.getIDCount();
			break;
		case Vehicle:
			cmd = de.tudresden.sumo.cmd.Vehicle.getIDCount();
			break;
		}
		return cmd;
	}

	public SumoCommand getIDListCommand() {
		SumoCommand cmd = null;
		switch(this) {
		case Edge:
			cmd = de.tudresden.sumo.cmd.Edge.getIDList();
			break;
		case Lane:
			cmd = de.tudresden.sumo.cmd.Lane.getIDList();
			break;
		case Vehicle:
			cmd = de.tudresden.sumo.cmd.Vehicle.getIDList();
			break;
		}
		return cmd;
	}

	public SumoCommand queryFieldToCommand(QueryField queryField, String id) {
		SumoCommand cmd = null;
		switch(this) {
		case Edge:
			switch(queryField) {
			case MeanSpeed:
				cmd = de.tudresden.sumo.cmd.Edge.getLastStepMeanSpeed(id);
				break;
			case MeanTime:
				cmd = de.tudresden.sumo.cmd.Edge.getTraveltime(id);
				break;
			}
			break;
		case Lane:
			switch(queryField) {
			case MeanSpeed:
				cmd = de.tudresden.sumo.cmd.Lane.getLastStepMeanSpeed(id);
				break;
			case MeanTime:
				cmd = de.tudresden.sumo.cmd.Lane.getTraveltime(id);
				break;
			case EdgeId:
				cmd = de.tudresden.sumo.cmd.Lane.getEdgeID(id);
				break;
			}
			break;
		case Vehicle:
			switch(queryField) {
			case LeadingVehicle:
				//TODO: make distance (100) vehicle type dependent?
				cmd = de.tudresden.sumo.cmd.Vehicle.getLeader(id, 100);
				break;
			case Position:
				cmd = de.tudresden.sumo.cmd.Vehicle.getLanePosition(id);
				break;
			case Speed:
				cmd = de.tudresden.sumo.cmd.Vehicle.getSpeed(id);
				break;
			case LaneId:
				cmd = de.tudresden.sumo.cmd.Vehicle.getLaneID(id);
				break;
			}
			break;
		}
		return cmd;
	}

	public Data toData(LinkedHashSet<QueryField> linkedHashSet,
			List<Object> subList, String id) {
		Data data = null;
		String [] 	edgeId 			= new String[1];
		String [] 	laneId			= new String[1];
		Object [][] leadingVehicle 	= new Object[1][2];
		double [] 	meanSpeed 		= new double[1];
		double [] 	meanTime 		= new double[1];
		double [] 	position 		= new double[1];
		double [] 	speed			= new double[1];
		
		Iterator<QueryField> queryFieldIt = linkedHashSet.iterator();
		Iterator<Object> responseIt = subList.iterator();
		parseResponses(edgeId, laneId, leadingVehicle, meanSpeed, meanTime,
				position, speed, queryFieldIt, responseIt);
		switch (this) {
		case Edge:
			data = new EdgeData(id, meanSpeed[0],meanTime[0]);
			break;
		case Lane:
			data = new LaneData(id, meanSpeed[0], meanTime[0], edgeId[0]);
			break;
		case Vehicle:
			data = new AgentData(id, leadingVehicle[0], position[0], speed[0],laneId[0]);
			break;
		}
		
		return data;
	}

	private void parseResponses(String[] edgeId, String[] laneId, Object[][] leadingVehicle,
			double[] meanSpeed, double[] meanTime, double[] position,
			double[] speed, Iterator<QueryField> queryFieldIt,
			Iterator<Object> responseIt) {
		while(queryFieldIt.hasNext() && responseIt.hasNext()) {
			QueryField queryField = queryFieldIt.next();
			Object response = responseIt.next();
			switch(queryField) {
			case EdgeId:
				edgeId[0] = (String) response;
				break;
			case LeadingVehicle:
				leadingVehicle[0] = (Object[]) response;
				break;
			case MeanSpeed:
				meanSpeed[0] = (double) response;
				break;
			case MeanTime:
				meanTime[0] = (double) response;
				break;
			case Position:
				position[0] = (double) response;
				break;
			case Speed:
				speed[0] = (double) response;
				break;
			case LaneId:
				laneId[0] = (String) response;
				break;
			}
		}
	}
}
