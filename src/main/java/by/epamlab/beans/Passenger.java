package by.epamlab.beans;

import java.util.Random;

import by.epamlab.enums.StatePassenger;
import by.epamlab.logs.LoggerManager;

public class Passenger {
	private final int initialFloor;
	private final int destinationFloor;
	private final int passengerID;
	private StatePassenger statePassenger;
	private int x;
	private int y;
	private int speed;
	private int stepX;

	public Passenger(int initialFloor, int destinationFloor, int passengerID) {
		super();
		this.initialFloor = initialFloor;
		this.destinationFloor = destinationFloor;
		this.passengerID = passengerID;
		this.statePassenger = StatePassenger.CREATED;
		x = 0;
		Random random = new Random();
		speed = random.nextInt(20);
		while(speed < 5){
			speed = random.nextInt(20);
		}
	}
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void setStepX(int stepX) {
		this.stepX = stepX;
	}
	
	public void movePassenger() {
		while(x != stepX){
			x++;
			try {
				Thread.sleep(Parametrs.animationSpeed * speed);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				LoggerManager.LOG.error(e);
			}
		}
	}
	
	public synchronized StatePassenger getStatePassenger() {
		return statePassenger;
	}

	public synchronized void setStatePassenger(StatePassenger statePassenger) {
		this.statePassenger = statePassenger;
	}

	public int getInitialFloor() {
		return initialFloor;
	}

	public int getDestinationFloor() {
		return destinationFloor;
	}

	public int getPassengerID() {
		return passengerID;
	}

	public String printPassenger() {
		return "#" + passengerID + " (" + initialFloor + "->"
				+ destinationFloor + ")";
	}

	@Override
	public String toString() {
		return "[Passenger " + passengerID + " rides from " + initialFloor
				+ " to " + destinationFloor + "]";
	}
}
