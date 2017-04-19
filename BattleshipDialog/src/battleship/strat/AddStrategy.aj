package battleship.strat;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;


import battleship.BattleshipDialog;
import battleship.BoardPanel;
import battleship.model.Board;
import battleship.model.Ship;
import battleship.model.Place;

privileged public aspect AddStrategy {
	//reference to main board
	BattleshipDialog mainDialog = null;
	//member variables
	SmartStrategy strat;
	Board board;
	JDialog player;
	BoardPanel boardPanel;
	int size = 10;
	
	pointcut constructor(): execution(BattleshipDialog.new(*));
	pointcut draw(): execution(JPanel BattleshipDialog.makeControlPane());
	//pointcut places(): execution(void BoardPanel.drawPlaces(*));
	pointcut hit(): execution(void BoardPanel.placeClicked(Place));
	pointcut places(): execution(void drawPlaces(Graphics));
	after(Place x): hit() && args(x) {
		if(this.player != null){
			respondHit();
			//TODO: bug here not going into if
			if(!x.battleBoard.isGameOver() && !x.isHit())
				respondHit();
		}
	}
	after(BattleshipDialog a): constructor() && target(a){
		this.mainDialog = a;
		a.playButton.setText("Practice");
	}
	after(Graphics g, BoardPanel a): places() && args(g) && target(a){
		if(a.getParent() != this.mainDialog){
			final Color oldColor = g.getColor();
		    for (Place p: a.board.places()) {
				if (p.isHit()) {
				    int x = a.leftMargin + (p.getX() - 1) * a.placeSize;
				    int y = a.topMargin + (p.getY() - 1) * a.placeSize;
				    g.setColor(p.isEmpty() ? a.missColor : a.hitColor);
				    g.fillRect(x + 1, y + 1, a.placeSize - 1, a.placeSize - 1);
		            if (p.hasShip() && p.ship().isSunk()) {
		                g.setColor(Color.BLACK);
		                g.drawLine(x + 1, y + 1,
		                        x + a.placeSize - 1, y + a.placeSize - 1);
		                g.drawLine(x + 1, y + a.placeSize - 1,
		                        x + a.placeSize - 1, y + 1);
		            }
				}
				else if(p.hasShip()){
					int x = a.leftMargin + (p.getX() - 1) * a.placeSize;
				    int y = a.topMargin + (p.getY() - 1) * a.placeSize;
			    	g.setColor(Color.yellow);
			    	g.fillRect(x + 1, y + 1, a.placeSize - 1, a.placeSize - 1);
			    }
		    }
		    g.setColor(oldColor);
		}
	}
	
	JPanel around(BattleshipDialog a): draw() && target(a){
		JPanel content = new JPanel(new BorderLayout());
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttons.setBorder(BorderFactory.createEmptyBorder(0,5,0,0));
        JButton playButton = new JButton("Play");
        buttons.add(a.playButton);
        buttons.add(playButton);
        playButton.setFocusPainted(false);
        playButton.addActionListener(this::createPlayerWindow);
        a.playButton.setFocusPainted(false);
        a.playButton.addActionListener(a::playButtonClicked);
        content.add(buttons, BorderLayout.NORTH);
        a.msgBar.setBorder(BorderFactory.createEmptyBorder(5,10,0,0));
        content.add(a.msgBar, BorderLayout.SOUTH);
        return content;
	}
	public void createPlayerWindow(ActionEvent event){
		//initialize variables
		this.mainDialog.msgBar.setText("Shots: 0");
		this.mainDialog.board.reset();
		this.mainDialog.placeShips();
		this.mainDialog.repaint();
		this.board = new Board(this.size);
		this.strat = new SmartStrategy(this.size);
		this.player = new JDialog();
		this.boardPanel = new BoardPanel(board);
		
		player.setLayout(new BorderLayout());
		player.add(boardPanel, BorderLayout.CENTER);
		player.setSize(new Dimension(335, 440));
		player.setTitle("Player Board");
		int size = board.size();
		Random random = new Random();
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
		player.setVisible(true);
        player.setDefaultCloseOperation(2);
	}
	public  void respondHit(){
		int shot = strat.checkShot();
		Place place = board.at((shot/10)+1, (shot%10)+1);
		if(!place.isHit()){
			board.hit(place);
			strat.doShot();
			if(place.isHitShip())
				if(place.ship().isSunk())
					strat.notifySunk();
				else
					strat.notifyHit(shot);
			this.boardPanel.repaint();
		}
	}
}
