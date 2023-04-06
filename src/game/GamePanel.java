package game;

import com.mrjaffesclass.apcs.messenger.MessageHandler;
import com.mrjaffesclass.apcs.messenger.Messenger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Arrays;

public class GamePanel extends JPanel implements GameEngine, MessageHandler {

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


    GamePlayer player1 = new HumanPlayer(1);
    GamePlayer player2 = new HumanPlayer(2);

    Timer player1HandlerTimer;
    Timer player2HandlerTimer;
    private final Messenger mvcMessaging;

    @Override
    public int getBoardValue(int i,int j){
        return board[i][j];
    }

    @Override
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

        //
        updateBoardInfo();
        updateTotalScore();

        //AI Handler Timer (to unfreeze gui)
        player1HandlerTimer = new Timer(1000,(ActionEvent e) -> {
            handlePlayer(player1);
            player1HandlerTimer.stop();
            manageTurn();
        });

        player2HandlerTimer = new Timer(1000,(ActionEvent e) -> {
            handlePlayer(player2);
            player2HandlerTimer.stop();
            manageTurn();
        });

        manageTurn();
    }

    private boolean awaitForClick = false;

    public void manageTurn(){
        if(BoardHelper.hasAnyMoves(board,1) || BoardHelper.hasAnyMoves(board,2)) {
            updateBoardInfo();
            if (turn == 1) {
                if(BoardHelper.hasAnyMoves(board,1)) {
                    if (player1.isUserPlayer()) {
                        awaitForClick = true;
                        //after click this function should be call backed
                    } else {
                        player1HandlerTimer.start();
                    }
                }else{
                    //forfeit this move and pass the turn
                    mvcMessaging.notify("move:player", "Player 1 has no legal moves !");
                    turn = 2;
                    manageTurn();
                }
            } else {
                if(BoardHelper.hasAnyMoves(board,2)) {
                    if (player2.isUserPlayer()) {
                        awaitForClick = true;
                        //after click this function should be call backed
                    } else {
                        player2HandlerTimer.start();
                    }
                }else{
                    //forfeit this move and pass the turn
                    mvcMessaging.notify("move:player", "Player 2 has no legal moves !");
                    turn = 1;
                    manageTurn();
                }
            }
        }else{
            //game finished
            mvcMessaging.notify("game:finished", "Game Finished !");
            int winner = BoardHelper.getWinner(board);
            if(winner==1) totalscore1++;
            else if(winner==2) totalscore2++;
            updateTotalScore();
            //restart
            //resetBoard();
            //turn=1;
            //manageTurn();
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

    //update highlights on possible moves and scores
    public void updateBoardInfo(){

        int p1score = 0;
        int p2score = 0;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if(board[i][j] == 1) p1score++;
                if(board[i][j] == 2) p2score++;

                if(BoardHelper.canPlay(board,turn,i,j)){
                    cells[i][j].highlight = 1;
                }else{
                    cells[i][j].highlight = 0;
                }
            }
        }

        score1.setText(player1.playerName() + " : " + p1score);
        score2.setText(player2.playerName() + " : " + p2score);
        mvcMessaging.notify("view:boardUpdate", "Board Updated");

    }

    public void updateTotalScore(){
        tscore1.setText(player1.playerName() + " : " + totalscore1);
        tscore2.setText(player2.playerName() + " : " + totalscore2);
    }

    @Override
    public void handleClick(int i, int j){
        if(awaitForClick && BoardHelper.canPlay(board,turn,i,j)){
            mvcMessaging.notify("move:player",
                    (turn == 1) ?
                            ("Player 1 ") + "Made Move : "+ i + " , " + j :
                            ("Player 2 ") + "Made Move : "+ i + " , " + j);

            mvcMessaging.notify("view:buttonClicked",
                    (turn == 1) ?
                    ("Player 1 ") + "Placed Piece : "+ i + " , " + j :
                    ("Player 2 ") + "Placed Piece : "+ i + " , " + j);

            //update board
            board = BoardHelper.getNewBoardAfterMove(board,new Point(i,j),turn);

            //advance turn
            turn = (turn == 1) ? 2 : 1;

            repaint();

            awaitForClick = false;

            //callback
            manageTurn();
        }
    }

    private MessagePayload createPayload(int fieldNumber, int direction) {
        return new MessagePayload(fieldNumber, direction);
    }

    public void handlePlayer(GamePlayer gamePlayer){
        Point playerPlayPoint = gamePlayer.play(board);
        int i = playerPlayPoint.x;
        int j = playerPlayPoint.y;
        if(!BoardHelper.canPlay(board,gamePlayer.myMark,i,j)) {
            mvcMessaging.notify("move:player", "Player Played in Invalid Move : " + i + " , " + j);
        }

        //update board
        board = BoardHelper.getNewBoardAfterMove(board,playerPlayPoint,turn);

        //advance turn
        turn = (turn == 1) ? 2 : 1;

        repaint();
    }

    @Override
    public void messageHandler(String message, Object messagePayload) {
        // Remove the substring of "move:" or otherwise from the message
        String messageName = message.substring(message.indexOf(":")+1);
        String messageData = message.substring(0, message.indexOf(":"));

        if (messagePayload != null) {
            System.out.printf("MSG: received by %s: "+messageName+" | "+messagePayload+"\n", messageData);
        } else {
            System.out.printf("MSG: received by %s: "+messageName+" | No data sent", messageData);
        }
        assert messagePayload != null;
    }
}
