package by.epamlab.controllers;

import by.epamlab.beans.BuildTask;
import by.epamlab.beans.Elevator;
import by.epamlab.beans.Floor;
import by.epamlab.beans.Parametrs;
import by.epamlab.logs.LoggerManager;

public class ElevatorController implements Runnable {
	private Elevator elevator;
	private BuildTask buildTask;
	
	public ElevatorController(int elevatorCapacity, int numberOfFloors,
			BuildTask buildTask) {
		super();
		elevator = new Elevator(elevatorCapacity, numberOfFloors);
		this.buildTask = buildTask;
	}

	public Elevator getElevator() {
		return elevator;
	}
	
	public boolean isEmptyFloors(){
		for (Floor floor : buildTask.getFloorsList()){
				if (!floor.isEmptyFloor()){
					return false;
				}
		}
		return true;
	}
	
	public void run() {
		// TODO Auto-generated method stub
		while (!isEmptyFloors() || elevator.getLoadElevator() > 0) {
			LoggerManager.LOG.info(elevator);
			
			Floor floor;
			synchronized (this) {
				int floorIndex = elevator.getCurrentFloor();
				floor = buildTask.getFloorsList().get(floorIndex);
			}
			
			//notify the passengers in the elevator
			elevator.notifyPassengers();
			boolean isExitElevator = true;
			while(isExitElevator){
				if(!elevator.hasPassengerToExit()){
					isExitElevator = false;
				}
			}
			
			//notify passengers on this floor
			floor.notifyPassengers();
			boolean isEnterElevator = true;
			while(isEnterElevator){
				if(elevator.isFullElevator() || !floor.hasPassangersToEnter()){
					isEnterElevator = false;
				}
			}
			
			synchronized (this) {
				elevator.toNextFloor();
			}
		}
		
		Parametrs.isAnimation = false;
		LoggerManager.LOG.info(elevator);
		LoggerManager.LOG.info("Elevator: stoped");
		buildTask.stopAllThreads();
	}
}
