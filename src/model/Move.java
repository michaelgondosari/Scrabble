package model;

import java.util.ArrayList;

public class Move {
    public static final int Sideways = 1;
    public static final int downward = 9;

    String word;
    int direction;
    int placeRow;
    int placeCol;
    boolean wordDoesExist;
    ArrayList<String> secondWord;

    public Move(String word, int direction, int placeCol, int placeRow){
    this.word = word;
    this.direction=direction;
    this.placeRow=placeRow;
    this.placeCol = placeCol;
    this.wordDoesExist= false;
    }
    public int scoreCalculator(){
     int score = 0;
        if(wordDoesExist){

     }
    }

}



