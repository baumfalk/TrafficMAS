package nl.uu.trafficmas;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.JComboBox;

public class TrafficGUI {

	private JFrame frame;
	private JList<String> list;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TrafficGUI window = new TrafficGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public TrafficGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
	
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton btnJustdoit = new JButton("Run Scenario");
		btnJustdoit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TrafficMAS tm = new TrafficMAS("sim/hello.mas.xml", "sumo-gui");
				tm.run();
			}
		});
		/*
		//JComboBox comboBox = new JComboBox();

		
		JComboBox<String> bookList = new JComboBox<>(bookTitles);
		 
		// add to the parent container (e.g. a JFrame):
		frame.getContentPane().add(bookList);
		 
		// get the selected item:
		String selectedBook = (String) bookList.getSelectedItem();
		System.out.println("You selected the book: " + selectedBook);
		*/
		String[] bookTitles = new String[] {"Effective Java", "Head First Java",
                "Thinking in Java", "Java for Dummies"};
		
		
		JComboBox<String> comboBox = new JComboBox<String>();
		comboBox.addItem("takeOverScenario");
		comboBox.addItem("hello");
		comboBox.setBounds(12, 107, 117, 24);
		frame.getContentPane().add(comboBox);
		
		btnJustdoit.setBounds(12, 238, 165, 25);
		frame.getContentPane().add(btnJustdoit);
		
		
		
		
	}
}
