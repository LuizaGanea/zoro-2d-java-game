package main;

import javax.swing.JFrame;

public class Main {

	public static void main(String[] args) {
		JFrame window =new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //pt a inchide jocul
		window.setResizable(false); //pt a schimba dimensiunile ferestrei
		window.setTitle("Zoro");

		GamePanel gamePanel=new GamePanel(); //adaugam gamepanelul in main
		window.add(gamePanel);
		window.pack(); //in fereastra jocului sa incapa componentele
		
		window.setVisible(true); //pt a vedea fereastra jocului
		window.setLocationRelativeTo(null); //in centrul ecranului
		gamePanel.setupGame();
		
		gamePanel.startGameThread(); //apelam loop-ul
		

	}

}
