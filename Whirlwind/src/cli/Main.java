package cli;

import java.awt.EventQueue;
import java.util.Scanner;

import logic.Game;

public class Main {

	private static int GAMEMODE = 0;

	public static void main(String[] args) throws Exception {
		setConfigurations();
		Game game = new Game(GAMEMODE);
		game.startGame();
	}

	private static void setConfigurations() {
		guiConfigs();
		// consoleConfigs();
	}

	private static void guiConfigs() {
		ConfigFrame configframe = new ConfigFrame();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					configframe.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		do {
			GAMEMODE = configframe.getGAMEMODE();
			System.out.print("");
		} while (GAMEMODE == 0);
		configframe.dispose();
	}

	private static void consoleConfigs() {
		System.out.println("Choose the game mode:");
		System.out.println("1 - Human versus Human");
		System.out.println("2 - Human versus Computer");
		System.out.println("3 - Computer versus Computer");

		Scanner reader = new Scanner(System.in);

		while (GAMEMODE != 1 && GAMEMODE != 2 && GAMEMODE != 3) {
			GAMEMODE = reader.nextInt();
		}
	}
}
