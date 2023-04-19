package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;

public class BoardCell extends JLabel implements MouseListener{

    int i;
    int j;

    GamePanel ge;
    JPanel parent;

    public int highlight = 0;

    public String text = "";

    public BoardCell(GamePanel ge ,JPanel parent,int i,int j){
        this.ge = ge;
        this.parent = parent;
        this.i = i;
        this.j = j;
        this.addMouseListener(this);
    }

    /**
	 * The `paint` function of the `BoardCell` class is responsible for drawing the cell on the game
	 * board, including highlighting, borders, and game pieces, as well as displaying any text in the
	 * center of the cell.
	 * 
	 * @param g The Graphics object used for painting the BoardCell.
	 */
	@Override
    public void paint(Graphics g) {

        int margin_left = this.getWidth() / 10;
        int margin_top = this.getHeight() / 10;


		// This code block is checking if the `highlight` variable of the `BoardCell` object is equal to 1.
		// If it is, it sets the color of the graphics object `g` to a lime color, fills a rectangle with the
		// lime color that covers the entire `BoardCell` object, and then sets the color of the graphics
		// object to the background color of the parent panel. It then fills another rectangle with the
		// background color of the parent panel, leaving a margin of 4 pixels on each side of the `BoardCell`
		// object. This code is responsible for highlighting the current cell on the game board.
		if(highlight == 1) {
			Color lime = new Color(138, 177, 62);
            g.setColor(lime);

			g.fillRect(0,0,this.getWidth(),this.getHeight());
            g.setColor(parent.getBackground());
            g.fillRect(4,4,this.getWidth()-8,this.getHeight()-8);
        }

		
        // This code is setting the color of the graphics object `g` to black and drawing a rectangle
		// with a black outline around the `BoardCell` object. The rectangle is drawn starting from the
		// top-left corner of the `BoardCell` object (coordinates 0,0) and has a width and height equal
		// to the width and height of the `BoardCell` object (`this.getWidth()` and
		// `this.getHeight()`). This code is responsible for drawing the border around each cell on the
		// game board.
		g.setColor(Color.BLACK);
        g.drawRect(0,0,this.getWidth(),this.getHeight());

		
        // This code block is checking the value of the current cell on the game board by calling the
		// `getBoardValue` method of the `ge` object (which is an instance of the `GamePanel` class)
		// with the current cell's `i` and `j` coordinates. If the value is 1, it sets the color of the
		// graphics object to black and fills an oval shape in the center of the current `BoardCell`
		// object. If the value is 2, it sets the color of the graphics object to white and fills an
		// oval shape in the center of the current `BoardCell` object. This code is responsible for
		// drawing the game pieces on the board.
		int value = ge.getBoardValue(i,j);
        if(value == 1){
            g.setColor(Color.BLACK);
            g.fillOval(margin_left,margin_top,this.getWidth()-2*margin_left,this.getHeight()-2*margin_top);
        }
        else if(value == 2) {
            g.setColor(Color.WHITE);
            g.fillOval(margin_left,margin_top,this.getWidth()-2*margin_left,this.getHeight()-2*margin_top);
        }

        // This code block is checking if the `text` variable is not empty. If it is not empty, it sets
		// the color of the graphics object to yellow, creates a new font with size 30, and sets the
		// graphics object's font to the new font. Then, it calls the `drawStringInCenterOfRectangle`
		// method to draw the `text` variable in the center of the current `BoardCell` object.
		if(!text.isEmpty()){
            g.setColor(new Color(255, 255, 0));
            Font font = g.getFont();
            Font nfont = new Font(font.getName(),Font.PLAIN,30);
            g.setFont(nfont);

            drawStringInCenterOfRectangle(g,0,0,this.getWidth(),this.getHeight(),text);
        }

		// `super.paint(g)` is calling the `paint` method of the parent class of `BoardCell`, which is
		// `JLabel`. This is necessary to ensure that the label is properly painted with its text and other
		// properties. If this line of code is not included, the label may not be displayed correctly.
		super.paint(g);
    }


	/**
	 * This function draws a given string in the center of a given rectangle using the provided graphics
	 * object.
	 * 
	 * @param g The Graphics object used for drawing.
	 * @param x The x-coordinate of the top-left corner of the rectangle where the text will be centered.
	 * @param y The y-coordinate of the top-left corner of the rectangle in which the text will be
	 * centered.
	 * @param w The width of the rectangle.
	 * @param h The height of the rectangle in which the text will be centered.
	 * @param text The string of text that you want to draw in the center of the rectangle.
	 */
	public void drawStringInCenterOfRectangle(Graphics g,int x,int y,int w,int h,String text){
        Graphics2D g2 = (Graphics2D) g;
        Font bfont = g2.getFont();
        FontRenderContext context = g2.getFontRenderContext();
        g2.setFont(bfont);
        int textWidth = (int) bfont.getStringBounds(text, context).getWidth();
        LineMetrics ln = bfont.getLineMetrics(text, context);
        int textHeight = (int) (ln.getAscent() + ln.getDescent());
        int tx = x+(w - textWidth)/2;
        int ty = (int)((y + h + textHeight)/2 - ln.getDescent());
        g2.drawString(text, tx, ty);
    }

    /**
	 * This is an empty implementation of the mouseClicked method from the MouseListener interface in
	 * Java.
	 * 
	 * @param e The parameter "e" in the above code refers to the MouseEvent object that contains
	 * information about the mouse event that occurred, such as the location of the mouse click, the
	 * type of mouse button that was pressed, and any modifiers keys that were held down during the
	 * click. The mouseClicked() method is called
	 */
	@Override
    public void mouseClicked(MouseEvent e) {

    }

    /**
	 * This function handles a mouse press event and calls a method to handle a click on a specific
	 * location.
	 * 
	 * @param e The parameter "e" is an object of the MouseEvent class, which represents a mouse event
	 * that occurred, such as a mouse button being pressed or released. It contains information about
	 * the event, such as the location of the mouse pointer and which button was pressed.
	 */
	@Override
    public void mousePressed(MouseEvent e) {
        ge.handleClick(i,j);
    }

    /**
	 * This is an empty implementation of the mouseReleased method in Java, which is called when a
	 * mouse button is released.
	 * 
	 * @param e The parameter "e" in the method signature represents the MouseEvent object that
	 * contains information about the mouse event that occurred, such as the location of the mouse
	 * cursor and the type of mouse button that was released. The method body is empty, so it does not
	 * perform any specific action when the mouse button is
	 */
	@Override
    public void mouseReleased(MouseEvent e) {

    }

    /**
	 * This is an empty method that gets called when the mouse enters a component.
	 * 
	 * @param e The parameter "e" in the method signature represents the MouseEvent object that
	 * contains information about the mouse event that occurred, such as the location of the mouse
	 * cursor and the type of mouse event (e.g. mouse clicked, mouse dragged, etc.). The method body
	 * can use this information to perform specific actions
	 */
	@Override
    public void mouseEntered(MouseEvent e) {

    }

    /**
	 * This is an empty method that gets called when the mouse exits a component.
	 * 
	 * @param e The parameter "e" in the above code is an object of the MouseEvent class. It represents
	 * the event that occurred when the mouse exited a component. This object contains information
	 * about the event, such as the source of the event, the location of the mouse pointer, and the
	 * type of mouse event that
	 */
	@Override
    public void mouseExited(MouseEvent e) {

    }
}