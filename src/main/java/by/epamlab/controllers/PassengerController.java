package by.epamlab.controllers;

import by.epamlab.beans.BuildTask;
import by.epamlab.beans.Elevator;
import by.epamlab.beans.Passenger;
import by.epamlab.enums.StatePassenger;
import by.epamlab.logs.LoggerManager;

public class PassengerController implements Runnable {
	Passenger passenger;
	BuildTask buildTask;

	public PassengerController(int initialFloor, int destinationFloor,
			int passengerID, BuildTask buildTask) {
		super();
		this.buildTask = buildTask;
		passenger = new Passenger(initialFloor, destinationFloor, passengerID);
	}

	public Passenger getPassenger() {
		return passenger;
	}

	public void run() {
		// TODO Auto-generated method stub
		Elevator elevator = buildTask.getElevatorController().getElevator();

		passenger.setStatePassenger(StatePassenger.GOES);
		passenger.movePassenger();
		passenger.setStatePassenger(StatePassenger.WAITING);

		try {
			// I'm waiting for elevator
			synchronized (this) {
				this.wait();
			}

			// I'm waiting for the entrance of the elevator, or again waiting
			// elevator
			boolean waitElevator = true;
			while (waitElevator) {
				boolean isEnterIntoElevator = false;
				synchronized (elevator) {
					isEnterIntoElevator = elevator.addToPassengers(this);
					if (isEnterIntoElevator) {
						buildTask.getFloorsList()
								.get(passenger.getInitialFloor())
								.removeFromDispatch(this);
						LoggerManager.LOG.info("+ "
								+ passenger.printPassenger() + " entered on: "
								+ elevator.getCurrentFloor());
					}
				}
				if (isEnterIntoElevator) {
					waitElevator = false;
					passenger.setStatePassenger(StatePassenger.RIDING);
				} else {
					synchronized (this) {
						this.wait();
					}
				}
			}

			// I am waiting for your floor
			synchronized (this) {
				this.wait();
			}

			// I am waiting for out of the elevator, or wait for your floor
			boolean waitFloor = true;
			while (waitFloor) {
				boolean isExitElevator = false;
				synchronized (elevator) {
					isExitElevator = elevator.removeFromPassengers(this);
					if (isExitElevator) {
						buildTask.getFloorsList()
								.get(elevator.getCurrentFloor())
								.addToArrival(this);
						LoggerManager.LOG.info("- "
								+ passenger.printPassenger() + " came out on: "
								+ elevator.getCurrentFloor());
					}
				}
				if (isExitElevator) {
					waitFloor = false;
				} else {
					synchronized (this) {
						this.wait();
					}
				}
			}
			passenger.setStatePassenger(StatePassenger.OUT);
			while (passenger.getStatePassenger() != StatePassenger.LEAVES) {
				// wait
			}
			passenger.movePassenger();
			passenger.setStatePassenger(StatePassenger.REMOVED);

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LoggerManager.LOG.error(e);
		}
	}
}
