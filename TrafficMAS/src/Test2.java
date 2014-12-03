import de.tudresden.sumo.cmd.Simulation;
import de.tudresden.sumo.cmd.Vehicle;
import it.polito.appeal.traci.SumoTraciConnection;
 
public class Test2 {
 
    static String sumo_bin = "FillMeInUsingArguments";
    static final String config_file = "sim/hello.sumo.cfg";
     
    public static void main(String[] args) {
    	Test2.sumo_bin = args[0];
        //start Simulation
        SumoTraciConnection conn = new SumoTraciConnection(sumo_bin, config_file);
         
        //set some options
        conn.addOption("step-length", "0.1"); //timestep 100 ms
         
        try{
             
            //start TraCI
            conn.runServer();
 
            //load routes and initialize the simulation
            conn.do_timestep();
             
            for(int i=0; i<3600; i++){
            	System.out.println(i);
                                //current simulation time
                                int simtime = (int) conn.do_job_get(Simulation.getCurrentTime());
 
                conn.do_job_set(Vehicle.add("vehicle"+i, "Car", "route0", simtime, 1, 13.8, (byte) 0));
                conn.do_timestep();
            }
             
            //stop TraCI
            conn.close();
             
        }catch(Exception ex){ex.printStackTrace();}
         
    }
 
}