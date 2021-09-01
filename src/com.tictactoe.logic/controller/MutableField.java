package com.tictactoe.logic.controller;

import com.tictactoe.logic.domain.Point;
import com.tictactoe.logic.domain.Field;
import com.tictactoe.logic.domain.Mark;

public class MutableField extends Field {

    public void setMarkOpponent(Point point) {
        assert(markIsEmpty(point));
        field[point.x()][point.y()] = Mark.opponent;
    }

    public void setMarkSelf(Point point) {
        assert(markIsEmpty(point));
        field[point.x()][point.y()] = Mark.self;
    }
}