package GUI;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;

import InputOutput.Elevator;

public class MainWindow extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static Elevator elev;
	
	private JList<String> travelList;
	private static DefaultListModel<String> travelListModel;
	private JScrollPane travelScroll;
	private JLabel stateLabel, travelLabel;
	private static JButton moveButton, closeButton;
	private JTextArea stateArea;
	private GridBagConstraints smallConstraint, largeConstraint;
	private int travels = 0;
	
	
	
	public static JButton getMoveButton() {
		return moveButton;
	}

	

	public static DefaultListModel<String> getTravelListModel() {
		return travelListModel;
	}



	public JTextArea getStateArea() {
		return stateArea;
	}

	public MainWindow() {
		super("Elevator Simulator");
		setSize(800,600);
		
		smallConstraint = new GridBagConstraints();
		smallConstraint.weightx = 1;
		smallConstraint.weighty = 1;
		
		largeConstraint = new GridBagConstraints();
		largeConstraint.weightx = 1;
		largeConstraint.weighty = 3;
		getContentPane().setLayout(new GridBagLayout());
		
		stateLabel = new JLabel("Current state:");
		travelLabel = new JLabel("Travel history:");
		
		smallConstraint.gridx = 0;
		smallConstraint.gridy = 0;
		getContentPane().add(stateLabel,smallConstraint);
		smallConstraint.gridx = 1;
		getContentPane().add(travelLabel,smallConstraint);
		
		stateArea = new JTextArea();
		stateArea.setPreferredSize(new Dimension(400,400));
		stateArea.setEditable(false);
		largeConstraint.gridx = 0;
		largeConstraint.gridy = 1;
		getContentPane().add(stateArea,largeConstraint);
		
		travelListModel = new DefaultListModel<>();
		travelList = new JList<>(travelListModel);
		travelList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		travelList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		travelList.setVisibleRowCount(-1);
		travelScroll = new JScrollPane(travelList);
		travelScroll.setPreferredSize(new Dimension(400,400));
		largeConstraint.gridx = 1;
		getContentPane().add(travelScroll,largeConstraint);
		
		moveButton = new JButton("Move the elevator!");
		moveButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				changeFloor();
			}
		});
		smallConstraint.gridx = 0;
		smallConstraint.gridy = 2;
		getContentPane().add(moveButton,smallConstraint);
		
		closeButton = new JButton("Close the program!");
		closeButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
				
			}
		});
		smallConstraint.gridx = 1;
		getContentPane().add(closeButton,smallConstraint);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
		
	}
	
	public void updateState() {
		Timer t1 = new Timer("each_second_trigger", false);
		t1.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				getStateArea().setText("");
				try {
					getStateArea().setText(elev.printState());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}, 0, 1000);
	}
	
	public void changeFloor() {
		
		String name  = "";
		name = (String) JOptionPane.showInputDialog(null, "Enter the name of the passenger!\n"
				+ "Leave it blank for the default one.");
		if (name != null) {
			travels++;
			travelListModel.addElement("Travel no. " + String.valueOf(travels) + ":");
			moveButton.setEnabled(false);
			moveButton.setText("Currently driving...");
			if (name.trim().equals("")) {
				elev.changeFloor();
			}
			else {
				elev.changeFloorManual(name);
			}
		}
	}
	
public static void main(String[] args) throws InterruptedException {
		
		elev = new Elevator();
		elev.provideTime();
		MainWindow gui = new MainWindow();
		gui.updateState();
	}

}
