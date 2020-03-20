//package ch.unibas;
import java.awt.Color;
//import

public class Card {

    private int number;
    private int specCard;
    Color col;

    Card (int num, Color col){ // Constructor for a normal card with enum color, and a number
        number = num;
        this.col = col;
    }

    Card (int num, int specCard){ // Constructor for the special card (Skip Bo)
        number = num;
        this.specCard = specCard;
    }

    public int getNum(){
        if (number == 13 && specCard > 0){
            return specCard;
        }else{
            return number;
        }
    }


}
