
public class Floor {
	
	private int level;
	private boolean arrivalLightOn;
	private Door myDoor;
	
	
	
	public Door getDoor() {
		return myDoor;
	}



	public Floor(int l) {
		this.level = l;
		myDoor = new Door();
		arrivalLightOn = false;
	}
	
	
	
	public void toggleLight() {
		if (arrivalLightOn) {
			arrivalLightOn = false;
			System.out.println("The arrival light was switched OFF at floor " + level);
		}
		else {
			arrivalLightOn = true;
			System.out.println("The arrival light was switched ON at floor " + level);
		}
	}


	public void toggleDoor() {
		if (myDoor.isOpen()) {
			myDoor.setOpen(false);
			System.out.println("Door was closed on floor " + level);
		}
		else {
			myDoor.setOpen(true);
			System.out.println("Door was opened on floor " + level);
		}
	}



	public int getLevel() {
		return level;
	}



	public void setLevel(int level) {
		this.level = level;
	}
}
