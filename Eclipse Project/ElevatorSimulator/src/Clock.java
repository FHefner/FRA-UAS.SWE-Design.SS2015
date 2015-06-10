
public class Clock {
	private int count;
	
	public int calculateTime() {
		count++;
		return count;
	}
	
	public Clock() {
		count = 0;
	}

}
