package InputOutput;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import Actors.Person;
import ElevatorParts.Bell;
import ElevatorParts.Clock;
import ElevatorParts.Floor;
import GUI.MainWindow;
import InternalLogic.Scheduler;

public class Elevator {

	private Floor fl1, fl2;
	private static int currentTime;
	private float averageWaitingTime;
	private int totalDrivingTime;
	private boolean hasPassenger;
	private boolean upstairs;
	private boolean busy, busyWithoutPassenger;
	private static Person passenger;
	private LinkedList<Person> passengerHistory;
	private LinkedList<Person> passengersDownstairs, passengersUpstairs;
	private Scheduler schedule;
	private static Random randomGenerator;
	private Bell bell;

	private enum modes {
		manual, automatic, none
	};

	private modes currentMode;

	public modes getCurrentMode() {
		return currentMode;
	}

	public void setCurrentMode(modes currentMode) {
		this.currentMode = currentMode;
	}

	public Random getRandomGenerator() {
		return randomGenerator;
	}

	public int getTotalDrivingTime() {
		return totalDrivingTime;
	}

	public float getAverageWaitingTime() {
		return (float) (((int) (averageWaitingTime * 100)) / 100.0);
	}

	public Bell getBell() {
		return bell;
	}

	public void setBell(Bell bell) {
		this.bell = bell;
	}

	public Scheduler getSchedule() {
		return schedule;
	}

	public void setSchedule(Scheduler schedule) {
		this.schedule = schedule;
	}

	public Elevator() {
		this.fl1 = new Floor(0);
		this.fl2 = new Floor(1);
		setBell(new Bell("res/Airplane-ding-sound.wav"));
		busy = false;
		busyWithoutPassenger = false;
		randomGenerator = new Random();
		setCurrentMode(modes.none);
		upstairs = false;
		passenger = null;
		averageWaitingTime = 0;
		totalDrivingTime = 0;
		passengerHistory = new LinkedList<Person>();
		passengersDownstairs = new LinkedList<Person>();
		passengersUpstairs = new LinkedList<Person>();
		System.out.println("Elevator is running!");
		setSchedule(new Scheduler());
		hasPassenger = false;

	}

	public static int getCurrentTime() {
		return currentTime;
	}

	public Floor getFloor() {
		if (upstairs) {
			return fl2;
		} else {
			return fl1;
		}
	}

	public Floor getOtherFloor() {
		Floor temp = getFloor();
		if (temp == fl1) {
			return fl2;
		} else {
			return fl1;
		}
	}

	public void setFloor(Floor activeFloor) {
		int lvl = activeFloor.getLevel();
		if (lvl == 1) {
			upstairs = true;
		} else {
			upstairs = false;
		}
	}

	public boolean isHasPassenger() {
		return hasPassenger;
	}

	public void setHasPassenger(boolean hasPassenger) {
		this.hasPassenger = hasPassenger;
	}

	public static Person getPassenger() {
		return passenger;
	}

	public static void setPassenger(Person passenger) {
		Elevator.passenger = passenger;
	}

	public String printPassengerHistory() {
		String output = "";
		if (this.passengerHistory.size() == 0) {
			output = "Passenger History: NO ONE did travel yet!";
		} else {
			output = "Passenger History: [";
			for (Person p : passengerHistory) {
				output += p.getName() + ", ";
			}
			output = output.substring(0, output.length() - 2);
			output += "]";
			output += " => Amount: " + passengerHistory.size();

		}
		return output;
	}

	public String printPassengersDownstairs() {
		String output = "";
		if (this.passengersDownstairs.size() == 0) {
			output = "Passengers downstairs: NO ONE downstairs yet!";
		} else {
			output = "Passenger downstairs: [";
			for (Person p : passengersDownstairs) {
				output += p.getName() + ", ";
			}
			output = output.substring(0, output.length() - 2);
			output += "]";
			output += " => Amount: " + passengersDownstairs.size();
		}
		return output;
	}

	public String printPassengersUpstairs() {
		String output = "";
		if (this.passengersUpstairs.size() == 0) {
			output = "Passengers upstairs: NO ONE upstairs yet!";
		} else {
			output = "Passengers upstairs: [";
			for (Person p : passengersUpstairs) {
				output += p.getName() + ", ";
			}
			output = output.substring(0, output.length() - 2);
			output += "]";
			output += " => Amount: " + passengersUpstairs.size();
		}
		return output;
	}

	public void doPromt() {
		System.out.println("==============================================");
		System.out
				.println("Elevator is ready for the next command, 'drive', 'state' or 'exit':");
	}

	public void writeLogFile(String logData) throws IOException {
		File logOutput = new File("log.txt");
		FileWriter fw = new FileWriter(logOutput,true);
		fw.write("==============================================" + "\n");
		fw.write("Timestamp: " + new Date().toString() + "\n");
		fw.write(logData);
		fw.write("==============================================" + "\n");
		fw.close();
	}

