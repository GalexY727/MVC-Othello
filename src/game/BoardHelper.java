package game;

public class BoardHelper {

    /**
	 * The function checks if there are any moves available for both players on a given game board and
	 * returns true if there are no moves available for either player.
	 * 
	 * @param board The parameter "board" is a 2D integer array that represents the current state of a
	 * game board. The values in the array represent the different pieces or tokens on the board, with
	 * each player typically having their own unique value. The function "isGameFinished" checks
	 * whether there are any moves
	 * @return The method is returning a boolean value that indicates whether the game is finished or
	 * not. It returns true if there are no moves available for either player (player 1 or player 2),
	 * and false otherwise.
	 */
	public static boolean isGameFinished(int[][] board) {
        return !(anyMovesAvailable(board,1) || anyMovesAvailable(board,2));
    }


    /**
	 * The function returns the winner of a game based on the scores of two players in a given board.
	 * 
	 * @param board a 2D integer array representing the game board, where each element represents the
	 * number of stones in that cell.
	 * @return The method `getWinner` returns an integer value that represents the winner of the game.
	 * If the game is not finished, it returns -1. If the game is finished and there is a tie, it
	 * returns 0. If player 1 wins, it returns 1. If player 2 wins, it returns 2.
	 */
	public static int getWinner(int[][] board) {
        if (!isGameFinished(board))
            //game not finished
            return -1;
        else {
            //count stones
            int p1s = getPlayerScore(board, 1);
            int p2s = getPlayerScore(board, 2);

            if (p1s == p2s) {
                //tie
                return 0;
            } else if (p1s > p2s) {
                //p1 wins
                return 1;
            } else {
                //p2 wins
                return 2;
            }
        }
    }

