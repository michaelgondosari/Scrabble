package model;

import view.TerminalColors;

import java.io.*;
import java.util.*;

public class TileBag {

    // --- Variables -------------------------------

    private Map<Character, Integer> letterToAmountLeft = new HashMap<>();
    private Map<Character, Integer> letterToValue = new HashMap<>();

    // --- Constructor -----------------------------

    /**
     * Constructor for a new tile bag
     * @param fileName "letters.txt" file containing (letters/value/amount)
     */
    public TileBag (String fileName) {
        try {
            readTxt(fileName);
        } catch (FileNotFoundException e) {
            System.out.println("File \"letters.txt\" is missing.");
            System.exit(1);
        } catch (IOException e) {
            System.out.println("There is a problem with the file \"letters.txt\".");
            System.exit(1);
        }
    }

    // --- Queries ---------------------------------

    /**
     * Returns the amount left of a character
     *
     * @param c the character (A-Z)
     * @return the amount left of the character
     */
    public int getLetterAmountLeft(char c) {
        return letterToAmountLeft.get(Character.toUpperCase(c));
    }

    /**
     * Returns the value of a character
     *
     * @param c the character (A-Z)
     * @return the value of the character
     */
    public int getLetterValue(char c) {
        return letterToValue.get(Character.toUpperCase(c));
    }

    // --- Commands --------------------------------

    /**
     * Read file to put amount left and value of each letter, then putting both in maps of letter
     * @param fileName file to read the amount left and value of each letter
     * @throws IOException if file is not found or broken
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

    /**
     * Draw a random tile from the tile bag
     * @return a random tile
     */
    private char drawTiles() {
        Random random = new Random();
        int tilesLeft = this.getTilesLeft();

        // If there are no tiles left, this method should throw an exception
        if (tilesLeft < 1) {
            throw new RuntimeException(TerminalColors.RED_BOLD + "There are no tiles left!" + TerminalColors.RESET);
        }

        int randomNumber = random.nextInt(tilesLeft); // randomize pick to a number between 0 and tilesLeft
        char tileDrawn = (char)(-1);
        for (char c : letterToAmountLeft.keySet()) {
            int amountLeft = getLetterAmountLeft(c);
            randomNumber -= amountLeft;
            if (randomNumber <= 0) { // meaning that the letter is available to be drawn
                tileDrawn = c;
                letterToAmountLeft.put(c, amountLeft-1);
                break; // break the for loop
            }
        }
        return tileDrawn;
    }

    /**
     * Draw random tiles from the tile bag as many as requested, then putting the tiles drawn into a list
     * @param amount the number of tiles to be drawn
     * @return list of tiles as many as requested
     */
    public List<Character> drawTiles(int amount) {
        int tilesLeft = this.getTilesLeft();
        if (amount > tilesLeft) {
            amount = tilesLeft;
        }

        List<Character> tilesList = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            tilesList.add(this.drawTiles());
        }
        return tilesList;
    }

    /**
     * Swap tiles
     * @param tilesSwap list of tiles that the player wants to swap (put back into the tile bag)
     * @return a list of random new tiles drawn from the bag, as many as the tiles put back into the bag
     */
    public List<Character> swapTiles(List<Character> tilesSwap) {
        for (char c : tilesSwap) {
            letterToAmountLeft.put(c, getLetterAmountLeft(c) + 1); // putting the tile back inside the tile bag
        }
        return this.drawTiles(tilesSwap.size());
    }

} // end of class