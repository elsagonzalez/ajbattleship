package battleship.strat;

import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.aspectj.lang.JoinPoint;

import battleship.BattleshipDialog;

privileged public aspect AddStrategy {
	pointcut constructor(): execution(BattleshipDialog.new(*));
	pointcut draw(): execution(JPanel BattleshipDialog.makeControlPane());
	after(BattleshipDialog a): constructor() && target(a){
		a.playButton.setText("Practice");
	}
	JPanel around(): draw(){
		return proceed();
	}
}
