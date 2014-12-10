import de.tudresden.sumo.cmd.ArealDetector;
import de.tudresden.sumo.cmd.Route;
import de.tudresden.sumo.cmd.Simulation;
import de.tudresden.sumo.cmd.Vehicle;
import de.tudresden.sumo.util.SumoCommand;
import it.polito.appeal.traci.SumoTraciConnection;
import nl.uu.trafficmas.*;
import nl.uu.trafficmas.agent.*;
 
public class Test2 {
 
    static String sumo_bin = "FillMeInUsingArguments";
    static final String config_file = "sim/hello.sumocfg";
     
    public static void main(String[] args) {
    	Test2.sumo_bin = args[0];
        //start Simulation
        SumoTraciConnection conn = new SumoTraciConnection(sumo_bin, config_file);
         
        //set some options
        conn.addOption("step-length", "1"); //timestep 10 ms
         
        try{
             
            //start TraCI
            conn.runServer();
 
            //load routes and initialize the simulation
            conn.do_timestep();
            double lastStepSpeedN42 = 0;
            double lastStepSpeedA28 = 0;
            int j =3600;
            for(int i=0; i<3600; i++){
            	
            	//System.out.println(i);
                                //current simulation time
                                int simtime = (int) conn.do_job_get(Simulation.getCurrentTime());
 
                Agent a;

                conn.do_job_set(Vehicle.add("vehicle"+i, "Car", "route0", simtime, 1, 13.8, (byte) 0));
                conn.do_job_set(Vehicle.add("vehicle"+j, "Car", "route1", simtime, 1, 13.8, (byte) 0));
                
                lastStepSpeedN42 = (double) conn.do_job_get(ArealDetector.getLastStepMeanSpeed("N42lane0"));
                lastStepSpeedA28 = (double) conn.do_job_get(ArealDetector.getLastStepMeanSpeed("A28_350_lane0_0"));

                conn.do_timestep();
                System.out.println("Simulation time: " + simtime);
                if(lastStepSpeedN42 > 0){
                	System.out.println("Last step mean speed N42: " + lastStepSpeedN42);
                }
                if(lastStepSpeedA28 > 0){
                	System.out.println("Last step mean speed A28: " + lastStepSpeedA28);
                }
                
                
                j++;
            }
             
            //stop TraCI
            conn.close();
             
        }catch(Exception ex){ex.printStackTrace();}
         
    }
 
}