	public String printState() throws IOException {
		String output = "";
		output += "Current Time: " + getCurrentTime() + " seconds" + "\n";
		output += "Current Floor: " + getFloor().getLevel() + "\n";
		if (getPassenger() == null) {
			output += "Current Passenger: " + "NO ONE!" + "\n";
		} else {
			output += "Current Passenger: " + getPassenger().getName() + "\n";
		}
		String pasHis = printPassengerHistory();
		output += pasHis + "\n";
		String pasDown = printPassengersDownstairs();
		output += pasDown + "\n";
		String pasUp = printPassengersUpstairs();
		output += pasUp + "\n";
		output += "Average wait time: " + getAverageWaitingTime() + " seconds"
				+ "\n";
		output += "Total driving time: " + getTotalDrivingTime() + " seconds"
				+ "\n";
		output += "Total idle time: "
				+ (getCurrentTime() - getTotalDrivingTime()) + " seconds"
				+ "\n";
		output += "Random number T1: " + schedule.getT1() + "\n";
		output += "Random number T2: " + schedule.getT2() + "\n";
		// doPromt();
		if (getCurrentTime() % 5 == 0) {
			writeLogFile(output);
		}
		return output;
	}

	public void changeFloor() {
		String name = "";
		if (this.getFloor().getLevel() == 0) {
			name = "Person 0";
		} else {
			name = "Person 1";
		}
		changeFloorManual(name);
	}

	public void driveWithoutPassenger() {
		busyWithoutPassenger = true;
		System.out
				.println("Since the elevator is on the other floor, it has now to drive without a passenger!");

		if (getFloor().getDoor().isOpen()) {
			getFloor().toggleDoor();
		}

		getOtherFloor().toggleLight();

		Timer waiting = new Timer("waiting_timer", false);
		waiting.schedule(new TimerTask() {

			@Override
			public void run() {
				setFloor(getOtherFloor());
				System.out.println("The elevator reached the floor #"
						+ getFloor().getLevel());
				getFloor().toggleLight();
				getBell().ring();
				if (getFloor().getDoor().isOpen() == false) {
					getFloor().toggleDoor();
				}
				busyWithoutPassenger = false;
			}
		}, 3 * 1000);
	}

	public void drive(int waitTime) {
		Timer waiting = new Timer("waiting_timer", false);
		waiting.schedule(new TimerTask() {

			@Override
			public void run() {
				System.out.println("The elevator reached the floor #"
						+ getFloor().getLevel());
				MainWindow.getTravelListModel().addElement("The elevator reached the floor #"
						+ getFloor().getLevel());
				getFloor().toggleLight();
				getBell().ring();
				if (getFloor().getDoor().isOpen() == false) {
					getFloor().toggleDoor();
				}
				System.out.println(getPassenger().getName()
						+ " left the elevator!");
				MainWindow.getTravelListModel().addElement(getPassenger().getName()
						+ " left the elevator!");
				passengerHistory.add(passenger);
				if (getFloor().getLevel() == 0) {
					passengersDownstairs.add(passenger);
				} else {
					passengersUpstairs.add(passenger);
				}
				if (averageWaitingTime != 0) {
					averageWaitingTime /= passengerHistory.size();
				}
				Elevator.setPassenger(null);
				MainWindow.getMoveButton().setText("Move");
				MainWindow.getMoveButton().setEnabled(true);
				MainWindow.getTravelListModel().addElement("===========================");
				if (getCurrentMode() == modes.manual) {
					doPromt();
				}
			}
		}, waitTime * 100);
		busy = false;
	}

	public void changeFloorManual(String passengerName) {
		int waitTime = -1;

		if (this.getFloor().getLevel() == 0) {
			Elevator.passenger = new Person(passengerName, 0);
			System.out.println(passenger.getName()
					+ " has just pressed the button on floor "
					+ passenger.getFloor());
			MainWindow.getTravelListModel().addElement(passenger.getName()
					+ " has just pressed the button on floor "
					+ passenger.getFloor());
			if (getFloor().getDoor().isOpen() == false) {
				getFloor().toggleDoor();
			}
			System.out.println(passenger.getName() + " entered the elevator!");
			MainWindow.getTravelListModel().addElement(passenger.getName() + " entered the elevator!");
			getFloor().toggleDoor();
			getOtherFloor().toggleLight();
			waitTime = this.schedule.generateNextRandom(0);
			if (averageWaitingTime == 0) {
				averageWaitingTime = (int) (waitTime / 10);
			} else {
				averageWaitingTime += (int) (waitTime / 10);
			}
			totalDrivingTime += (int) (waitTime / 10);
			System.out
					.println("Elevator is now moving to floor 1 with passenger "
							+ Elevator.passenger.getName());
			MainWindow.getTravelListModel().addElement("Elevator is now moving to floor 1 with passenger "
					+ Elevator.passenger.getName());
			System.out.print("Moving...");
			MainWindow.getTravelListModel().addElement("Moving...");
			upstairs = true;
		} else {
			Elevator.passenger = new Person(passengerName, 1);
			System.out.println(passenger.getName()
					+ " has just pressed the button on floor "
					+ passenger.getFloor());
			MainWindow.getTravelListModel().addElement(passenger.getName()
					+ " has just pressed the button on floor "
					+ passenger.getFloor());
			if (getFloor().getDoor().isOpen() == false) {
				getFloor().toggleDoor();
			}
			System.out.println(passenger.getName() + " entered the elevator!");
			MainWindow.getTravelListModel().addElement(passenger.getName() + " entered the elevator!");
			getFloor().toggleDoor();
			getOtherFloor().toggleLight();
			waitTime = this.schedule.generateNextRandom(1);
			if (averageWaitingTime == 0) {
				averageWaitingTime = waitTime / 10;
			} else {
				averageWaitingTime += waitTime / 10;
			}
			totalDrivingTime += (int) (waitTime / 10);
			System.out
					.println("Elevator is now moving to floor 0 with passenger "
							+ Elevator.passenger.getName());
			MainWindow.getTravelListModel().addElement("Elevator is now moving to floor 0 with passenger "
					+ Elevator.passenger.getName());
			System.out.print("Moving...");
			MainWindow.getTravelListModel().addElement("Moving...");
			upstairs = false;
		}
		drive(waitTime);

	}

