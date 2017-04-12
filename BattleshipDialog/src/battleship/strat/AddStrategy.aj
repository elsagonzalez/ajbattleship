package battleship.strat;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
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
	pointcut constructor(): execution(BattleshipDialog.new(*));
	pointcut draw(): execution(JPanel BattleshipDialog.makeControlPane());
	pointcut hit(): execution(void BoardPanel.placeClicked(Place));
	after(Place x): hit() && args(x) {
		if(!x.battleBoard.isGameOver() && !x.isHit())
			respondHit();
	}
	after(BattleshipDialog a): constructor() && target(a){
		a.playButton.setText("Practice");
	}
	//member variables
	SmartStrategy strat;
	Board board;
	int size = 10;
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
		this.board = new Board(this.size);
		this.strat = new SmartStrategy(this.size);
		JDialog player = new JDialog();
		player.setLayout(new BorderLayout());
		player.add(new BoardPanel(board), BorderLayout.CENTER);
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
	public void respondHit(){
		int shot = strat.checkShot();
		Place place = board.at((shot/10)+1, (shot%10)+1);
		if(!place.isHit()){
			board.hit(place);
			strat.doShot();
			if(place.isHitShip())
				strat.notifyHit(shot);
		}
	}
}
