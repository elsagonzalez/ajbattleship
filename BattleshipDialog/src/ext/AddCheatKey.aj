package ext;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

import battleship.BoardPanel;
import battleship.model.Board;
import battleship.model.Place;

public aspect AddCheatKey {

	public static boolean showingBoats = false;
	protected Board board;
	protected int topMargin;
	protected int leftMargin;
	protected int placeSize;
	protected Color boardColor;
	protected Color hitColor;
	protected Color missColor;
	protected Color boatColor = Color.green;
	
	after(Board b, int tMargin, int lMargin, int pSize, Color bColor, Color hColor, Color mColor, BoardPanel p): execution(BoardPanel(*)) && args(b, tMargin , lMargin,  pSize, bColor, hColor, mColor) && target(p){
		ActionMap actionMap = getActionMap();
		int condition = JComponent.WHEN_IN_FOCUSED_WINDOW;
		InputMap inputMap = getInputMap(condition);
		String cheat = "Cheat";
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0), cheat);
		actionMap.put(cheat, new KeyAction(p, cheat));
		
		board = b;
		topMargin = tMargin;
		leftMargin = lMargin;
		placeSize = pSize;
		boardColor = bColor;
		hitColor = hColor;
		missColor = mColor;
	}
	
	void around(Graphics g): execution(void drawPlaces(Graphics)) && args(g){
		if(showingBoats){
			final Color oldColor = g.getColor();
	        for (Place p: board.places()) {
	    		if (p.isHit()) {
	    		    int x = leftMargin + (p.getX() - 1) * placeSize;
	    		    int y = topMargin + (p.getY() - 1) * placeSize;
	    		    g.setColor(p.isEmpty() ? missColor : hitColor);
	    		    g.fillRect(x + 1, y + 1, placeSize - 1, placeSize - 1);
	                if (p.hasShip() && p.ship().isSunk()) {
	                    g.setColor(Color.BLACK);
	                    g.drawLine(x + 1, y + 1,
	                            x + placeSize - 1, y + placeSize - 1);
	                    g.drawLine(x + 1, y + placeSize - 1,
	                            x + placeSize - 1, y + 1);
	                }
	                else if(p.hasShip()){
	                	g.setColor(boatColor);
	                	g.drawLine(x + 1, y + 1,
	                            x + placeSize - 1, y + placeSize - 1);
	                    g.drawLine(x + 1, y + placeSize - 1,
	                            x + placeSize - 1, y + 1);
	                }
	    		}
	        }
	        g.setColor(oldColor);
		}
		else{
			proceed(g);
		}
	}
	
	public static void toggleCheat(){
		if(showingBoats){
			showingBoats = false;
		}
		else{
			showingBoats = true;
		}
	}
	
	@SuppressWarnings("serial")
    private static class KeyAction extends AbstractAction {
       private final BoardPanel boardPanel;
       
       public KeyAction(BoardPanel boardPanel, String command) {
           this.boardPanel = boardPanel;
           putValue(ACTION_COMMAND_KEY, command);
       }
       
       /** Called when a cheat is requested. */
       public void actionPerformed(ActionEvent event) {
           toggleCheat();
       }   
    }
	
}