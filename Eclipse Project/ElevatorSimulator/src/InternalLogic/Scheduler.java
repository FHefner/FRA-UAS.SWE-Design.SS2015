package InternalLogic;
import java.util.Random;


public class Scheduler {
	private int T1,T2;
	private Random randomGenerator;
	
	public Scheduler() {
		randomGenerator = new Random();
		T1 = randomGenerator.nextInt(20 - 5 + 1) + 5;
		T2 = randomGenerator.nextInt(20 - 5 + 1) + 5;
	}
	
	public int getT1() {
		return T1;
	}

	public int getT2() {
		return T2;
	}

	public int generateNextRandom(int person) {
		if (person == 1) {
			T1 += randomGenerator.nextInt(20 - 5 + 1) + 5;
			return T1;
		}
		else {
			T2 += randomGenerator.nextInt(20 - 5 + 1) + 5;
			return T2;
		}
	}
}
