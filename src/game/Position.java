package game;

import java.awt.*;

/*
 * Copyright 2017 Roger J

package game;
import java.awt.Point;

 */
public class Position extends Point {

    public Position(int row, int col) {
        super (row, col);
    }

    public int getRow() {
        return this.x;
    }

    public int getCol() {
        return this.y;
    }

    public Position translate(Position vector) {
        return new Position(this.x+vector.x, this.y+vector.y);
    }

    public boolean isOffBoard() {
        return (this.x < 0 || this.x >= 8 ||
                this.y < 0 || this.y >= 8);
    }

    @Override
    public String toString() {
        return "["+this.getRow()+","+this.getCol()+"]";
    }

}