package game;

//interface that gui speaks to
public interface GameEngine {

    int getBoardValue(int i,int j);

    void setBoardValue(int i,int j,int value);

    void handleClick(int i,int j);

}