	public void changeFloorAutomatic() {
		busy = true;
		int waitTime = -1;

		if (this.getFloor().getLevel() == 0) {
			if (getFloor().getDoor().isOpen() == false) {
				getFloor().toggleDoor();
			}
			System.out.println(passenger.getName() + " entered the elevator!");
			getFloor().toggleDoor();
			getOtherFloor().toggleLight();
			waitTime = this.schedule.generateNextRandom(0);
			if (averageWaitingTime == 0) {
				averageWaitingTime = (int) (waitTime / 10);
			} else {
				averageWaitingTime += (int) (waitTime / 10);
			}
			totalDrivingTime += (int) (waitTime / 10);
			System.out
					.println("Elevator is now moving to floor 1 with passenger "
							+ Elevator.passenger.getName());
			System.out.print("Moving...");
			upstairs = true;
		} else {
			if (getFloor().getDoor().isOpen() == false) {
				getFloor().toggleDoor();
			}
			System.out.println(passenger.getName() + " entered the elevator!");
			getFloor().toggleDoor();
			getOtherFloor().toggleLight();
			waitTime = this.schedule.generateNextRandom(1);
			if (averageWaitingTime == 0) {
				averageWaitingTime = waitTime / 10;
			} else {
				averageWaitingTime += waitTime / 10;
			}
			totalDrivingTime += (int) (waitTime / 10);
			System.out
					.println("Elevator is now moving to floor 0 with passenger "
							+ Elevator.passenger.getName());
			System.out.print("Moving...");
			upstairs = false;
		}
		drive(waitTime);
	}

	public void provideTime() {
		final Clock c1 = new Clock();
		Timer t1 = new Timer("each_second_trigger", false);
		t1.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				currentTime = c1.calculateTime();

			}
		}, 0, 1000);
	}

	public void manualMode() throws IOException {

		System.out.println("Entering manual mode...");
		setCurrentMode(modes.manual);
		Scanner sc = new Scanner(System.in);
		System.out
				.println("Enter 'drive' to move the elevator, 'state' for the current elevator state and 'exit' to quit the program");
		while (true) {

			String input = sc.next();
			switch (input) {

			case "drive": {
				System.out
						.println("Enter the passenger name or enter a 'd' to use default names:");
				String name = sc.next();
				if (name.trim().equals("d")) {
					changeFloor();
				} else {
					changeFloorManual(name);
				}
				break;
			}
			case "state": {
				printState();
				break;
			}

			case "exit": {
				System.exit(0);
				sc.close();
				break;
			}
			}
		}
	}

	public void automaticMode(int runtime) throws InterruptedException {
		if (runtime == 0) {
			runtime = 99999;
		}
		System.out.println("Entering automatic mode for " + runtime
				+ " seconds...");
		setCurrentMode(modes.automatic);

		int personID = 1;
		while (true && getCurrentTime() < runtime) {
			if (!busy && !busyWithoutPassenger) {
				int floor = getRandomGenerator().nextInt(2);
				Elevator.passenger = new Person("Person "
						+ String.valueOf(personID), floor);

				System.out.println("=======================================");
				System.out.println(Elevator.passenger.getName()
						+ " has just pressed the button on floor "
						+ Elevator.passenger.getFloor());

				if (this.getFloor().getLevel() != Elevator.getPassenger()
						.getFloor()) {
					this.driveWithoutPassenger();
				}
				while (busyWithoutPassenger) {
					Thread.sleep(100);
				}
				this.changeFloorAutomatic();
				personID++;
			}
			// int waitForNextPerson = this.schedule.generateNextRandom(floor);
			// try {
			// Thread.sleep(1000 * waitForNextPerson);
			// } catch (InterruptedException e) {
			// e.printStackTrace();
			// }

		}
		System.out.println("In " + runtime + " seconds there did travel "
				+ personID + " passengers!");
		System.exit(0);
	}

}
