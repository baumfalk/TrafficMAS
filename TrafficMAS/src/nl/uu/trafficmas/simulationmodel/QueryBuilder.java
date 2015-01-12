package nl.uu.trafficmas.simulationmodel;

import it.polito.appeal.traci.SumoTraciConnection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;

import de.tudresden.sumo.config.Constants;
import de.tudresden.sumo.util.SumoCommand;
import de.tudresden.ws.container.SumoStringList;

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
	private boolean timeStep;
	
	public QueryBuilder() {
		querySubjects 		= new LinkedHashSet<QuerySubject>();
		querySubjectFields 	= new LinkedHashMap<QuerySubject,LinkedHashSet<QueryField>>();
		subjectIDs			= new LinkedHashMap<QuerySubject,List<String>>();
		timeStep 			= false;
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
	
	public void addQueryField(QuerySubject sq, QueryField sf) throws Exception {
		if(!querySubjects.contains(sq)) {
			this.addQuerySubject(sq);
		}
		querySubjectFields.get(sq).add(sf);
	}
	
	public void executeQuery(SumoTraciConnection conn) throws Exception {
		// for what subjects do we need to get info?
		ArrayList<SumoCommand> cmdList = new ArrayList<SumoCommand>();
		if(timeStep) {
			cmdList.add(new SumoCommand(Constants.CMD_SIMSTEP2, 0));
		}
		cmdList.add(de.tudresden.sumo.cmd.Simulation.getCurrentTime());
		for(QuerySubject querySubject : querySubjects) {
			cmdList.add(querySubject.getIDListCommand());
		}
		long start_time = System.nanoTime();
		
		ArrayList<Object> responses = conn.do_jobs_get(cmdList);
		long end_time = System.nanoTime();
		double difference = (end_time - start_time)/1e6;
		System.out.println("Querybuilder: get id lists " + difference );
		cmdList.clear();
		
		// first response is null/or subscribed events
		if(timeStep) {
			responses.remove(0);
		}
		int currentTimeStep = (int) responses.remove(0);
		
		// generate the new queries
		generateQueries(cmdList, responses);
		// responses should now be empty!
		assert(responses.isEmpty());

		
		// get new responses
		start_time = System.nanoTime();
		responses = conn.do_jobs_get(cmdList);
		end_time = System.nanoTime();
		difference = (end_time - start_time)/1e6;
		System.out.println("Querybuilder: get stuff " + difference );
		processResponses(responses);
		assert(responses.isEmpty());
		
		stateData = new StateData(agentsData, edgesData, lanesData, currentTimeStep);
		querySubjects.clear();
	}

	private void processResponses(ArrayList<Object> responses) {
		agentsData 	= new LinkedHashMap<>();
		edgesData 	= new LinkedHashMap<>();
		lanesData 	= new LinkedHashMap<>();
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
			ArrayList<Object> responses) throws Exception {
		// how many ids are there for each subject,
		// and what field do we need to retrieve for each id?
		for(QuerySubject querySubject : querySubjects) {
			//SumoStringList s				= (SumoStringList) responses.remove(0);
			SumoStringList subList 	= (SumoStringList) responses.remove(0);
			List<String> idList = new ArrayList<String>(subList);
			subjectIDs.put(querySubject, idList);
			// prepare queries
			for(String id : subjectIDs.get(querySubject)) {
				for(QueryField queryField : querySubjectFields.get(querySubject)) {
					cmdList.add(querySubject.queryFieldToCommand(queryField,id));
				}
			}
		}
	}

	public void doNextTimeStep() {
		// TODO Auto-generated method stub
		this.timeStep = true;
	}
}
