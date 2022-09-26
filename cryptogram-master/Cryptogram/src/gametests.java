
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("unused")
public class gametests {

	private game g;
	private player p;
	//private player p2;
	private players ps;
	private cryptogram c;
	private List<Character> cl; // common letters
	private HashMap<Character, Character> cryptoAlphabet;
	private HashMap<Character, Character> userAlphabet;
	private ArrayList<String> encryptedPhrase;
	private String phrase;

	@Before
	public void setUp() throws FileNotFoundException { 
		this.p = new player(null, 0, 0, 0, 0, 0);
		//this.p2 = new player(null, 0, 0, 0, 0, 0);
		this.g = new game(null, null);
		this.ps = new players();
		this.cl = Arrays.asList('e', 'a', 'r', 'i', 'o', 't', 'n', 's', 'l', 'c', 'u', 'd', 'p', 'm', 'h', 'g', 'b', 'f', 'y', 'w', 'k', 'v', 'x', 'z', 'j', 'q');
		phrase = "test";
		this.cryptoAlphabet = new HashMap<>();
		this.userAlphabet = new HashMap<>();
		this.encryptedPhrase = new ArrayList<>();
	}
	
	
	@Test
	public void showSolutionTest() {
		// user story 6
		
		
		
	}
	
	@Test
	public void showFrequenciesTest() {
		// user story 7
		/*assertEquals(cl, g.getCommonLetters());
		System.out.println(cl);
		System.out.println(g.getCommonLetters());
		
		assertEquals(new Character ('e'), cl.get(0));
		System.out.println(cl.get(0));
		System.out.println(g.getCommonLetters().get(0));
		assertEquals(new Character ('e'), g.getCommonLetters().get(0));
		
		generateMappings();
		System.out.println("here: " + getCryptoAlphabet());
		getFrequency();
		*/
		System.out.println(" ");	
		System.out.println("Common Proportions of Letter Frequencies: ");
		System.out.print("{");
		for(int i = 0; i < cl.size(); i++) {
			System.out.print(cl.get(i) + ":" + i + ", ");
		}
		System.out.print("}");
		
	}
	
	public HashMap<Character, Character> getCryptoAlphabet() {
		return cryptoAlphabet;
	}
	
	public ArrayList<String> getEncryptedPhrase() {
		return encryptedPhrase;
	}
	
	public void getFrequency() { // helper method to get frequency
		char[] arr = getCryptoAlphabet().keySet().toString().toCharArray();
		System.out.println("Letter Frequencies of Current Cryptogram: ");
		for(int i = 0; i < arr.length;i++) {
			int count  = 0;
			if((int) arr[i] >= 97 && (int) arr[i] <=122) {
				for(int b = 0; b< getEncryptedPhrase().size();b++){
					if(arr[i] == getEncryptedPhrase().get(b).charAt(0)){
						count++;
					}
				}
				System.out.println(arr[i] + ":" + count + "");
			}
		}
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
			
}
