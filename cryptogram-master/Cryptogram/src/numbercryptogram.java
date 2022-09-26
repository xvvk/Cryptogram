import java.io.FileNotFoundException;
import java.util.*;
//let me push dammit
public class numbercryptogram extends cryptogram{

	private static final long serialVersionUID = 1L;
	private HashMap<Integer, Character> cryptoAlphabet;
	private HashMap<Integer, Character> userAlphabet;
	private String phrase;
	private ArrayList<String> encryptedPhrase;
	private int currentNumber;

	public numbercryptogram(String filePath) throws FileNotFoundException {
		super(filePath);
		this.phrase = getPhrase();
		this.cryptoAlphabet = new HashMap<>();
		this.userAlphabet = new HashMap<>();
		this.encryptedPhrase = new ArrayList<>();
		generateMappings();
	}

	private void generateMappings() { 		//completely randomised right now, if someone can map it easier 
											// thatd be great T^T
						
		int encryptedNumber;
		String punctuations = ".,:;";
		HashMap<Character, Integer> numberKeys = new HashMap<>();
		for (int i = 0; i < phrase.length(); i++) {
			char letter = phrase.charAt(i);
			if (Character.isLetter(letter)) {
				if (!cryptoAlphabet.containsValue(letter)) {
					encryptedNumber = (new Random()).nextInt(26) + 1;
					while (cryptoAlphabet.containsKey(encryptedNumber)) {
						encryptedNumber = 26 - 3;
					}
				} else {
					// Get repetitive number from HashMap
					encryptedNumber = numberKeys.get(letter);
				}
				cryptoAlphabet.put(encryptedNumber, letter);
				userAlphabet.put(encryptedNumber, '-');
				numberKeys.put(letter, encryptedNumber);
				encryptedPhrase.add(Integer.toString(encryptedNumber));
			} else {
				if(punctuations.indexOf(letter) > 0) {
					encryptedPhrase.add(Character.toString(letter));
				} else {
				}
				encryptedPhrase.add(Character.toString(letter));
			}
		}
	}

	public void setPhrase(String phrase) {
		super.setPhrase(phrase);
	}

	public String getPhrase() {
		return super.getPhrase();
	}

	public ArrayList<String> getEncryptedPhrase() {
		return encryptedPhrase;
	}

	public HashMap<Integer, Character> getCryptoAlphabet() {
		return cryptoAlphabet;
	}

	public HashMap<Integer, Character> getUserAlphabet() {
		return userAlphabet;
	}

	public boolean isMapped(char l){
		return userAlphabet.containsValue(l);
	}

	public boolean updateMappings(char l){
		userAlphabet.replace(currentNumber, l);
		if(cryptoAlphabet.get(currentNumber) == l) {
			return true;
		}
		return false;
	}

	public char fetchVal(int n){
		return userAlphabet.getOrDefault(n, '-');
	}

	public boolean checkKey(int n){
		currentNumber = n;
		return cryptoAlphabet.containsKey(n);
	}

	public char getValue(int n){
		return userAlphabet.get(n);
	}
	public char getTrueValue(int n) {
		return cryptoAlphabet.get(n);
	}

	public boolean undoGuess(int n){
		if(cryptoAlphabet.get(currentNumber) == userAlphabet.get(n)) {
			userAlphabet.replace(n, '-');
			return true;
		}
		userAlphabet.replace(n, '-');
		return false;
	}

	public String getType() { 
		return "number"; 
	}
}




