
public class Person {
	private int floor;
	private String name;
	Person(String n, int f) {
		this.name = n;
		this.floor = f;
	}
	public int getFloor() {
		return floor;
	}
	public void setFloor(int floor) {
		this.floor = floor;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
