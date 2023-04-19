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

    /**
	 * This function returns the value of the "x" variable.
	 * 
	 * @return The method `getRow()` is returning the value of the instance variable `x`.
	 */
	public int getRow() {
        return this.x;
    }

    /**
	 * This Java function returns the value of the "y" variable.
	 * 
	 * @return The method `getCol()` is returning the value of the `y` attribute of the object.
	 */
	public int getCol() {
        return this.y;
    }

    /**
	 * The function translates a position by adding a vector to it.
	 * 
	 * @param vector The "vector" parameter is an object of the "Position" class that represents a 2D
	 * vector with an x and y component. It is used to specify the amount by which the current position
	 * should be translated. The method adds the x and y components of the vector to the x and y
	 * @return A new `Position` object with the `x` and `y` values of the current `Position` object
	 * added to the `x` and `y` values of the `vector` parameter.
	 */
	public Position translate(Position vector) {
        return new Position(this.x+vector.x, this.y+vector.y);
    }

    /**
	 * This function checks if a given point is outside the boundaries of an 8x8 board.
	 * 
	 * @return A boolean value indicating whether the current position represented by the x and y
	 * coordinates is off the board or not.
	 */
	public boolean isOffBoard() {
        return (this.x < 0 || this.x >= 8 ||
                this.y < 0 || this.y >= 8);
    }

    /**
	 * This function returns a string representation of the row and column values of an object.
	 * 
	 * @return A string representation of the object's row and column values enclosed in square
	 * brackets.
	 */
	@Override
    public String toString() {
        return "["+this.getRow()+","+this.getCol()+"]";
    }

}