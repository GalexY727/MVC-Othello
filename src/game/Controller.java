package game;

import javax.swing.*;
import com.mrjaffesclass.apcs.messenger.*;

public class Controller extends JFrame {

    private final Messenger mvcMessaging;

    public Controller(){
        mvcMessaging = new Messenger();

        GamePanel gp = new GamePanel(mvcMessaging);
        this.add(gp);
    }

    public void init(){

        this.setTitle("Reversi v0.1");
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
    }

    public static void main(String[] args) {
        Controller app = new Controller();
        app.init();
    }

}
