package nl.uu.trafficmas.organisation;

import java.util.ArrayList;
import java.util.List;

import nl.uu.trafficmas.norm.NormInstantiation;
import nl.uu.trafficmas.norm.NormScheme;
import nl.uu.trafficmas.roadnetwork.Sensor;

public class Organisation {
	private ArrayList<NormScheme> normSchemes;
	private ArrayList<NormInstantiation> normInstantiations;
	private ArrayList<Sensor> sensors;
	private ArrayList<BruteState> bruteStates;
	private ArrayList<InstitutionalState> institutionalStates;
	
	private List<BruteState>readSensors(List<Sensor> sn){
		return null;
	}
	private List<BruteState >updateBeliefs(List<BruteState> oldBF, List<BruteState> newBF) {
		return null;
	}
	private List<NormInstantiation> instantiateNorms(List<BruteState> bf, List<NormScheme> ns, List<NormInstantiation> ni) {
		return null;
	}
	private List<NormInstantiation> clearNorms(List<BruteState> bf, List<BruteState> ni) {
		return null;
	}
	private List<NormInstantiation> enforceNorms(List<BruteState> bf, List<NormInstantiation> ni) {
		return null;
	}
	
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
	
	public ArrayList<BruteState> getBruteStates() {
		return bruteStates;
	}
	public void setBruteStates(ArrayList<BruteState> bruteStates) {
		this.bruteStates = bruteStates;
	}
	
	public ArrayList<InstitutionalState> getInstitutionalStates() {
		return institutionalStates;
	}
	public void setInstitutionalStates(ArrayList<InstitutionalState> institutionalStates) {
		this.institutionalStates = institutionalStates;
	}
}
