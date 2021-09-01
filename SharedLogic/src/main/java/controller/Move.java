package controller;

import domain.Mark;

enum EndGameState {
    won, lost, tie
}

/**
 * Contains the data for the updated field,
 * after a move from the player
 * (and response of the opponent)
 *
 * If state is set, depending on its value,
 * the game was won, lost or their was a tie
 *
 * If it is null, the game goes on
 */
public record Move(EndGameState state, Mark[][] field) { }