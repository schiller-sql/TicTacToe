package com.tictactoe.logic.controller;

import com.tictactoe.logic.domain.Mark;

enum EndGameState {
    won, lost, tie
}

public record Move(EndGameState state, Mark[][] field) { }