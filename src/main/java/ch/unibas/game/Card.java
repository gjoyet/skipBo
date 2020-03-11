public class Card {

    private int number;
    private int specCard;
    private String colour;

    Card (int num, String col){ // Constructor for a normal card with color and a number
        number = num;
        colour = col;
    }

    Card (int num, int specCard){ // Constructor for special card (Skip Bo)
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
