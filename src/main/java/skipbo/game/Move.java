package skipbo.game;

public class Move {
    boolean valid;
    Player player;
    char pileFrom;
    char pileTo;
    int indexFrom;
    int indexTo;
    Card stockPileTopCard;

    public Move(boolean valid) {
        this.valid = valid;
    }

    public Move(boolean v, Player p, char pF, char pT, int iF, int iT) {
        this.valid = v;
        this.player = p;
        this.pileFrom = pF;
        this.pileTo = pT;
        this.indexFrom = iF;
        this.indexTo = iT;
    }

    public Move(boolean v, Player p, char pF, char pT, int iF, int iT, Card c) {
        this.valid = v;
        this.player = p;
        this.pileFrom = pF;
        this.pileTo = pT;
        this.indexFrom = iF;
        this.indexTo = iT;
        this.stockPileTopCard = c;
    }

}
