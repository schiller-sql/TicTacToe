package com.tictactoe.logic.opponents;

import com.tictactoe.logic.domain.Point;
import com.tictactoe.logic.domain.Field;

public interface Opponent {
    public Point move(Field field);
}