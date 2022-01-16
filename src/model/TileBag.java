package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class TileBag {

    // --- Variables -------------------------------

    private Map<Character, Integer> letterToAmountLeft = new HashMap<>();
    private Map<Character, Integer> letterToValue = new HashMap<>();

    // --- Constructor -----------------------------

    public TileBag (String fileName) {

        // First, read the file with letters, value & letters, amount
        // then put them in maps
        try {
            readTxt(fileName);
        } catch (IOException e) {
            System.out.println("File \"letters.txt\" is missing.");
            System.exit(1);
        }

    }

    // --- Queries ---------------------------------

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
    public void readTxt(String fileName) throws IOException {
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

    /**
     * Returns how many tiles are still in the bag
     * @return the amount left of the tiles in the bag
     */
    public int getTilesLeft() {
        int amountLeft = 0;
        for (int i : letterToAmountLeft.values()) {
            amountLeft += i;
        }
        return amountLeft;
    }

    private char drawTiles() {
        Random random = new Random();
        int tilesLeft = this.getTilesLeft();

        // If there are no tiles left, this method should throw an exception
        if (tilesLeft < 1) {
            throw new RuntimeException("There are no tiles left!");
        }

        int randomNumber = random.nextInt(tilesLeft); // randomize pick to a number between 0 and tilesLeft
        char tileDrawn = (char)(-1);
        for (char c : letterToAmountLeft.keySet()) {
            int amountLeft = letterToAmountLeft.get(c);
            randomNumber -= amountLeft;
            if (randomNumber <= 0) { // meaning that the letter is available to be drawn
                tileDrawn = c;
                System.out.println(letterToAmountLeft.get(c));
                letterToAmountLeft.put(c, amountLeft-1);
                System.out.println(letterToAmountLeft.get(c));
                break; // break the for loop
            }
        }
        return tileDrawn;
    }

    public static void main(String[] args) {
        TileBag tb = new TileBag("C:\\Users\\HP\\Desktop\\MASTER\\UniversityofTwente\\PreMaster\\PreMasterQ2\\Programming\\Project\\Scrabble\\letters.txt");
        System.out.println(tb.getLetterValue('z'));
        System.out.println(tb.getTilesLeft());
        System.out.println(tb.drawTiles());
    }

} // end of class
