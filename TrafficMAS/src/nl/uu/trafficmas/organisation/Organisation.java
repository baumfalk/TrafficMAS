package nl.uu.trafficmas.organisation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.uu.trafficmas.norm.NormInstantiation;
import nl.uu.trafficmas.norm.NormScheme;
import nl.uu.trafficmas.norm.Sanction;
import nl.uu.trafficmas.roadnetwork.Sensor;
import nl.uu.trafficmas.simulationmodel.AgentData;

public class Organisation {
	private ArrayList<NormScheme> normSchemes;
	private ArrayList<NormInstantiation> normInstantiations;
	private ArrayList<Sensor> sensors;
	private Map<String,AgentData> currentOrgKnowledge;
	private ArrayList<InstitutionalState> institutionalStates;
	
	public ArrayList<NormScheme> getNormSchemes() {
		return normSchemes;
	}
	public void setNormSchemes(ArrayList<NormScheme> normSchemes) {
		this.normSchemes = normSchemes;
	}
	
	public ArrayList<NormInstantiation> getNormInstantiations() {
		return normInstantiations;
	}
	public void setNormInstantiations(ArrayList<NormInstantiation> normInstantiations) {
		this.normInstantiations = normInstantiations;
	}
	
	public ArrayList<Sensor> getSensors() {
		return sensors;
	}
	public void setSensors(ArrayList<Sensor> sensors) {
		this.sensors = sensors;
	}
	
	public ArrayList<InstitutionalState> getInstitutionalStates() {
		return institutionalStates;
	}
	public void setInstitutionalStates(ArrayList<InstitutionalState> institutionalStates) {
		this.institutionalStates = institutionalStates;
	}
	public List<Sanction> getNewSanctions() {
		for(NormInstantiation ni : normInstantiations) {

		}
		return null;
	}
	public List<NormInstantiation> getNewNormInstantiations() {
		// TODO Auto-generated method stub
		return null;
	}
	public List<NormInstantiation> getClearedNormInstantiations() {
		// TODO Auto-generated method stub
		return null;
	}
	public void readSensors() {
		currentOrgKnowledge = new HashMap<String,AgentData>();
		for(Sensor s : sensors) {
			for(AgentData ad : s.readSensor()) {
				if(currentOrgKnowledge.containsKey(ad.id)) {
					System.out.println("Org.readsensors: agent " + ad.id + " was already located by a sensor!");
					
					AgentData oldAD = currentOrgKnowledge.get(ad.id);
					System.out.println("Its old road:"+ oldAD.roadID + ", lane:"+ (oldAD.laneIndex) + " and pos:" + oldAD.position);
					System.out.println("Its new road:"+ ad.roadID + ", lane:"+ (ad.laneIndex) + " and pos:" + ad.position);
				}
				currentOrgKnowledge.put(ad.id, ad);
			}
		}
	}
}
