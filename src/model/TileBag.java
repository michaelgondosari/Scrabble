package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TileBag {

    // --- Variables -------------------------------

    private Map<Character, Integer> letterToAmountLeft = new HashMap<>();
    private Map<Character, Integer> letterToValue = new HashMap<>();

    // --- Constructor -----------------------------

    public TileBag (String fileName) {
        try {
            readTxt(fileName);
        } catch (IOException e) {
            System.out.println("File \"letters.txt\" is missing.");
            System.exit(1);
        }
    }

    // --- Queries ---------------------------------

    /**
     * Returns the amount left of a character
     * @param c - the character (A-Z)
     * @return the amount left of the character
     */
    public int getLetterAmountLeft(char c) {
        return letterToAmountLeft.get(Character.toUpperCase(c));
    }

    /**
     * Returns the value of a character
     * @param c - the character (A-Z)
     * @return the value of the character
     */
    public int getLetterValue(char c) {
        return letterToValue.get(Character.toUpperCase(c));
    }

    // --- Commands --------------------------------

    /**
     * Read file to put amount left and value of each letter, then putting both in maps of letter
     * @param fileName - file to read the amount left and value of each letter
     * @throws IOException
     */
    private void readTxt(String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String line;
        while ((line = br.readLine()) != null) {

            String[] lettersTxt = line.split("/");
            char letter = lettersTxt[0].charAt(0);
            int letterAmount = Integer.parseInt(lettersTxt[1]);
            int letterValue = Integer.parseInt(lettersTxt[2]);

            letterToAmountLeft.put(letter, letterAmount);
            letterToValue.put(letter, letterValue);
        }
        br.close();
    }

    public static void main(String[] args) {
        TileBag tb = new TileBag("C:\\Users\\HP\\Desktop\\MASTER\\UniversityofTwente\\PreMaster\\PreMasterQ2\\Programming\\Project\\Scrabble\\letters.txt");
        System.out.println(tb.getLetterValue('z'));
    }

} // end of class
