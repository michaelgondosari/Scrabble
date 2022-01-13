package model.wordchecker;

public interface ScrabbleWordChecker {
    ScrabbleWordChecker.WordResponse isValidWord(String var1);

    public static class WordResponse {
        private String word;
        private String description;

        public WordResponse(String word, String description) {
            this.word = word;
            this.description = description;
        }

        public String getWord() {
            return this.word;
        }

        public String getDescription() {
            return this.description;
        }

        public String toString() {
            String var10000 = this.getWord();
            return var10000 + ": " + this.getDescription();
        }
    }
}
