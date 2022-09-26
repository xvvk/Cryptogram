import java.io.FileNotFoundException;
import java.util.*;
//let me push dammit
public class lettercryptogram extends cryptogram {
   
	private static final long serialVersionUID = 1L;
	private HashMap<Character, Character> cryptoAlphabet;
    private HashMap<Character, Character> userAlphabet;
    private String phrase;
    private ArrayList<String> encryptedPhrase;
    private char currentLetter;

    public lettercryptogram(String filePath) throws FileNotFoundException {
        super(filePath);
        this.phrase = getPhrase();
        this.cryptoAlphabet = new HashMap<>();
        this.userAlphabet = new HashMap<>();
        this.encryptedPhrase = new ArrayList<>();
        generateMappings();
    }

    private void generateMappings() {  // shifted by 3, so A is at D
        char encryptedLetter;
        int shiftBy = 3;
        
        for (int i = 0; i < phrase.length(); i++) {
            char letter = phrase.charAt(i);
            if (Character.isLetter(letter)) {
                char c = (char)(phrase.charAt(i) + shiftBy);
                if (c > 'z')
                    encryptedLetter = ((char) (letter - (26 - shiftBy)));
                else
                    encryptedLetter = c;
                cryptoAlphabet.put(encryptedLetter, letter);
                userAlphabet.put(encryptedLetter, '-');
                encryptedPhrase.add(Character.toString(encryptedLetter));
            } else {
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

    public HashMap<Character, Character> getCryptoAlphabet() {
        return cryptoAlphabet;
    }

    public HashMap<Character, Character> getUserAlphabet() {
        return userAlphabet;
    }

    public boolean isMapped(char l){
        return userAlphabet.containsValue(l);
    }

    public boolean updateMappings(char l){
        userAlphabet.replace(currentLetter, l);
        if(cryptoAlphabet.get(currentLetter) == l) {
            return true;
        }
        return false;
    }

    public char fetchVal(char c){
        return userAlphabet.getOrDefault(c, '-');
    }

    public boolean checkKey(char c) {
        currentLetter = c;
        return cryptoAlphabet.containsKey(c);
    }

    public char getValue(char c){
        return userAlphabet.get(c);
    }
    public char getTrueValue(char c) {return cryptoAlphabet.get(c);}

    public boolean undoGuess(char c){
        if(cryptoAlphabet.get(currentLetter) == userAlphabet.get(c)) {
            userAlphabet.replace(c, '-');
            return true;
        }
        userAlphabet.replace(c, '-');
        return false;
    }

    public String getType() { 
    	return "letter"; 
    }

}
