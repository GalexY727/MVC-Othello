package game;

import com.mrjaffesclass.apcs.messenger.MessageHandler;
import com.mrjaffesclass.apcs.messenger.Messenger;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class GamePanel extends JPanel implements MessageHandler {

    //reversi board
    int[][] board;

    //player turn
    //black plays first
    int turn = 1;

    //swing elements
    BoardCell[][] cells;
    JLabel score1;
    JLabel score2;

    int totalscore1 = 0;
    int totalscore2 = 0;

    JLabel tscore1;
    JLabel tscore2;

    private final Messenger mvcMessaging;
	private MessagePayload messagePayload = new MessagePayload(null, null);

    /**
	 * The function returns the value of a specific cell in a 2D board.
	 * 
	 * @param i The row index of the cell in the board for which we want to get the value.
	 * @param j The parameter "j" represents the column index of the 2D array "board". It is used to
	 * access the value of a specific element in the column "j" of the row "i" in the array.
	 * @return The method `getBoardValue` is returning the value of the element at position `(i,j)` in
	 * the `board` array.
	 */
	public int getBoardValue(int i,int j){
        return board[i][j];
    }

    /**
	 * This function sets the value of a specific cell in a 2D board.
	 * 
	 * @param i The row index of the cell in the board where the value is being set.
	 * @param j The parameter "j" represents the column index of the cell in the 2D array "board" where
	 * the value is to be set.
	 * @param value The value to be set at the specified position (i,j) in the board.
	 */
	public void setBoardValue(int i,int j,int value){
        board[i][j] = value;
    }

    public GamePanel(Messenger messages){

        mvcMessaging = messages;

        this.setBackground(Color.WHITE);
        this.setLayout(new BorderLayout());

        JPanel reversiBoard = new JPanel();
        reversiBoard.setLayout(new GridLayout(8,8));
        reversiBoard.setPreferredSize(new Dimension(500,500));
        reversiBoard.setBackground(new Color(41,100, 59));

        //init board
        resetBoard();

        // This code is creating a 2D array of `BoardCell` objects with dimensions 8x8 and initializing
		// each element with a new `BoardCell` object. The `BoardCell` constructor takes four
		// arguments: `this` (referring to the current `GamePanel` object), `reversiBoard` (a `JPanel`
		// object), and the row and column indices of the current element in the 2D array. The
		// `reversiBoard` panel is then adding each `BoardCell` object to itself. This code is
		// essentially creating the game board by populating it with `BoardCell` objects.
		cells = new BoardCell[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                cells[i][j] = new BoardCell(this,reversiBoard,i,j);
                reversiBoard.add(cells[i][j]);
            }
        }


        // This code is creating a panel (`sidebar`) that will be added to the left side of the main
		// panel (`this`) using a `BorderLayout`. The `sidebar` panel has a vertical box layout
		// (`BoxLayout.Y_AXIS`) and a preferred size of 200 pixels wide and 0 pixels tall.
		JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar,BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(200,0));

        score1 = new JLabel("Score 1");
        score2 = new JLabel("Score 2");

        tscore1 = new JLabel("Total Score 1");
        tscore2 = new JLabel("Total Score 2");

        sidebar.add(score1);
        sidebar.add(score2);

        sidebar.add(new JLabel("-----------"));

        sidebar.add(tscore1);
        sidebar.add(tscore2);


        this.add(sidebar,BorderLayout.WEST);
        this.add(reversiBoard);

        // These lines of code are subscribing the `GamePanel` object to various message channels using
		// the `Messenger` object `mvcMessaging`. This allows the `GamePanel` object to receive
		// messages from other components of the program that are also subscribed to these channels.
		// The specific channels being subscribed to are `"view:buttonClicked"`, `"view:boardUpdate"`,
		// `"move:player"`, `"move:player:waitForClick"`, `"move:player1"`, `"move:player2"`, and
		// `"game:finished"`.
		mvcMessaging.subscribe("view:buttonClicked", this);
        mvcMessaging.subscribe("view:boardUpdate", this);
		mvcMessaging.subscribe("move:player", this);
		mvcMessaging.subscribe("move:player:waitForClick", this);
        mvcMessaging.subscribe("move:player1", this);
        mvcMessaging.subscribe("move:player2", this);
        mvcMessaging.subscribe("game:finished", this);

        updateBoardInfoCmd();
        updateTotalScoreCmd();


        manageTurn();
    }

    // `private boolean awaitForClick = false;` is initializing a boolean variable `awaitForClick` to
	// `false`. This variable is used to determine whether the program is currently waiting for the
	// user to click on a cell in the game board. If `awaitForClick` is `true`, it means that the
	// program is waiting for the user to click on a cell, and if it is `false`, it means that the
	// program is not waiting for the user to click on a cell.
	private boolean awaitForClick = false;

	/**
	 * The function checks if there are any moves available for both players and returns true if there are
	 * no moves available for either player, indicating that the game has finished.
	 * 
	 * @return The method is returning a boolean value, either true or false, depending on whether the
	 * game is finished or not. If there are any moves available for player 1 or player 2, the method
	 * returns false, indicating that the game is not finished. Otherwise, if there are no moves available
	 * for either player, the method returns true, indicating that the game is finished.
	 */
	private boolean gameFinished(){
		if (BoardHelper.anyMovesAvailable(board, 1) || 
			BoardHelper.anyMovesAvailable(board, 2)){
			return false;
		} else {
			return true;
		}
	}

    /**
	 * This function manages the turn of the players in a game and notifies the view and messaging
	 * system accordingly.
	 */
	public void manageTurn(){
        if(!gameFinished()) {

			mvcMessaging.notify("view:boardUpdate", messagePayload.createMessagePayload("Board Updated"), true);

            if (turn == 1) {
                // This code block is checking if there are any legal moves available for player 1. If
				// there are, it sends a message to the messaging system to wait for the player to
				// click on a cell to make a move. If there are no legal moves available for player 1,
				// it sends a message to the messaging system to move to the next player's turn.
				if(BoardHelper.anyMovesAvailable(board,1)) {
					mvcMessaging.notify("move:player:waitForClick", messagePayload.createMessagePayload("Player 1 Turn"), true);
                }else{
                    mvcMessaging.notify("move:player1", messagePayload.createMessagePayload("Player 1 has no legal moves !"), true);
                }
            } else {
                // The above code is checking if there are any legal moves available for player 2 on
				// the game board. If there are, it sends a notification to wait for player 1's click
				// and start player 1's turn. If there are no legal moves available for player 2, it
				// sends a notification that player 2 has no legal moves.
				if(BoardHelper.anyMovesAvailable(board, 2)) {
					mvcMessaging.notify("move:player:waitForClick", messagePayload.createMessagePayload("Player 1 Turn"), true);
                }else{
                    mvcMessaging.notify("move:player2", messagePayload.createMessagePayload("Player 2 has no legal moves !"), true);
                }
            }
        }else{
            mvcMessaging.notify("game:finished", messagePayload.createMessagePayload("Game Finished !"), true);
        }
    }

	/**
	 * This function sets a boolean variable to true, indicating that the player is waiting for a click
	 * command.
	 */
	private void playerWaitForClickCmd(){
		awaitForClick = true;
	}

	/**
	 * This function sets the turn to player 2 and manages the turn.
	 */
	private void movePlayer1Cmd(){
		turn = 2;
		manageTurn();
	}
	/**
	 * This function sets the turn to player 1 and manages the turn.
	 */
	private void movePlayer2Cmd(){
		turn = 1;
		manageTurn();
	}
	
    /**
	 * The function resets the board to its initial state with two players and few pieces on the board.
	 */
	public void resetBoard(){
        board = new int[8][8];
        for (int i = 0; i < 8; i++) {
            Arrays.fill(board[i], 0);
        }
        //initial board state
        setBoardValue(3,3,2);
        setBoardValue(3,4,1);
        setBoardValue(4,3,1);
        setBoardValue(4,4,2);
    }    
	
    /**
	 * The function handles a click event on a game board and notifies the appropriate player and view
	 * of the move made.
	 * 
	 * @param i The row index of the button that was clicked on the game board.
	 * @param j The j parameter represents the column index of the button that was clicked on the game
	 * board.
	 */
	public void handleClick(int i, int j){
        if(awaitForClick && BoardHelper.canPlay(board, turn, new Position(i, j))){
			if(turn == 1){
				mvcMessaging.notify("move:player", messagePayload.createMessagePayload("Player 1 Made Move : "+ i + " , " + j, new Position(i, j)), true);
				mvcMessaging.notify("view:buttonClicked", messagePayload.createMessagePayload(("Player 1 ") + "Placed Piece : "+ i + " , " + j, new Position(i, j)), true);

			}else{
				mvcMessaging.notify("move:player", messagePayload.createMessagePayload("Player 2 Made Move : "+ i + " , " + j, new Position(i, j)), true);
				mvcMessaging.notify("view:buttonClicked", messagePayload.createMessagePayload(("Player 2 ") + "Placed Piece : "+ i + " , " + j, new Position(i, j)), true);
			}
		}
    }

	/**
	 * The function returns an array of two integers representing the scores of two players in a game,
	 * calculated based on the contents of a game board and the current turn.
	 * 
	 * @param p1score The current score of player 1.
	 * @param p2score The current score of player 2.
	 * @return An integer array containing the scores of two players.
	 */
	private int[] getPlayerScoresCmd(int p1score, int p2score){
		int[] scores = new int[2];
		for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == 1) p1score++;
                if (board[i][j] == 2) p2score++;

                if (BoardHelper.canPlay(board, turn, new Position(i, j))) {
                    cells[i][j].highlight = 1;
                } else {
                    cells[i][j].highlight = 0;
                }
            }
        }
		scores[0] = p1score;
		scores[1] = p2score;
		return scores;
	}

	/**
	 * The function updates the board, advances the turn, repaints the screen, and calls the manageTurn()
	 * function.
	 * 
	 * @param position The position parameter is an object of the Position class, which represents the
	 * position on the game board where the player has made their move. It contains information such as
	 * the row and column of the position.
	 */
	private void makeMoveCmd(Position position) {
			//update board
            board = BoardHelper.makeMove(turn, board, position);

            //advance turn
            turn = (turn == 1) ? 2 : 1;

            repaint();

            awaitForClick = false;

            //callback
            manageTurn();
	}
	
	/**
	 * The function updates the total score of two players in a game and displays their status (winning,
	 * losing, or tied) based on their scores.
	 */
	private void updateTotalScoreCmd(){
        tscore1.setText("Player 1: Black = " + totalscore1 +
                ((totalscore1 == totalscore2) ? " (Tied)" :
                        (totalscore1 > totalscore2) ? " (Winning)" :
                                " (Losing)" ));

        tscore2.setText("Player 2: White = " + totalscore2 +
                ((totalscore2 == totalscore1) ? " (Tied) " :
                        (totalscore2 > totalscore1) ? " (Winning)" :
                                " (Losing)" ));
    }
	
	/**
	 * The function updates the scores of two players in a board game.
	 */
	private void updateBoardInfoCmd() {

        int p1score = 0;
        int p2score = 0;

        p1score = getPlayerScoresCmd(p1score, p2score)[0];
		p2score = getPlayerScoresCmd(p1score, p2score)[1];

        score1.setText("Player 1: Black = " + p1score);
        score2.setText("Player 2: White = " + p2score);

    }

	/**
	 * The function updates the total score of the players, resets the board, and manages the turn after
	 * the game is finished.
	 */
	private void gameFinishedCmd(){
		int winner = BoardHelper.getWinner(board);
		if(winner==1) totalscore1++;
		else if(winner==2) totalscore2++;
		updateTotalScoreCmd();
		//restart
		resetBoard();
		turn=1;
		manageTurn();
	}

    /**
	 * This function handles different types of messages and executes corresponding commands based on
	 * the message name and payload.
	 * 
	 * @param messageName A string representing the name of the message being handled.
	 * @param messagePayload An object that contains the message payload, which includes the message
	 * and position information.
	 */
	@Override
    public void messageHandler(String messageName, Object messagePayload) {
		assert messageName != null : "Message name cannot be null";
		
		MessagePayload payload = (MessagePayload) messagePayload;
		String message = payload.getMessage();
		Position position = payload.getPosition();

		if (position == null) {
			switch (messageName){
				case "game:finished" -> gameFinishedCmd();
				case "view:boardUpdate" -> updateBoardInfoCmd();
				case "move:player:waitForClick" -> playerWaitForClickCmd();
			}
		}else {
			switch (messageName){
				case "view:buttonClicked" -> makeMoveCmd(position);
				case "move:player1" -> movePlayer1Cmd();
				case "move:player2" -> movePlayer2Cmd();
			}
		}
		
		System.out.println("\t"+message+"\n");
    }
}
