package nl.uu.trafficmas.controller;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nl.uu.trafficmas.norm.NormInstantiation;
import nl.uu.trafficmas.norm.Sanction;
import nl.uu.trafficmas.simulationmodel.AgentData;
import nl.uu.trafficmas.simulationmodel.StateData;

public class Statistics {
	
	List<Map<String, List<Sanction>>> sanctionsLog;
	List<Map<String, List<NormInstantiation>>> newNormsLog;
	List<Map<String, List<NormInstantiation>>> clearedNormsLog;
	List<StateData> stateDataLog;
	List<Double> averageSpeedPerTick;
	List<Double> averageGapPerTick;
	List<Integer> departuresPerTick;

	double averageSpeedInNetwork;
	
	// total number of vehicles that have left the network/simulation time
	double throughput; // average number of vehicles leaving network per minute
	// every tick:
	// add all gap distances of all cars/divide by current number of cars
	// add all tick-averages, divide by total simulation length
	
	double averageGap; // average distance between leader and car
	// every tick: add all sanctions issued by every organization
	int sanctionsIssued;
	
	public Statistics(int simulationLength) {
		sanctionsLog 	= new ArrayList<>(simulationLength);
		newNormsLog 	= new ArrayList<>(simulationLength);
		clearedNormsLog = new ArrayList<>(simulationLength);
		stateDataLog 	= new ArrayList<>(simulationLength);
		averageGapPerTick	= new ArrayList<>(simulationLength);
		averageSpeedPerTick = new ArrayList<>(simulationLength);
		departuresPerTick	= new ArrayList<>(simulationLength);
	}
	
	public void save(String dir, String title) {
		Path p = FileSystems.getDefault().getPath(dir,title+".txt");
		List<String> fileContent = new ArrayList<>();

		long totalSpeed = 0;
		for(Double speed : averageSpeedPerTick) {
			totalSpeed += speed;
		}
		averageSpeedInNetwork = totalSpeed/averageSpeedPerTick.size();
		
		double totalGap = 0;
		for(Double gap : averageGapPerTick) {
			if(gap.isNaN()){
				gap = 100.0;
			}
			totalGap += gap;
		}
		averageGap = totalGap/averageGapPerTick.size();
		
		double totalDepartures = 0;
		for(int departures : departuresPerTick) {
			totalDepartures += departures;
		}
		throughput = totalDepartures/departuresPerTick.size()*60;
		
		
		fileContent.add("Throughput:" + Double.toString(throughput));
		fileContent.add("Average Network Speed:" + Double.toString(averageSpeedInNetwork));
		fileContent.add("Average Gap:" + Double.toString(averageGap));
		fileContent.add("Sanctions Issued:" + Integer.toString(sanctionsIssued));
		
		try {
			Files.write(p, fileContent, Charset.defaultCharset());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void addSanctions(int currentTime,
			Map<String, List<Sanction>> sanctions) {
		// TODO Auto-generated method stub
		sanctionsLog.add(sanctions);
		for(List<Sanction> s : sanctions.values()) {
			sanctionsIssued += s.size();
		}
	}

	public void addNewNorms(int currentTime,
			Map<String, List<NormInstantiation>> normInst) {
		// TODO Auto-generated method stub
		newNormsLog.add(normInst);
	}

	public void addRemovedNorms(int currentTime,
			Map<String, List<NormInstantiation>> clearedNormInst) {
		// TODO Auto-generated method stub
		clearedNormsLog.add(clearedNormInst);
	}

	public void addStateData(StateData simulationStateData, int i) {
		stateDataLog.add(simulationStateData);
		
		double totalSpeed 	= 0;
		double totalGap		= 0;
		for(AgentData ad : simulationStateData.agentsData.values()) {
			totalSpeed 	+= ad.velocity;
			totalGap	+= ad.leaderDistance;
		}
		averageSpeedPerTick.add(totalSpeed/simulationStateData.agentsData.size());
		averageGapPerTick.add(totalGap/simulationStateData.agentsData.size());
		departuresPerTick.add(simulationStateData.departedList.size());
	}
	
}
