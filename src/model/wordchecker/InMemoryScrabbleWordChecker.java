package model.wordchecker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class InMemoryScrabbleWordChecker implements ScrabbleWordChecker {
    private static final Map<String, WordResponse> words = new HashMap();

    public InMemoryScrabbleWordChecker() {
    }

    public WordResponse isValidWord(String word) {
        return word != null && !word.isBlank() ? (WordResponse)words.get(word.toUpperCase()) : null;
    }

    static {
        try {
            InputStream resourceStream = InMemoryScrabbleWordChecker.class.getResourceAsStream("/collins_scrabble_words_2019.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(resourceStream));

            String line;
            try {
                while((line = br.readLine()) != null) {
                    String[] splitLine = line.split("\t");
                    if (splitLine.length == 2) {
                        words.put(splitLine[0], new WordResponse(splitLine[0], splitLine[1]));
                    }
                }
            } catch (Throwable var5) {
                try {
                    br.close();
                } catch (Throwable var4) {
                    var5.addSuppressed(var4);
                }

                throw var5;
            }

            br.close();
        } catch (IOException var6) {
            System.out.println("Could not load scrabble words: " + var6.getMessage());
            var6.printStackTrace();
        }

    }
}
