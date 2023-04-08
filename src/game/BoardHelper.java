package game;

public class BoardHelper {

    public static boolean isGameFinished(int[][] board) {
        return !(anyMovesAvailable(board,1) || anyMovesAvailable(board,2));
    }


    public static int getWinner(int[][] board) {
        if (!isGameFinished(board))
            //game not finished
            return -1;
        else {
            //count stones
            int p1s = getPlayerStoneCount(board, 1);
            int p2s = getPlayerStoneCount(board, 2);

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

    public static int getPlayerStoneCount(int[][] board, int player) {
        int score = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == player) score++;
            }
        }
        return score;
    }


    public static int getSquare(int[][] board, Position position) {
        return board[position.getRow()][position.getCol()];
    }

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

    protected static boolean step(int player, int[][] board, Position position, Position direction, int count) {
        Position newPosition = position.translate(direction);
        int oplayer = ((player == 1) ? 2 : 1);

        if (newPosition.isOffBoard()) {
            // If off the board then move is not legal
            return false;
        } else if ((getSquare(board, newPosition) == 0) && (count == 0)) {
            // If empty space AND adjacent to position then not legal
            return false;
        } else if (getSquare(board, newPosition) == oplayer && getSquare(board, newPosition) != 0) {
            // If space has opposing player then move to next space in same direction
            return step(player, board, newPosition, direction, count+1);
        } else if (getSquare(board, newPosition) == player) {
            // If space has this player and we've moved more than one space, then it's legal,
            // otherwise it's not legal
            return count > 0;
        } else {
            // Didn't pass any other test, not legal move
            return false;
        }
    }

    private static boolean makeMoveStep(int player, int[][] board, Position position, Position direction, int count) {
        Position newPosition = position.translate(direction);
        int oplayer = ((player == 1) ? 2 : 1);

        if (newPosition.isOffBoard()) {
            return false;
        } else if (getSquare(board, newPosition) == oplayer) {
            boolean valid = makeMoveStep(player, board, newPosition, direction, count+1);
            if (valid) {
                setSquare(player, board, newPosition);
            }
            return valid;
        } else if (getSquare(board, newPosition) == player) {
            return count > 0;
        } else {
            return false;
        }
    }

    /**
     * Make the move.  Scan all directions and switch the piece colors
     * of the ones as appropriate
     * @param playerToMove Player asking
     * @param positionToMove Position of the new move
     */
    public static int[][] makeMove(int playerToMove, int[][] board, Position positionToMove) {
        for (String direction : Directions.getDirections()) {
            Position directionVector = Directions.getVector(direction);
            if (makeMoveStep(playerToMove, board, positionToMove, directionVector, 0)) {
                board = setSquare(playerToMove, board, positionToMove);
//      } else {
//        System.out.println("**** THIS SPACE IS NOT A VALID MOVE. YOU LOSE!");
            }
        }
        return board;
    }

    public static int[][] setSquare(int player, int[][] board, Position position) {
        board[position.getRow()][position.getCol()] = player;
        return board;
    }

    public static boolean canPlay(int[][] board, int playerToMove, Position positionToMove) {
        return isLegalMove(board, playerToMove, positionToMove);
    }
}
