package by.epamlab.frames;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import by.epamlab.beans.BuildTask;
import by.epamlab.beans.Elevator;
import by.epamlab.beans.Parametrs;
import by.epamlab.controllers.PassengerController;
import by.epamlab.enums.StatePassenger;

public class AnimationPanel extends JPanel implements Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private BuildTask buildTask;
	Elevator elevator;

	private int numberOfFloors = 3;
	private final int numPartsWidth = 5;
	private int panelWidth = 1;
	private int panelHeight = 1;
	private int stepWidth;
	private int stepHeight;
	
	private double kX = 1;
	private double kY = 1;

	/**
	 * Create the panel.
	 */
	public AnimationPanel() {
	}

	public void setNumberOfFloors(int numberOfFloors) {
		this.numberOfFloors = numberOfFloors;
	}

	public void setBuildTask(BuildTask buildTask) {
		this.buildTask = buildTask;
		elevator = buildTask.getElevatorController().getElevator();

		for (PassengerController passCtrl : buildTask.getAllPassengers()) {
			passCtrl.getPassenger().setX(0);
			passCtrl.getPassenger().setY(passCtrl.getPassenger().getInitialFloor() * (getHeight() / numberOfFloors));
			passCtrl.getPassenger().setStepX(stepWidth * ((numPartsWidth - 1) / 2) - stepHeight / 4);
		}

		Parametrs.isAnimation = true;
	}

	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);
		
		kX = (double)getWidth() / (double)panelWidth;
		kY = (double)getHeight() / (double)panelHeight;
		
		panelWidth = getWidth();
		panelHeight = getHeight();
		stepWidth = panelWidth / numPartsWidth;
		stepHeight = panelHeight / numberOfFloors;
		Graphics2D graphics2D = (Graphics2D) g;
		drawHouse(graphics2D);
		if (Parametrs.isAnimation) {
			elevator.setX(stepWidth * ((numPartsWidth - 1) / 2));
			elevator.setY((int)(elevator.getY() * kY));
			elevator.setStepY(stepHeight);
			drawElevator(graphics2D, elevator.getX(), panelHeight - elevator.getY());

			for (PassengerController passCtrl : buildTask.getAllPassengers()) {
				boolean isPaint = true;

				switch (passCtrl.getPassenger().getStatePassenger()) {
				case GOES:
					passCtrl.getPassenger().setX((int)(passCtrl.getPassenger().getX() * kX));
					passCtrl.getPassenger().setStepX(stepWidth * ((numPartsWidth - 1) / 2) - stepHeight / 4);
					passCtrl.getPassenger().setY(passCtrl.getPassenger().getInitialFloor() * (getHeight() / numberOfFloors));
					break;
				case WAITING:
					passCtrl.getPassenger().setX((int)(passCtrl.getPassenger().getX() * kX));
					passCtrl.getPassenger().setStepX(stepWidth * ((numPartsWidth - 1) / 2) - stepHeight / 4);
					passCtrl.getPassenger().setY(passCtrl.getPassenger().getInitialFloor() * (getHeight() / numberOfFloors));
					break;
				case RIDING:
					passCtrl.getPassenger().setX(elevator.getX() + stepWidth / 2);
					passCtrl.getPassenger().setY(elevator.getY());
					break;
				case OUT:
					passCtrl.getPassenger().setX(stepWidth * (1 + (numPartsWidth - 1) / 2) + stepHeight / 4);
					passCtrl.getPassenger().setStepX(panelWidth);
					passCtrl.getPassenger().setY(passCtrl.getPassenger().getDestinationFloor() * (getHeight() / numberOfFloors));
					passCtrl.getPassenger().setStatePassenger(StatePassenger.LEAVES);
					break;
				case LEAVES:
					passCtrl.getPassenger().setX((int)(passCtrl.getPassenger().getX() * kX));
					passCtrl.getPassenger().setStepX(panelWidth);
					passCtrl.getPassenger().setY(passCtrl.getPassenger().getDestinationFloor() * (getHeight() / numberOfFloors));
					break;
				case REMOVED:
					isPaint = false;
					break;
				default:
					break;
				}

				if (isPaint) {
					drawPassenger(graphics2D, passCtrl.getPassenger().getX(),
							panelHeight - passCtrl.getPassenger().getY(),
							passCtrl.getPassenger().getDestinationFloor());
				}
			}
		}
	}

	public void drawHouse(Graphics2D graph2D) {
		graph2D.setPaint(Color.BLACK);
		graph2D.setStroke(new BasicStroke(3));
		for (int i = 1; i <= numberOfFloors; i++) {
			graph2D.drawLine(0, i * stepHeight, stepWidth
					* ((numPartsWidth - 1) / 2) - 4, i * stepHeight);
			graph2D.drawLine(stepWidth * (1 + (numPartsWidth - 1) / 2) + 3, i
					* stepHeight, stepWidth * (numPartsWidth), i * stepHeight);
		}
	}

	public void drawElevator(Graphics2D graph2D, int x, int y) {
		graph2D.setPaint(Color.BLUE);
		graph2D.setStroke(new BasicStroke(2));
		graph2D.drawLine(x, y, x + stepWidth, y);
		graph2D.drawLine(x + stepWidth, y, x + stepWidth, y - stepHeight);
		graph2D.drawLine(x + stepWidth, y - stepHeight, x, y - stepHeight);
		graph2D.drawLine(x, y - stepHeight, x, y);
	}

	public void drawPassenger(Graphics2D graph2D, int x, int y,
			int destinationFloor) {
		destinationFloor++;
		graph2D.setPaint(Color.GREEN);
		graph2D.setStroke(new BasicStroke(1));
		int heightMan = 2 * stepHeight / 3;
		graph2D.drawLine(x, y - heightMan / 3, x, y - 2 * heightMan / 3); // body
		graph2D.drawLine(x - heightMan / 4, y, x, y - heightMan / 3); // legs
		graph2D.drawLine(x + heightMan / 4, y, x, y - heightMan / 3);
		graph2D.drawLine(x - heightMan / 4, y - heightMan / 3, x, y - 4
				* heightMan / 7); // hands
		graph2D.drawLine(x + heightMan / 4, y - heightMan / 3, x, y - 4
				* heightMan / 7);
		graph2D.drawOval(x - heightMan / 6, y - heightMan, heightMan / 3,
				heightMan / 3); // head
		graph2D.setPaint(Color.RED);
		graph2D.drawString(Integer.toString(destinationFloor), x - heightMan
				/ 20, y - heightMan + heightMan / 4);
	}

	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			super.repaint();
		}
	}
}
