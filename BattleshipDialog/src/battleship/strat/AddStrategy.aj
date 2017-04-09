package battleship.strat;

import java.awt.Dimension;

import javax.swing.JButton;

import battleship.BattleshipDialog;

public aspect AddStrategy {
	void around(BattleshipDialog a): execution(BattleshipDialog.new(*)) && this(a){
		System.out.println("Created a dialog object");
		proceed(a);
	}
}
