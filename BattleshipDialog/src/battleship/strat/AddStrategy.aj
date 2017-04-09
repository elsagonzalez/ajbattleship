package battleship.strat;

import javax.swing.JButton;

import battleship.BattleshipDialog;

public aspect AddStrategy {
	void around(BattleshipDialog a): execution(new(*)) && target(a){
		//a.playButton.setText("Practice");
	}
}
