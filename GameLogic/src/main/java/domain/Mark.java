package domain;

/**
 * Represents a marking on a Grid
 * by the opponent or self (which is the player)
 * <p>
 * Is used in the Grid class
 */
public enum Mark {
    opponent, self;
  
    public static Mark invert(Mark mark) {
        if(mark == Mark.opponent) {
            return Mark.self;
        }
        return Mark.opponent;
    }
}