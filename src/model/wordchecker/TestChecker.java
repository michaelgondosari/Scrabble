package model.wordchecker;

import java.util.Scanner;

public class TestChecker {
    public TestChecker() {
    }

    public static void main(String[] args) {
        ScrabbleWordChecker fileStreamChecker = new FileStreamScrabbleWordChecker();
        ScrabbleWordChecker inMemoryChecker = new InMemoryScrabbleWordChecker();
        System.out.println("Please enter a word to check it against the collins dictionary.");
        System.out.println("There are two checkers available:");
        System.out.println(" - memory: this loads all words in memory (more memory, but less IO)");
        System.out.println(" - file: this checks the words files everytime (less memory, but more IO)");
        System.out.println();
        System.out.println("Usage: [checker:stream/memory] [Word]");
        System.out.println();
        System.out.print("> ");

        for(Scanner in = new Scanner(System.in); in.hasNextLine(); System.out.print(System.lineSeparator() + "> ")) {
            String input = in.nextLine();
            String[] splittedInput = input.split(" ");
            if ("exit".equals(input)) {
                System.out.println("The application will be closed");
                break;
            }

            if (splittedInput.length != 2) {
                System.out.println("Invalid input, expects: [Checker:stream/memory] [Word]");
            } else {
                String var7 = splittedInput[0];
                byte var8 = -1;
                switch(var7.hashCode()) {
                    case -1077756671:
                        if (var7.equals("memory")) {
                            var8 = 1;
                        }
                        break;
                    case -891990144:
                        if (var7.equals("stream")) {
                            var8 = 0;
                        }
                }

                Object checker;
                switch(var8) {
                    case 0:
                        checker = fileStreamChecker;
                        break;
                    case 1:
                        checker = inMemoryChecker;
                        break;
                    default:
                        checker = null;
                }

                if (checker == null) {
                    System.out.println("Invalid checker, expects: [Checker:stream/memory] [Word]");
                } else {
                    ScrabbleWordChecker.WordResponse response = ((ScrabbleWordChecker)checker).isValidWord(splittedInput[1]);
                    if (response == null) {
                        System.out.println("The word \"" + splittedInput[1] + "\" is not known in the dictionary!");
                    } else {
                        System.out.println(response);
                    }
                }
            }
        }

    }
}
