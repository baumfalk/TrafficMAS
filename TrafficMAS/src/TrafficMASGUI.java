import java.awt.EventQueue;
import java.io.File;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;


public class TrafficMASGUI {

	private JFrame mainFrame;
	private JFrame agentsFrame;
	private JFrame scenariosFrame;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TrafficMASGUI window = new TrafficMASGUI();
					window.mainFrame.setVisible(true);
					window.scenariosFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public TrafficMASGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		initializeMainFrame();
		initializeScenariosFrame();
	}

	
	public HashMap<String,String> getScenarioFiles(final File folder) {
		HashMap <String,String> scenarioFiles = new HashMap<String, String>();
	    for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	        	scenarioFiles.putAll(getScenarioFiles(fileEntry));
	        } else {
	        	if(fileEntry.getName().endsWith(".mas.xml")) {
	        		scenarioFiles.put(fileEntry.getName(),fileEntry.getAbsolutePath());
	        	}
	        }
	    }
	    return scenarioFiles;
	}

	
	public void initializeScenariosFrame() {
		scenariosFrame = new JFrame("TrafficMAS - Scenarios");
		scenariosFrame.setBounds(150, 150, 300, 700);
		scenariosFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		scenariosFrame.getContentPane().setLayout(null);

		DefaultListModel<Entry<String,String>> lm = new DefaultListModel<Entry<String,String>>();
		final File folder = new File("./");
		HashMap<String, String> scenarioFiles = getScenarioFiles(folder); 
	    for(Entry<String, String> pair : scenarioFiles.entrySet()) {
	    	lm.addElement(pair);
	    }

		JList<Entry<String,String>> possibleScenarios = new JList<Entry<String,String>>(lm);
		possibleScenarios.setBounds(10, 10, 250, 700);
		scenariosFrame.getContentPane().add(possibleScenarios);
	}

	public void initializeMainFrame() {
		mainFrame = new JFrame("TrafficMAS - <No Scenario Loaded>");
		mainFrame.setBounds(100, 100, 450, 300);
		mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		mainFrame.getContentPane().setLayout(null);

		initializeMenu();
	}

	public void initializeMenu() {
		JMenuBar menuBar = new JMenuBar();
		mainFrame.setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		mnFile.add(new JMenuItem("New"));
		mnFile.add(new JMenuItem("Open"));
		mnFile.addSeparator();
		mnFile.add(new JMenuItem("Save"));
		mnFile.add(new JMenuItem("Save as..."));
		mnFile.add(new JMenuItem("Load"));
		mnFile.addSeparator();

		mnFile.add(new JMenuItem("Exit"));
		menuBar.add(mnFile);
		
		JMenu mnEdit = new JMenu("Edit");
		mnEdit.add(new JMenuItem("Run"));
		mnEdit.add(new JMenuItem("Do Step"));
		mnEdit.add(new JMenuItem("Pause"));
		mnEdit.add(new JMenuItem("Stop"));
		menuBar.add(mnEdit);
		
		JMenu mnView = new JMenu("View");
		mnView.add(new JMenuItem("Agents"));
		mnView.add(new JMenuItem("Organsations"));
		mnView.add(new JMenuItem("Sanctions"));
		mnView.add(new JMenuItem("Scenarios"));

		menuBar.add(mnView);
		
		JMenu mnHelp = new JMenu("Help");
		mnHelp.add(new JMenuItem("About"));

		menuBar.add(mnHelp);
	}

}
