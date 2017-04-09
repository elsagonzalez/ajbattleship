package battleship;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import battleship.model.Board;
import battleship.model.Ship;
import battleship.model.Place;
import battleship.BoardPanel;

/**
 *  Simple dialog to shoot ships placed in a battleship game board.
 */
@SuppressWarnings("serial")
public class BattleshipDialog extends JDialog {

    /** Default dimension of the dialog. */
    private final static Dimension DEFAULT_DIMENSION = new Dimension(335, 440);

    /** To place ships randomly. */
    private final static Random random = new Random();

    /** To start a new game. */
    private final JButton playButton = new JButton("Play");

    /** Message bar to display the number of shots and the outcome. */
    private final JLabel msgBar = new JLabel("Shots: 0");
    
    /** Battleship game model. */
    private Board board;
    
    /** Create a battleship dialog. */
    public BattleshipDialog() {
    	this(DEFAULT_DIMENSION);
    }
    
    /** Create a battleship dialog of the given dimension. */
    public BattleshipDialog(Dimension dim) {
        super((JFrame) null, "Battleship");
        board = new Board(10);
        board.addBoardChangeListener(createBoardChangeListener());
        placeShips();
        configureGui();
        setSize(dim);
        //setResizable(false);
        setLocationRelativeTo(null);
    }
	
    /** Configure UI. */
    private void configureGui() {
        setLayout(new BorderLayout());
        add(makeControlPane(), BorderLayout.NORTH);
        add(makeBoardPane(), BorderLayout.CENTER);
    }
    
    /** Create a control panel consisting of a play button and
     * a message bar. */
    private JPanel makeControlPane() {
    	JPanel content = new JPanel(new BorderLayout());
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttons.setBorder(BorderFactory.createEmptyBorder(0,5,0,0));
        buttons.add(playButton);
        playButton.setFocusPainted(false);
        playButton.addActionListener(this::playButtonClicked);
        content.add(buttons, BorderLayout.NORTH);
        msgBar.setBorder(BorderFactory.createEmptyBorder(5,10,0,0));
        content.add(msgBar, BorderLayout.SOUTH);
        return content;
    }
    
    /** Create a panel for a game board. */
    private JPanel makeBoardPane() {
    	return new BoardPanel(board);
    }
    
    /** Place ships randomly. */
    private void placeShips() {
        int size = board.size();
        for (Ship ship : board.ships()) {
            int i = 0;
            int j = 0;
            boolean dir = false;
            do {
                i = random.nextInt(size) + 1;
                j = random.nextInt(size) + 1;
                dir = random.nextBoolean();
            } while (!board.placeShip(ship, i, j, dir));
        }
    }
    
    /** To be called when the play button is clicked. If the current play
     * is over, start a new play; otherwise, prompt the user for
     * confirmation and then proceed accordingly. */
    private void playButtonClicked(ActionEvent event) {
        if (isGameOver()) {
            startNewGame();
        } else {
            if (JOptionPane.showConfirmDialog(BattleshipDialog.this, 
                "Play a new game?", "Battleship", JOptionPane.YES_NO_OPTION)
                == JOptionPane.YES_OPTION) {
                startNewGame();
            }
        }
    }
    
    /** Is the current play over? */
    private boolean isGameOver() {
        return board.isGameOver();
    }
        
    /** Start a new game. This will terminate the current play and
     * start a new play. */
    private void startNewGame() {
    	msgBar.setText("Shots: 0");
        board.reset();
        placeShips();
        repaint();
    }
    
    /** Create a listener to listen to board changes such as shots.
     * The created listener will update the number of shots and
     * congratulate when the game is over. */
    private Board.BoardChangeListener createBoardChangeListener() {
    	return new Board.BoardChangeAdapter() {
            public void hit(Place place, int numOfShots) {
                showMessage("Shots: " + numOfShots);
            }

            public void gameOver(int numOfShots) {
                showMessage("All ships destroyed with " + numOfShots + " shots!");
            }
        }; 
    }
    
    /** Display the given string in the message bar. */
    private void showMessage(String msg) {
        msgBar.setText(msg);
    }
        
    public static void main(String[] args) {
        BattleshipDialog dialog = new BattleshipDialog();
        dialog.setVisible(true);
        dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
}
