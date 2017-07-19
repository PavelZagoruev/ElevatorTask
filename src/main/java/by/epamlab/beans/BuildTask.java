package by.epamlab.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import by.epamlab.controllers.ElevatorController;
import by.epamlab.controllers.PassengerController;

public class BuildTask {
	private int floorsCount;
	private int passengerCount;
	private int elevatorCapacity;
	private List<Floor> floorsList;
	private ElevatorController elevatorController;
	private List<PassengerController> allPassengers;

	private Thread elevatorThread;
	private List<Thread> passangersThreads;

	public BuildTask(int floorsCount, int passengerCount, int elevatorCapacity) {
		super();
		this.floorsCount = floorsCount;
		this.passengerCount = passengerCount;
		this.elevatorCapacity = elevatorCapacity;

		floorsList = new ArrayList<Floor>(floorsCount);
		allPassengers = new ArrayList<PassengerController>();
		elevatorController = new ElevatorController(elevatorCapacity,
				floorsCount, this);
		elevatorThread = new Thread(elevatorController);
		passangersThreads = new ArrayList<Thread>(passengerCount);

		createFloors();
		createPassengers();
		startAllThreads();
	}

	public int getPassengerCount() {
		return passengerCount;
	}

	public List<Floor> getFloorsList() {
		return floorsList;
	}

	public ElevatorController getElevatorController() {
		return elevatorController;
	}

	public void createPassengers() {
		for (int i = 0; i < passengerCount; i++) {
			Random random = new Random();
			int dispatchFloor = random.nextInt(floorsCount);
			int arrivalFloor = random.nextInt(floorsCount);
			while (dispatchFloor == arrivalFloor) {
				dispatchFloor = random.nextInt(floorsCount);
				arrivalFloor = random.nextInt(floorsCount);
			}
			PassengerController passContr = new PassengerController(
					dispatchFloor, arrivalFloor, i, this);
			passangersThreads.add(new Thread(passContr));
			allPassengers.add(passContr);
			floorsList.get(dispatchFloor).addToDispatch(passContr);
		}
	}

	public List<PassengerController> getAllPassengers() {
		return allPassengers;
	}

	public void createFloors() {
		for (int i = 0; i < floorsCount; i++) {
			floorsList.add(new Floor(i));
		}
	}

	public void startAllThreads() {
		for (Thread passThread : passangersThreads) {
			passThread.start();
		}
		
		elevatorThread.start();
	}

	public void stopAllThreads() {
		for (Thread passThread : passangersThreads) {
			passThread.stop();
		}
		elevatorThread.stop();
	}
}
