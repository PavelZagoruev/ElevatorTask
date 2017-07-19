package by.epamlab.frames;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.Color;
import java.awt.GridBagLayout;

import javax.swing.JLabel;

import java.awt.GridBagConstraints;

import javax.swing.JSpinner;

import java.awt.Insets;

import javax.swing.SwingConstants;
import javax.swing.SpinnerNumberModel;
import javax.swing.JSlider;
import javax.swing.JButton;

import by.epamlab.beans.BuildTask;
import by.epamlab.beans.Parametrs;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class MainFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	AnimationPanel panelAnimation;
	Thread threaAnimation;
	
	private JPanel contentPane;
	private JSpinner spNumberOfPeople;
	private JSpinner spCapacityElevator;
	private JSpinner spNumberOfFloors;
	
	private JSlider slider;
	
	private JButton btnStart;
	
	private BuildTask build;
	private JLabel lblAnimationSpeed;
	private JLabel lblFaster;
	private JLabel lblSlower;
	
	/**
	 * Create the frame.
	 */
	public MainFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 559, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		panelAnimation = new AnimationPanel();
		panelAnimation.setBackground(Color.WHITE);
		contentPane.add(panelAnimation, BorderLayout.CENTER);
		
		JPanel panelMenu = new JPanel();
		contentPane.add(panelMenu, BorderLayout.EAST);
		GridBagLayout gbl_panelMenu = new GridBagLayout();
		gbl_panelMenu.columnWidths = new int[]{110, 80};
		gbl_panelMenu.rowHeights = new int[] {0, 0, 0, 30, 30, 30, 0, 0, 0, 0};
		gbl_panelMenu.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_panelMenu.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panelMenu.setLayout(gbl_panelMenu);
		
		JLabel lblNumberOfPeople = new JLabel("Number of people:");
		lblNumberOfPeople.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_lblNumberOfPeople = new GridBagConstraints();
		gbc_lblNumberOfPeople.anchor = GridBagConstraints.WEST;
		gbc_lblNumberOfPeople.insets = new Insets(0, 0, 5, 5);
		gbc_lblNumberOfPeople.gridx = 0;
		gbc_lblNumberOfPeople.gridy = 0;
		panelMenu.add(lblNumberOfPeople, gbc_lblNumberOfPeople);
		
		JLabel lblCapacityElevator = new JLabel("Capacity elevator:");
		lblCapacityElevator.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_lblCapacityElevator = new GridBagConstraints();
		gbc_lblCapacityElevator.anchor = GridBagConstraints.WEST;
		gbc_lblCapacityElevator.insets = new Insets(0, 0, 5, 5);
		gbc_lblCapacityElevator.gridx = 0;
		gbc_lblCapacityElevator.gridy = 1;
		panelMenu.add(lblCapacityElevator, gbc_lblCapacityElevator);
		
		JLabel lblNumberOfFloors = new JLabel("Number of floors:");
		lblNumberOfFloors.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_lblNumberOfFloors = new GridBagConstraints();
		gbc_lblNumberOfFloors.anchor = GridBagConstraints.WEST;
		gbc_lblNumberOfFloors.insets = new Insets(0, 0, 5, 5);
		gbc_lblNumberOfFloors.gridx = 0;
		gbc_lblNumberOfFloors.gridy = 2;
		panelMenu.add(lblNumberOfFloors, gbc_lblNumberOfFloors);
		
		spNumberOfPeople = new JSpinner();
		spNumberOfPeople.setModel(new SpinnerNumberModel(new Integer(10), new Integer(1), null, new Integer(1)));
		GridBagConstraints gbc_spNumberOfPeople = new GridBagConstraints();
		gbc_spNumberOfPeople.insets = new Insets(0, 0, 5, 0);
		gbc_spNumberOfPeople.gridx = 1;
		gbc_spNumberOfPeople.gridy = 0;
		gbc_spNumberOfPeople.fill = GridBagConstraints.HORIZONTAL;
		panelMenu.add(spNumberOfPeople, gbc_spNumberOfPeople);
		
		spCapacityElevator = new JSpinner();
		spCapacityElevator.setModel(new SpinnerNumberModel(new Integer(4), new Integer(1), null, new Integer(1)));
		GridBagConstraints gbc_spCapacityElevator = new GridBagConstraints();
		gbc_spCapacityElevator.insets = new Insets(0, 0, 5, 0);
		gbc_spCapacityElevator.gridx = 1;
		gbc_spCapacityElevator.gridy = 1;
		gbc_spCapacityElevator.fill = GridBagConstraints.HORIZONTAL;
		panelMenu.add(spCapacityElevator, gbc_spCapacityElevator);
		
		spNumberOfFloors = new JSpinner();
		spNumberOfFloors.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				panelAnimation.setNumberOfFloors((Integer)spNumberOfFloors.getValue());
			}
		});
		spNumberOfFloors.setModel(new SpinnerNumberModel(new Integer(3), new Integer(2), null, new Integer(1)));
		GridBagConstraints gbc_spNumberOfFloors = new GridBagConstraints();
		gbc_spNumberOfFloors.insets = new Insets(0, 0, 5, 0);
		gbc_spNumberOfFloors.gridx = 1;
		gbc_spNumberOfFloors.gridy = 2;
		gbc_spNumberOfFloors.fill = GridBagConstraints.HORIZONTAL;
		panelMenu.add(spNumberOfFloors, gbc_spNumberOfFloors);
		
		btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int numFloors = (Integer)spNumberOfFloors.getValue();
				threaAnimation = new Thread(panelAnimation);
				threaAnimation.start();
				build = new BuildTask(numFloors, (Integer)spNumberOfPeople.getValue(), (Integer)spCapacityElevator.getValue());
				panelAnimation.setNumberOfFloors(numFloors);
				panelAnimation.setBuildTask(build);
				Parametrs.isAnimation = true;
				//btnStart.setEnabled(false);
			}
		});
		GridBagConstraints gbc_btnStart = new GridBagConstraints();
		gbc_btnStart.insets = new Insets(0, 0, 5, 5);
		gbc_btnStart.gridx = 0;
		gbc_btnStart.gridy = 4;
		panelMenu.add(btnStart, gbc_btnStart);
		
		JButton btnStop = new JButton("Stop");
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				build.stopAllThreads();
				Parametrs.isAnimation = false;
			}
		});
		GridBagConstraints gbc_btnStop = new GridBagConstraints();
		gbc_btnStop.insets = new Insets(0, 0, 5, 0);
		gbc_btnStop.gridx = 1;
		gbc_btnStop.gridy = 4;
		panelMenu.add(btnStop, gbc_btnStop);
		
		slider = new JSlider();
		slider.setValue(20);
		Parametrs.animationSpeed = slider.getValue();
		slider.setMinimum(1);
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				Parametrs.animationSpeed = slider.getValue();
			}
		});
		
		lblAnimationSpeed = new JLabel("Animation speed");
		GridBagConstraints gbc_lblAnimationSpeed = new GridBagConstraints();
		gbc_lblAnimationSpeed.insets = new Insets(0, 0, 5, 0);
		gbc_lblAnimationSpeed.gridx = 0;
		gbc_lblAnimationSpeed.gridy = 6;
		gbc_lblAnimationSpeed.gridwidth = 2;
		panelMenu.add(lblAnimationSpeed, gbc_lblAnimationSpeed);
		GridBagConstraints gbc_slider = new GridBagConstraints();
		gbc_slider.insets = new Insets(0, 0, 5, 0);
		gbc_slider.gridx = 0;
		gbc_slider.gridy = 7;
		gbc_slider.gridwidth = 2;
		panelMenu.add(slider, gbc_slider);
		
		lblFaster = new JLabel("Faster");
		GridBagConstraints gbc_lblFaster = new GridBagConstraints();
		gbc_lblFaster.anchor = GridBagConstraints.WEST;
		gbc_lblFaster.insets = new Insets(0, 0, 0, 5);
		gbc_lblFaster.gridx = 0;
		gbc_lblFaster.gridy = 8;
		panelMenu.add(lblFaster, gbc_lblFaster);
		
		lblSlower = new JLabel("Slower");
		GridBagConstraints gbc_lblSlower = new GridBagConstraints();
		gbc_lblSlower.anchor = GridBagConstraints.EAST;
		gbc_lblSlower.gridx = 1;
		gbc_lblSlower.gridy = 8;
		panelMenu.add(lblSlower, gbc_lblSlower);
	}
}
