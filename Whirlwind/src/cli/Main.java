package cli;

import logic.Game;

import java.awt.*;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    private static int GAMEMODE = 0;

    public static void main(String[] args) throws Exception {
        int DISPLAY = setConfigurations();
        Game game = new Game(DISPLAY, GAMEMODE);
        int wins_of_random = 0;
        int wins_of_heur = 0;
        int winner;

        if (GAMEMODE == 4) {
            for (int i = 0; i < 5; i++) {
                winner = game.startGame();
                if (winner == 0)
                    wins_of_random++;
                else if (winner == 1)
                    wins_of_heur++;
            }
            System.out.println("\nNúmero de vitórias sem heuristicas: " + wins_of_random);
            System.out.println("Número de vitórias com heuristica: " + wins_of_heur);
        } else {
            winner = game.startGame();

            if (winner == 1)
                System.out.println("Black Won!");
            else if (winner == 0)
                System.out.println("White Won!");
            else
                System.err.println("Error. Invalid Winner.");
        }
    }

    private static int setConfigurations() {
        if (selectDisplay() == 1) {
            guiConfigs();
            return 1;
        } else
            consoleConfigs();
        return 0;
    }

    private static int selectDisplay() {
        int answer = 0;
        System.out.println("If you want a Graphic display press 1");
        System.out.println("If you want a Console display press 2");

        while (answer != 1 && answer != 2)
            try {
                answer = -48 + System.in.read();
            } catch (IOException e) {
                e.printStackTrace();
            }

        return answer;
    }

    private static void guiConfigs() {
        ConfigFrame configframe = new ConfigFrame();
        EventQueue.invokeLater(() -> {
            try {
                configframe.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
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
        System.out.println("1 - Human vs Human");
        System.out.println("2 - Human vs Computer");
        System.out.println("3 - Computer vs Computer");
        System.out.println("4 - CPU vs CPU with stats");

        Scanner reader = new Scanner(System.in);

        while (GAMEMODE != 1 && GAMEMODE != 2 && GAMEMODE != 3 && GAMEMODE != 4) {
            GAMEMODE = reader.nextInt();
        }
    }
}
