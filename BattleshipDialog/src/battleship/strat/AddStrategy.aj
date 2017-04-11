package battleship.strat;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;


import battleship.BattleshipDialog;
import battleship.BoardPanel;
import battleship.model.Board;

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
		JDialog player = new JDialog();
		player.setLayout(new BorderLayout());
		player.add(new BoardPanel(new Board(10)), BorderLayout.CENTER);
		player.setSize(new Dimension(335, 440));
		player.setTitle("PLayer Board");
		player.setVisible(true);
        player.setDefaultCloseOperation(2);
	}
}
