package by.epamlab.beans;

import java.util.ArrayList;
import java.util.List;

import by.epamlab.controllers.PassengerController;
import by.epamlab.enums.StatePassenger;
import by.epamlab.logs.LoggerManager;

public class Elevator {
	// private static final double SPEED = 0.1;

	private final int elevatorCapacity;
	private List<PassengerController> passengersList;
	private final int numberOfFloors;
	private int currentFloor = 0;
	private int direction = 1;

	private int x;
	private int y;
	private int stepY;

	public Elevator(int elevatorCapacity, int numberOfFloors) {
		super();
		this.elevatorCapacity = elevatorCapacity;
		this.numberOfFloors = numberOfFloors;
		passengersList = new ArrayList<PassengerController>();
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

	public void setStepY(int stepY) {
		this.stepY = stepY;
	}
	
	public int getStepY() {
		return stepY;
	}

	public synchronized int getCurrentFloor() {
		return currentFloor;
	}

	public int getElevatorCapacity() {
		return elevatorCapacity;
	}

	public int getNumberOfFloors() {
		return numberOfFloors;
	}

	public List<PassengerController> getPassengersList() {
		return passengersList;
	}

	public synchronized boolean addToPassengers(PassengerController passCtrl) {
		boolean isAdd = false;
		if (!isFullElevator()
				&& passCtrl.getPassenger().getStatePassenger() == StatePassenger.WAITING) {
			isAdd = passengersList.add(passCtrl);
		}
		return isAdd;
	}

	public synchronized boolean removeFromPassengers(
			PassengerController passCtrl) {
		boolean isRemove = false;
		if (passCtrl.getPassenger().getDestinationFloor() == getCurrentFloor()
				&& !isEmptyElevator() && passCtrl.getPassenger().getStatePassenger() == StatePassenger.RIDING) {
			isRemove = passengersList.remove(passCtrl);
		}
		return isRemove;
	}

	public boolean isEmptyElevator() {
		return passengersList.isEmpty();
	}

	public boolean isFullElevator() {
		return getLoadElevator() == getElevatorCapacity();
	}

	public synchronized boolean hasPassengerToExit() {
		for (PassengerController passCtrl : passengersList) {
			if (passCtrl.getPassenger().getDestinationFloor() == currentFloor
					&& passCtrl.getPassenger().getStatePassenger() == StatePassenger.RIDING) {
				return true;
			}
		}
		return false;
	}

	public synchronized int getLoadElevator() {
		return passengersList.size();
	}

	public void printPassengers() {
		for (PassengerController passCtrl : passengersList) {
			System.out.println("in: " + passCtrl.getPassenger());
		}
	}

	public void moveElevator() {
		while(y != (currentFloor + direction) * stepY){
			y += direction;
			try {
				Thread.sleep(Parametrs.animationSpeed);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				LoggerManager.LOG.error(e);
			}
		}
	}
	
	public void toNextFloor() {
		moveElevator();
		currentFloor += direction;
		if (currentFloor == 0 || currentFloor == numberOfFloors - 1) {
			direction = (direction == 1) ? -1 : 1;
		}
	}

	public synchronized void notifyPassengers() {
		for (PassengerController passCtrl : passengersList) {
			synchronized (passCtrl) {
				passCtrl.notify();
			}
		}
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "***Elevator: capacity = " + passengersList.size()
				+ "; floor = " + currentFloor;
	}
}
