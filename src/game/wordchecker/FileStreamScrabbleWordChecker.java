package game.wordchecker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileStreamScrabbleWordChecker implements ScrabbleWordChecker {
    public FileStreamScrabbleWordChecker() {
    }

    public WordResponse isValidWord(String word) {
        if (word != null && !word.isBlank()) {
            word = word.toUpperCase();

            try {
                InputStream resourceStream = FileStreamScrabbleWordChecker.class.getResourceAsStream("/collins_scrabble_words_2019.txt");
                BufferedReader br = new BufferedReader(new InputStreamReader(resourceStream));

                WordResponse var6;
                label63: {
                    String line;
                    try {
                        while((line = br.readLine()) != null) {
                            String[] splitLine = line.split("\t");
                            if (splitLine.length == 2 && word.equals(splitLine[0])) {
                                var6 = new WordResponse(splitLine[0], splitLine[1]);
                                break label63;
                            }
                        }
                    } catch (Throwable var8) {
                        try {
                            br.close();
                        } catch (Throwable var7) {
                            var8.addSuppressed(var7);
                        }

                        throw var8;
                    }

                    br.close();
                    return null;
                }

                br.close();
                return var6;
            } catch (IOException var9) {
                System.out.println("Could not load scrabble words: " + var9.getMessage());
                var9.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }
}
