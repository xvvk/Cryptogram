import java.io.Serializable;

public class player implements Serializable, Comparable<player>{

	private static final long serialVersionUID = 1L;
	private String username;
	private double accuracy;
	private int totalGuesses;
	private int correctGuesses;
	private int cryptogramsPlayed;
	private int cryptogramsCompleted;

	public player(String username, double accuracy, int totalGuesses, int correctGuesses, int cryptogramsPlayed, int cryptogramsCompleted) {
		this.username = username;
		this.accuracy = accuracy;
		this.totalGuesses = totalGuesses;
		this.correctGuesses = correctGuesses;
		this.cryptogramsPlayed = cryptogramsPlayed;
		this.cryptogramsCompleted = cryptogramsCompleted;
	}

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}	
	public double getAccuracy() {
		return accuracy;
	}
	public void setAccuracy(double accuracy) {
		this.accuracy = accuracy;
	}
	public void updateAccuracy() {
		double i = getCorrectGuesses();
		double j = getTotalGuesses();
		this.accuracy = ((i/j) * 100);
	}
	public int getTotalGuesses() {
		return totalGuesses;
	}
	public void setTotalGuesses(int totalGuesses) {
		this.totalGuesses = totalGuesses;
	}
	public void incrementTotalGuesses() {
		this.totalGuesses++; 
	}
	public int getCorrectGuesses() { 
		return correctGuesses; 
	}
	public void setCorrectGuesses(int correctGuesses) {
		this.correctGuesses = correctGuesses;
	}
	public void incrementCorrectGuesses() { 
		this.correctGuesses++; 
	}
	public void decrementCorrectGuesses() { 
		this.correctGuesses--; 
	}
	public int getCryptogramsPlayed() {
		return cryptogramsPlayed;
	}
	public void setCryptogramsPlayed(int cryptogramsPlayed) {
		this.cryptogramsPlayed = cryptogramsPlayed;
	}
	public void incrementsCryptogramsPlayed() {
		this.cryptogramsPlayed++;
	}
	public int getCryptogramsCompleted() {
		return cryptogramsCompleted;
	}
	public void setCryptogramsCompleted(int cryptogramsCompleted) {
		this.cryptogramsCompleted = cryptogramsCompleted;
	}
	public void incrementCryptogramsCompleted() {
		this.cryptogramsCompleted++;
	}
	
	@Override
	public String toString() {
		return username +
				"\t" +
				accuracy +
				"\t" +
				totalGuesses +
				"\t" +
				correctGuesses +
				"\t" +
				cryptogramsPlayed +
				"\t" +
				cryptogramsCompleted;
	}

	@Override
	public int compareTo(player otherPlayer) {
		return Integer.compare(otherPlayer.cryptogramsCompleted, this.cryptogramsCompleted);
	}
}
