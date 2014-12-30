package nl.uu.trafficmas.simulationmodel;

import it.polito.appeal.traci.SumoTraciConnection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;

import de.tudresden.sumo.util.SumoCommand;

public class QueryBuilder {
	private LinkedHashSet<QuerySubject> 	querySubjects;
	private LinkedHashMap<QuerySubject,
		LinkedHashSet<QueryField>> 			querySubjectFields;
	private LinkedHashMap<QuerySubject,
		List<String>> 						subjectIDs;
	
	private StateData 					stateData;
	private HashMap<String,AgentData> 	agentsData;
	private HashMap<String,EdgeData> 	edgesData;
	private HashMap<String,LaneData> 	lanesData;
	
	public QueryBuilder() {
		querySubjects 		= new LinkedHashSet<QuerySubject>();
		querySubjectFields 	= new LinkedHashMap<QuerySubject,LinkedHashSet<QueryField>>();
		subjectIDs			= new LinkedHashMap<QuerySubject,List<String>>();
	}
	
	public StateData getStateData() {
		return stateData;
	}

	public HashMap<String, AgentData> getAgentsData() {
		return agentsData;
	}

	public HashMap<String, EdgeData> getEdgesData() {
		return edgesData;
	}

	public HashMap<String, LaneData> getLanesData() {
		return lanesData;
	}

	public void addQuerySubject(QuerySubject sq) {
		querySubjects.add(sq);
		if(!querySubjectFields.containsKey(sq)){
			querySubjectFields.put(sq, new LinkedHashSet<QueryField>());
		}
	}
	
	public void addQueryField(QuerySubject sq, QueryField sf) {
		if(!querySubjects.contains(sq)) {
			this.addQuerySubject(sq);
		}
		if(sq.hasField(sf)) {
			querySubjectFields.get(sq).add(sf);
		}
	}
	
	public void executeQuery(SumoTraciConnection conn) throws Exception {
		// for what subjects do we need to get info?
		ArrayList<SumoCommand> cmdList = new ArrayList<SumoCommand>();
		cmdList.add(de.tudresden.sumo.cmd.Simulation.getCurrentTime());
		for(QuerySubject querySubject : querySubjects) {
			cmdList.add(querySubject.getIDCountCommand());
			cmdList.add(querySubject.getIDListCommand());
		}
		ArrayList<Object> responses = conn.do_jobs_get(cmdList);
		
		cmdList.clear();
		
		int currentTimeStep = (int) responses.remove(0);
		
		// generate the new queries
		generateQueries(cmdList, responses);
		// responses should now be empty!
		assert(responses.isEmpty());
		
		// get new responses
		responses = conn.do_jobs_get(cmdList);
		
		processResponses(responses);
		assert(responses.isEmpty());
		
		stateData = new StateData(agentsData, edgesData, lanesData, currentTimeStep);
		querySubjects.clear();
	}

	private void processResponses(ArrayList<Object> responses) {
		agentsData 	= new HashMap<>();
		edgesData 	= new HashMap<>();
		lanesData 	= new HashMap<>();
		for(QuerySubject querySubject : querySubjects) {
			int numberOfFields = querySubjectFields.get(querySubject).size();
			for(String id : subjectIDs.get(querySubject)) {
				List<Object> subList = responses.subList(0, numberOfFields);
				
				Data data = querySubject.toData(querySubjectFields.get(querySubject), subList, id);

				switch(querySubject) {
				case Edge:
					edgesData.put(id, (EdgeData) data);
					break;
				case Lane:
					lanesData.put(id, (LaneData) data);
					break;
				case Vehicle:
					agentsData.put(id, (AgentData) data);
					break;
				}
				
				responses.subList(0, numberOfFields).clear();
			}
		}
	}

	private void generateQueries(ArrayList<SumoCommand> cmdList,
			ArrayList<Object> responses) {
		// how many ids are there for each subject,
		// and what field do we need to retrieve for each id?
		for(QuerySubject querySubject : querySubjects) {
			int idCount 			= (int) responses.remove(0);
			List<Object> subList 	= responses.subList(0, idCount);
			List<String> idList = new ArrayList<String>();
			for(Object o : subList) {
				idList.add((String)o);
			}
			responses.subList(0, idCount).clear();
			subjectIDs.put(querySubject, idList);
			// prepare queries
			for(String id : subjectIDs.get(querySubject)) {
				for(QueryField queryField : querySubjectFields.get(querySubject)) {
					cmdList.add(querySubject.queryFieldToCommand(queryField,id));
				}
			}
		}
	}
}
