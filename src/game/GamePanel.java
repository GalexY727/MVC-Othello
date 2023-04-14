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

    public int getBoardValue(int i,int j){
        return board[i][j];
    }

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

        cells = new BoardCell[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                cells[i][j] = new BoardCell(this,reversiBoard,i,j);
                reversiBoard.add(cells[i][j]);
            }
        }


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

        mvcMessaging.subscribe("view:buttonClicked", this);
        mvcMessaging.subscribe("view:boardUpdate", this);
        mvcMessaging.subscribe("move:player", this);
        mvcMessaging.subscribe("game:finished", this);

        updateBoardInfoCmd();
        updateTotalScoreCmd();


        manageTurn();
    }

    private boolean awaitForClick = false;

	private boolean gameFinished(){
		if (BoardHelper.anyMovesAvailable(board, 1) || 
			BoardHelper.anyMovesAvailable(board, 2)){
			return false;
		} else {
			return true;
		}
	}

    public void manageTurn(){
        if(!gameFinished()) {

			mvcMessaging.notify("view:boardUpdate", messagePayload.createMessagePayload("Board Updated"), true);

            if (turn == 1) {
                if(BoardHelper.anyMovesAvailable(board,1)) {
                        awaitForClick = true;
                        //after click this function should be call backed
                }else{
                    //forfeit this move and pass the turn
                    mvcMessaging.notify("move:player", messagePayload.createMessagePayload("Player 1 has no legal moves !"), true);
                    turn = 2;
                    manageTurn();
                }
            } else {
                if(BoardHelper.anyMovesAvailable(board, 2)) {
                        awaitForClick = true;
                }else{
                    //forfeit this move and pass the turn
                    mvcMessaging.notify("move:player", messagePayload.createMessagePayload("Player 2 has no legal moves !"), true);
                    turn = 1;
                    manageTurn();
                }
            }
        }else{
            //game finished
            mvcMessaging.notify("game:finished", messagePayload.createMessagePayload("Game Finished !"), true);
        }
    }

	
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
	
    public void handleClick(int i, int j){
        if(awaitForClick && BoardHelper.canPlay(board, turn, new Position(i, j))){
            mvcMessaging.notify("move:player",
                    (turn == 1) ?
                            messagePayload.createMessagePayload(("Player 1 ") + "Made Move : "+ i + " , " + j,
																new Position(i, j)) :
                            messagePayload.createMessagePayload(("Player 2 ") + "Made Move : "+ i + " , " + j,
																new Position(i, j)), 
							true);

            mvcMessaging.notify("view:buttonClicked",
                    (turn == 1) ?
                    messagePayload.createMessagePayload(("Player 1 ") + "Placed Piece : "+ i + " , " + j,
														new Position(i, j)) :
                    messagePayload.createMessagePayload(("Player 2 ") + "Placed Piece : "+ i + " , " + j,
														new Position(i, j)), 
					true);
        }
    }

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
	
	private void updateBoardInfoCmd() {

        int p1score = 0;
        int p2score = 0;

        p1score = getPlayerScoresCmd(p1score, p2score)[0];
		p2score = getPlayerScoresCmd(p1score, p2score)[1];

        score1.setText("Player 1: Black = " + p1score);
        score2.setText("Player 2: White = " + p2score);

    }

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
			}
		}else {
			switch (messageName){
				case "view:buttonClicked" -> makeMoveCmd(position);
			}
		}
		
		System.out.println("\t"+message+"\n");
    }
}
