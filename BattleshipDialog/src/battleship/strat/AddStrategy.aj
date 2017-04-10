package battleship.strat;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
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
	JPanel around(BattleshipDialog a): draw() && target(a){
		JPanel content = new JPanel(new BorderLayout());
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttons.setBorder(BorderFactory.createEmptyBorder(0,5,0,0));
        JButton playButton = new JButton("Play");
        buttons.add(a.playButton);
        buttons.add(playButton);
        a.playButton.setFocusPainted(false);
        a.playButton.addActionListener(a::playButtonClicked);
        content.add(buttons, BorderLayout.NORTH);
        a.msgBar.setBorder(BorderFactory.createEmptyBorder(5,10,0,0));
        content.add(a.msgBar, BorderLayout.SOUTH);
        return content;
	}
}
