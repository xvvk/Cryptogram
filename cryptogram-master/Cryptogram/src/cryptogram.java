import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public abstract class cryptogram implements Serializable {

	private static final long serialVersionUID = 1L;
	private String phrase;
    private ArrayList<String> encryptedPhrase;

    public cryptogram(String filePath) throws FileNotFoundException {
        Scanner input = new Scanner(new File(filePath));
        Random ran = new Random();
        this.encryptedPhrase = new ArrayList<String>();
        int n = 0;

        while (input.hasNext()) {	
            ++n;							// increments random counter
            String str = input.nextLine();
            if (ran.nextInt(n) == 0)		// stores random string
                phrase = str.toLowerCase();
        }
        input.close();

    }

    public String getPhrase() {
        return phrase;
    }
    public void setPhrase(String phrase) {
        this.phrase = phrase;
    }

    public ArrayList<String> getEncryptedPhrase() {
        return encryptedPhrase;
    }
    
    public abstract HashMap<?, Character> getUserAlphabet();
    public abstract HashMap<?, Character> getCryptoAlphabet();
    public abstract boolean updateMappings(char l);
    public abstract boolean isMapped(char l);
    public abstract String getType();
}
