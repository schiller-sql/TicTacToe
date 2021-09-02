package controller;

/**
 * State of the Move class,
 * if the Game has been ended,
 * and if yes, who won
 *
 * If the game is still going on,
 * the state would be running
 */
public enum GameState {
    won, lost, tie, running
}
