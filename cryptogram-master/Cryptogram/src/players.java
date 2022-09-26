import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class players implements Serializable{

	private static final long serialVersionUID = 1L;
	private ArrayList<player> allplayers;
	private ArrayList<player> tempArray;
	private static final String FILE_PATH = "playerDetails.txt";


	public players() {
		this.allplayers = getAllplayers();
	}

	public players(ArrayList<player> allplayers) {
		this.allplayers = allplayers;
	}

	public void addPlayer(player player) { //adds new player
		allplayers.add(player);
	}

	private void savePlayer(String playersDetails) throws IOException { //saves player in txt file
		FileWriter fw = new FileWriter(FILE_PATH, true);
		PrintWriter pw = new PrintWriter(fw);
		
		pw.println(playersDetails);
		pw.close();
		fw.close();
	}

	public ArrayList<player> getAllplayers() {
		tempArray = new ArrayList<>();
		try {
			Scanner fileSc = new Scanner(new File(FILE_PATH));
			while (fileSc.hasNext()) {
				try {
					tempArray.add(new player(fileSc.next(), fileSc.nextDouble(), fileSc.nextInt(), fileSc.nextInt(), fileSc.nextInt(), fileSc.nextInt()));
				}
				catch (Exception e){  // exception thrown if corrupt
					System.err.println("Error: Corrupted player file");
					//System.exit(0);
				}
			}
			fileSc.close();
		} catch (FileNotFoundException e) { //exception thrown if file does not exist
			System.err.println("No player file was found! Creating a new player...");
			@SuppressWarnings("unused")
			File f = new File(FILE_PATH); //creates a new playerDetails.txt, in src
		}
		return tempArray;
	}


	public player existingPlayer(String username) {
		for (player p : allplayers) {
			if (p.getUsername() != null && p.getUsername().equals(username)) {
				return p;
			}
		}
		return null;
	}

	private int getIndex(String wanted) { //matches the username to username input
		for(int i = 0; i<allplayers.size(); i++) {
			if (allplayers.get(i).getUsername().equals(wanted)) {
				return i;
			}
		}
		return -1;
	}

	public void updatePlayer(player p) {
		allplayers.set(getIndex(p.getUsername()), p);
	}

	public void saveplayers() {
		try {

			if (!new File(FILE_PATH).delete()) {
				System.err.println("Content failed to delete!");
			}
			savePlayer(toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public String toString() {	//maintain expansion to strings (playerDetails)
		StringBuilder sb = new StringBuilder();
		for (player p: allplayers){
			sb.append(p.toString());
			sb.append("\n");
		}
		return sb.toString();
	}
}
