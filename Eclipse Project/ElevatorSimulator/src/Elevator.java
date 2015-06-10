import java.util.LinkedList;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class Elevator {
	private Floor fl1, fl2;
	private static int currentTime;
	private boolean hasPassenger;
	private boolean upstairs;
	private static Person passenger;
	private LinkedList<Person> passengerHistory;
	private Scheduler schedule;
	private Bell bell;
	
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
		setBell(new Bell("src/res/Airplane-ding-sound.wav"));
		upstairs = false;
		passenger = null;
		passengerHistory = new LinkedList<Person>();
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
		}
		else {
			return fl1;
		}
	}
	
	public Floor getOtherFloor() {
		Floor temp = getFloor();
		if (temp == fl1) {
			return fl2;
		}
		else {
			return fl1;
		}
	}

	public void setFloor(Floor activeFloor) {
		int lvl = activeFloor.getLevel();
		if (lvl == 1) {
			upstairs = true;
		}
		else {
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
		}
		else {
			String output = "Passenger History: [";
			for (Person p : passengerHistory) {
				output += p.getName() + ", ";
			}
			output = output.substring(0, output.length() - 2);
			output += "]";
			System.out.println(output);
		}
	}
	
	public void doPromt() {
		System.out.println("==============================================");
		System.out.println("Elevator is ready for the next command, 'drive', 'state' or 'exit':");
	}
	
	public void printState() {
		System.out.println("==============================================");
		System.out.println("Current Time: " + getCurrentTime() + " seconds");
		System.out.println("Current Floor: " + getFloor().getLevel());
		if (getPassenger() == null) {
			System.out.println("Current Passenger: " + "NO ONE!");
		}
		else {
			System.out.println("Current Passenger: " + getPassenger().getName());
		}
		printPassengerHistory();
		System.out.println("Random number T1: " + schedule.getT1());
		System.out.println("Random number T2: " + schedule.getT2());
		System.out.println("==============================================");
		doPromt();
	}
	
	public void changeFloor() {
		String name = "";
		if (this.getFloor().getLevel() == 0) {
			name = "Person 0";
		}
		else {
			name = "Person 1";
		}
		changeFloor(name);
	}

	public void changeFloor(String passengerName) {
		int waitTime = -1;
		
		
		if (this.getFloor().getLevel() == 0) {
			Elevator.passenger = new Person(passengerName, 0);
			System.out.println(passenger.getName() + " has just pressed the button on floor " + passenger.getFloor());
			if (getFloor().getDoor().isOpen() == false) {
				getFloor().toggleDoor();
			}
			System.out.println(passenger.getName() + " entered the elevator!");
			getFloor().toggleDoor();
			getOtherFloor().toggleLight();
			waitTime = this.schedule.generateNextRandom(0);
			System.out.println("Elevator is now moving to floor 1 with passenger " + Elevator.passenger.getName());
			System.out.print("Moving...");
			upstairs = true;
		} else {
			Elevator.passenger = new Person(passengerName, 1);
			System.out.println(passenger.getName() + " has just pressed the button on floor " + passenger.getFloor());
			if (getFloor().getDoor().isOpen() == false) {
				getFloor().toggleDoor();
			}
			System.out.println(passenger.getName() + " entered the elevator!");
			getFloor().toggleDoor();
			getOtherFloor().toggleLight();
			waitTime = this.schedule.generateNextRandom(1);
			System.out.println("Elevator is now moving to floor 0 with passenger " + Elevator.passenger.getName());
			System.out.print("Moving...");
			upstairs = false;
		}
		Timer waiting = new Timer("waiting_timer", false);
		waiting.schedule(new TimerTask() {
			
			@Override
			public void run() {
				System.out.println("The elevator reached the floor #" + getFloor().getLevel());
				getFloor().toggleLight();
				getBell().ring();
				if (getFloor().getDoor().isOpen() == false) {
					getFloor().toggleDoor();
				}
				System.out.println(getPassenger().getName() + " left the elevator!");
				passengerHistory.add(passenger);
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
		System.out.println("Enter 'drive' to move the elevator, 'state' for the current elevator state and 'exit' to quit the program");
		while (true) {
			
			String input = sc.next();
			switch (input) {
			
			case "drive": {
				System.out.println("Enter the passenger name or enter a 'd' to use default names:");
				String name = sc.next();
				if (name.trim().equals("d")) {
					elev.changeFloor();
				}
				else {
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
