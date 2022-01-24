package model;

public class InvalidWordException extends Exception {

    public InvalidWordException() {
        super("The word is not found in the dictionary!");
    }

}