    /**
	 * The function returns the score of a player in a game represented by a 2D board.
	 * 
	 * @param board The board is a 2D array of integers representing the current state of a game. Each
	 * element in the array represents a cell on the game board, and the value of the element indicates
	 * which player (if any) occupies that cell. A value of 0 indicates an empty cell, while a
	 * @param player The player parameter is an integer value representing the player for whom we want
	 * to calculate the score. It is used to count the number of cells on the board that belong to the
	 * specified player.
	 * @return The method `getPlayerScore` returns an integer value which represents the score of a
	 * player on a given game board.
	 */
	public static int getPlayerScore(int[][] board, int player) {
        int score = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == player) score++;
            }
        }
        return score;
    }


    /**
	 * The function returns the value of a specific position in a 2D array.
	 * 
	 * @param board a 2D array representing a game board where each element contains an integer value
	 * @param position The parameter "position" is an object of the class "Position" which contains
	 * information about a specific position on a two-dimensional board. It has two attributes: "row"
	 * and "col" which represent the row and column indices of the position on the board.
	 * @return The method `getSquare` is returning the value of the square at the given `position` in
	 * the `board` array.
	 */
	public static int getSquare(int[][] board, Position position) {
        return board[position.getRow()][position.getCol()];
    }

    /**
	 * The function checks if a move is legal in a game by verifying if the space is empty and checking
	 * all directions for a valid move.
	 * 
	 * @param board a 2D array representing the game board, where each element represents a square on
	 * the board and its value indicates which player (1 or 2) has a piece on that square, or 0 if the
	 * square is empty.
	 * @param player an integer representing the player making the move (1 or 2)
	 * @param positionToCheck The position on the board that needs to be checked for legality of the
	 * move.
	 * @return The method is returning a boolean value, which indicates whether the move at the given
	 * position is legal or not.
	 */
	public static boolean isLegalMove(int[][] board, int player, Position positionToCheck) {
        // If the space isn't empty, it's not a legal move
        if (getSquare(board, positionToCheck) != 0)
            return false;
        // Check all directions to see if the move is legal
        for (String direction : Directions.getDirections()) {
            Position directionVector = Directions.getVector(direction);
            if (step(player, board, positionToCheck, directionVector, 0)) {
                return true;
            }
        }
        return false;
    }

    /**
	 * The function checks if there are any legal moves available for a given player on a given board.
	 * 
	 * @param board a 2D array representing the current state of the game board
	 * @param player The player parameter is an integer value representing the player for whom we are
	 * checking if there are any legal moves available.
	 * @return The method is returning a boolean value, either true or false. It returns true if there
	 * is at least one legal move available for the given player on the given board, and false
	 * otherwise.
	 */
	public static boolean anyMovesAvailable(int[][] board, int player) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Position pos = new Position(row, col);
                if (isLegalMove(board, player, pos)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
	 * The function checks if a given move is legal in a game by analyzing the board, player, and
	 * direction.
	 * 
	 * @param player An integer representing the player making the move.
	 * @param board A 2D array representing the game board, where each element represents a square on
	 * the board and its value represents the player who occupies that square (0 for empty, 1 for
	 * player 1, 2 for player 2).
	 * @param position The current position of the player on the board.
	 * @param direction The direction in which the player is attempting to move on the board. It is a
	 * Position object representing the direction of the move.
	 * @param count The number of spaces the player has already moved in the given direction.
	 * @return A boolean value indicating whether the move is legal or not.
	 */
	protected static boolean step(int player, int[][] board, Position position, Position direction, int count) {
        // `Position newPosition = position.translate(direction);` is creating a new `Position` object
		// called `newPosition` by translating the current `position` object in the direction specified
		// by the `direction` object. The `translate` method is defined in the `Position` class and
		// takes a `Position` object representing a direction and returns a new `Position` object that
		// is the result of moving one space in that direction from the current position.
		Position newPosition = position.translate(direction);
        int oplayer = ((player == 1) ? 2 : 1);

        if (newPosition.isOffBoard()) {
            return false;
        } 
		
		// This code block is checking if the new position being evaluated is an empty space AND is
		// adjacent to the current position being evaluated (count == 0). If both conditions are
		// true, then the move is not legal and the function returns false.
		else if ((getSquare(board, newPosition) == 0) && (count == 0)) {
            return false;
        } 
		
		// This code block is checking if the new position being evaluated has a piece belonging to
		// the opposing player AND is not an empty space. If both conditions are true, then the
		// function recursively calls itself with the new position and direction, incrementing the
		// count by 1. This continues until the function either finds a space belonging to the
		// current player or an empty space, or until it reaches the edge of the board.
		else if (getSquare(board, newPosition) == oplayer && getSquare(board, newPosition) != 0) {
            return step(player, board, newPosition, direction, count+1);
        } 
		
		// This code block is checking if the new position being evaluated has a piece belonging to
		// the current player AND is not an empty space. If both conditions are true, then the
		// function checks if the count is greater than 0. If it is, then the move is legal and the
		// function returns true. If it is not, then the move is not legal and the function returns
		// false. This is checking if the player has moved more than one space in the given
		// direction, which is necessary for the move to be legal.
		else if (getSquare(board, newPosition) == player) {
            return count > 0;
        } 
		
		// This code block is executed if none of the previous conditions in the `step` method are met. It
		// simply returns `false`, indicating that the move being evaluated is not legal.
		else {
            return false;
        }
    }

    /**
	 * The function checks if a move is valid in a board game by recursively checking if a player can
	 * move in a certain direction.
	 * 
	 * @param player an integer representing the current player (either 1 or 2)
	 * @param board a 2D array representing the game board, where each element represents a square on
	 * the board and contains either a 0 (empty), 1 (player 1), or 2 (player 2)
	 * @param position The current position of the player on the board.
	 * @param direction The direction parameter is a Position object that represents the direction in
	 * which the move is being made. It is used to determine the next position to check during the
	 * move.
	 * @param count The number of squares that have been traversed in a particular direction so far.
	 * @return The method `makeMoveStep` returns a boolean value.
	 */
	private static boolean makeMoveStep(int player, int[][] board, Position position, Position direction, int count) {
        Position newPosition = position.translate(direction);
        int oplayer = ((player == 1) ? 2 : 1);

        if (newPosition.isOffBoard()) {
            return false;
        } 
		
		// The above code is a part of a method that checks if a move is valid in a game. It checks if the
		// square on the board at a new position is occupied by the opponent player. If it is, it recursively
		// calls the `makeMoveStep` method with the updated position, direction, and count. If the move is
		// valid, it sets the square to the current player and returns true. Otherwise, it returns false.
		else if (getSquare(board, newPosition) == oplayer) {
            boolean valid = makeMoveStep(player, board, newPosition, direction, count+1);
            if (valid) {
                setSquare(player, board, newPosition);
            }
            return valid;
        } 
		
		// The above code is a conditional statement in Java. It checks if the value returned by the
		// `getSquare` method for the `board` and `newPosition` parameters is equal to the `player` variable.
		// If it is, then it returns a boolean value indicating whether the `count` variable is greater than
		// zero.
		else if (getSquare(board, newPosition) == player) {
            return count > 0;
        } else {
            return false;
        }
    }

	
    /**
	 * The function makes a move for a player on a given board at a specified position by iterating
	 * through all possible directions and checking if the move is valid.
	 * 
	 * @param playerToMove An integer representing the player who is making the move.
	 * @param board A 2D integer array representing the game board. Each element in the array
	 * represents a square on the board and its value indicates which player (if any) occupies that
	 * square.
	 * @param positionToMove The position on the board where the player wants to make a move.
	 * @return The method is returning a 2D integer array representing the updated game board after a
	 * player has made a move.
	 */
	public static int[][] makeMove(int playerToMove, int[][] board, Position positionToMove) {
        for (String direction : Directions.getDirections()) {
            Position directionVector = Directions.getVector(direction);
            if (makeMoveStep(playerToMove, board, positionToMove, directionVector, 0)) {
                board = setSquare(playerToMove, board, positionToMove);
            }
        }
        return board;
    }

    /**
	 * The function sets a player's mark on a specific position of a 2D board and returns the updated
	 * board.
	 * 
	 * @param player an integer representing the player (either 1 or 2) who is making a move on the
	 * board.
	 * @param board a 2D integer array representing the game board
	 * @param position The parameter "position" is an object of the class "Position" which contains the
	 * row and column values of a position on a 2D board. It is used to specify the location where the
	 * player wants to place their move on the board.
	 * @return The method is returning a 2D integer array representing the updated game board after
	 * setting the square at the specified position to the player's value.
	 */
	public static int[][] setSquare(int player, int[][] board, Position position) {
        board[position.getRow()][position.getCol()] = player;
        return board;
    }

    /**
	 * The function checks if a player can make a legal move on a given board at a given position.
	 * 
	 * @param board a 2D array representing the current state of the game board
	 * @param playerToMove The player who is making the move. It is usually represented by an integer
	 * value, such as 1 or 2.
	 * @param positionToMove The position on the board where the player wants to make a move. It is
	 * represented as a Position object, which typically contains two integer values - row and column.
	 * @return The method `canPlay` is returning a boolean value. It is returning the result of the
	 * method `isLegalMove` which takes in the current state of the game board, the player who wants to
	 * make a move, and the position they want to move to. The `isLegalMove` method checks if the move
	 * is legal and returns true if it is, and false otherwise. Therefore,
	 */
	public static boolean canPlay(int[][] board, int playerToMove, Position positionToMove) {
        return isLegalMove(board, playerToMove, positionToMove);
    }
}
