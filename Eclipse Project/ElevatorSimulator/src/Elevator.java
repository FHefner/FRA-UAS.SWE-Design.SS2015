import java.util.LinkedList;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class Elevator {

	private Floor fl1, fl2;
	private static int currentTime;
	private float averageWaitingTime;
	private int totalDrivingTime;
	private boolean hasPassenger;
	private boolean upstairs;
	private static Person passenger;
	private LinkedList<Person> passengerHistory;
	private LinkedList<Person> passengersDownstairs, passengersUpstairs;
	private Scheduler schedule;
	private Bell bell;
	
	public int getTotalDrivingTime() {
		return totalDrivingTime;
	}

	public float getAverageWaitingTime() {
		return (float) (((int)(averageWaitingTime * 100)) / 100.0);
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

	public void printPassengerHistory() {
		if (this.passengerHistory.size() == 0) {
			System.out.println("Passenger History: NO ONE did travel yet!");
		} else {
			String output = "Passenger History: [";
			for (Person p : passengerHistory) {
				output += p.getName() + ", ";
			}
			output = output.substring(0, output.length() - 2);
			output += "]";
			output += " => Amount: " + passengerHistory.size();
			System.out.println(output);
		}
	}
	
	public void printPassengersDownstairs() {
		if (this.passengersDownstairs.size() == 0) {
			System.out.println("Passengers downstairs: NO ONE downstairs yet!");
		} else {
			String output = "Passenger downstairs: [";
			for (Person p : passengersDownstairs) {
				output += p.getName() + ", ";
			}
			output = output.substring(0, output.length() - 2);
			output += "]";
			output += " => Amount: " + passengersDownstairs.size();
			System.out.println(output);
		}
	}
	
	public void printPassengersUpstairs() {
		if (this.passengersUpstairs.size() == 0) {
			System.out.println("Passengers upstairs: NO ONE upstairs yet!");
		} else {
			String output = "Passengers upstairs: [";
			for (Person p : passengersUpstairs) {
				output += p.getName() + ", ";
			}
			output = output.substring(0, output.length() - 2);
			output += "]";
			output += " => Amount: " + passengersUpstairs.size();
			System.out.println(output);
		}
	}

	public void doPromt() {
		System.out.println("==============================================");
		System.out
				.println("Elevator is ready for the next command, 'drive', 'state' or 'exit':");
	}

	public void printState() {
		System.out.println("==============================================");
		System.out.println("Current Time: " + getCurrentTime() + " seconds");
		System.out.println("Current Floor: " + getFloor().getLevel());
		if (getPassenger() == null) {
			System.out.println("Current Passenger: " + "NO ONE!");
		} else {
			System.out
					.println("Current Passenger: " + getPassenger().getName());
		}
		printPassengerHistory();
		printPassengersDownstairs();
		printPassengersUpstairs();
		System.out.println("Average wait time: " + getAverageWaitingTime() + " seconds");
		System.out.println("Total driving time: " + getTotalDrivingTime() + " seconds");
		System.out.println("Total idle time: " + (getCurrentTime() - getTotalDrivingTime()) + " seconds");
		System.out.println("Random number T1: " + schedule.getT1());
		System.out.println("Random number T2: " + schedule.getT2());
		System.out.println("==============================================");
		doPromt();
	}

	public void changeFloor() {
		String name = "";
		if (this.getFloor().getLevel() == 0) {
			name = "Person 0";
		} else {
			name = "Person 1";
		}
		changeFloor(name);
	}

	public void changeFloor(String passengerName) {
		int waitTime = -1;

		if (this.getFloor().getLevel() == 0) {
			Elevator.passenger = new Person(passengerName, 0);
			System.out.println(passenger.getName()
					+ " has just pressed the button on floor "
					+ passenger.getFloor());
			if (getFloor().getDoor().isOpen() == false) {
				getFloor().toggleDoor();
			}
			System.out.println(passenger.getName() + " entered the elevator!");
			getFloor().toggleDoor();
			getOtherFloor().toggleLight();
			waitTime = this.schedule.generateNextRandom(0);
			if (averageWaitingTime == 0) {
				averageWaitingTime = (int) (waitTime / 10);
			}
			else {
				averageWaitingTime += (int) (waitTime / 10);
			}
			totalDrivingTime += (int) (waitTime / 10);
			System.out
					.println("Elevator is now moving to floor 1 with passenger "
							+ Elevator.passenger.getName());
			System.out.print("Moving...");
			upstairs = true;
		} else {
			Elevator.passenger = new Person(passengerName, 1);
			System.out.println(passenger.getName()
					+ " has just pressed the button on floor "
					+ passenger.getFloor());
			if (getFloor().getDoor().isOpen() == false) {
				getFloor().toggleDoor();
			}
			System.out.println(passenger.getName() + " entered the elevator!");
			getFloor().toggleDoor();
			getOtherFloor().toggleLight();
			waitTime = this.schedule.generateNextRandom(1);
			if (averageWaitingTime == 0) {
				averageWaitingTime = waitTime / 10;
			}
			else {
				averageWaitingTime += waitTime / 10;
			}
			totalDrivingTime += (int) (waitTime / 10);
			System.out
					.println("Elevator is now moving to floor 0 with passenger "
							+ Elevator.passenger.getName());
			System.out.print("Moving...");
			upstairs = false;
		}
		Timer waiting = new Timer("waiting_timer", false);
		waiting.schedule(new TimerTask() {

			@Override
			public void run() {
				System.out.println("The elevator reached the floor #"
						+ getFloor().getLevel());
				getFloor().toggleLight();
				getBell().ring();
				if (getFloor().getDoor().isOpen() == false) {
					getFloor().toggleDoor();
				}
				System.out.println(getPassenger().getName()
						+ " left the elevator!");
				passengerHistory.add(passenger);
				if (getFloor().getLevel() == 0) {
					passengersDownstairs.add(passenger);
				}
				else {
					passengersUpstairs.add(passenger);
				}
				if (averageWaitingTime != 0) {
					averageWaitingTime /= passengerHistory.size();
				}
				Elevator.setPassenger(null);
				doPromt();
			}
		}, waitTime * 100);

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

	public static void main(String[] args) {
		Elevator elev = new Elevator();
		elev.provideTime();
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
					elev.changeFloor();
				} else {
					elev.changeFloor(name);
				}
				break;
			}
			case "state": {
				elev.printState();
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

}
