package nl.uu.trafficmas.roadnetwork;

import java.util.ArrayList;

public class Route {
	public final String routeID;
	private ArrayList<Edge> route;
	
	public Route(String routeID,ArrayList<Edge> route) {
		this.routeID = routeID;
		this.route = route;
	}
	
	public Edge[] getRoute() {
		Edge [] routeArray = new Edge[route.size()];
		return route.toArray(routeArray);
	}
}
