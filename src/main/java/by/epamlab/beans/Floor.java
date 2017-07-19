package by.epamlab.beans;

import java.util.ArrayList;
import java.util.List;

import by.epamlab.controllers.PassengerController;
import by.epamlab.enums.StatePassenger;
import by.epamlab.logs.LoggerManager;

public class Floor {
	private final int numberOfFloor;
	private List<PassengerController> dispatchList;
	private List<PassengerController> arrivalList;

	public Floor(int numberOfFloor) {
		super();
		this.numberOfFloor = numberOfFloor;
		dispatchList = new ArrayList<PassengerController>();
		arrivalList = new ArrayList<PassengerController>();
	}

	public int getNumberOfFloor() {
		return numberOfFloor;
	}

	public List<PassengerController> getArrivalList() {
		return arrivalList;
	}

	public void addToDispatch(PassengerController pas) {
		LoggerManager.LOG.info(pas.getPassenger());
		dispatchList.add(pas);
	}

	public synchronized void removeFromDispatch(PassengerController pas) {
		dispatchList.remove(pas);
	}

	public synchronized boolean isEmptyFloor() {
		return dispatchList.isEmpty();
	}
	
	public synchronized boolean hasPassangersToEnter() {
		if(!isEmptyFloor()){
			for (PassengerController passCtrl : dispatchList) {
				if(passCtrl.getPassenger().getStatePassenger() == StatePassenger.WAITING){
					return true;
				}
			}
		}
		return false;
	}
	
	public List<PassengerController> getDispatchList() {
		return dispatchList;
	}

	public void addToArrival(PassengerController pas) {
		arrivalList.add(pas);
	}

	public synchronized void notifyPassengers() {
		for (PassengerController pass : dispatchList) {
			synchronized (pass) {
				pass.notify();
			}
		}
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuilder str = new StringBuilder();
		for (PassengerController pass : dispatchList) {
			str.append(pass.getPassenger() + " ");
		}
		if (dispatchList.size() == 0) {
			str.append("none");
		}
		return str.toString();
	}
}
