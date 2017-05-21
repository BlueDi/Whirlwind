package cli;

import logic.Game;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    private static int GAMEMODE = 0;
    private static Game game;
    private static int DISPLAY;
    private static ArrayList<String> winnersMessages;

    public static void main(String[] args) throws Exception {
        DISPLAY = setConfigurations();
        int winner = -1;
        game = new Game(DISPLAY, GAMEMODE);


        if (DISPLAY != 1) {
            if (GAMEMODE == 4) {
                analyseProgram();
            } else {
                winner = game.startGame();
            }
            if (winner == 1)
                System.out.println("Black Won!");
            else if (winner == 0)
                System.out.println("White Won!");
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
        System.out.println("3 - CPU Hard vs CPU Hard");
        System.out.println("4 - All modes with stats");
        System.out.println("5 - CPU Random vs CPU Hard");
        System.out.println("6 - CPU Easy vs CPU Medium");
        System.out.println("7 - CPU Medium vs CPU Hard");
        System.out.println("8 - CPU Easy vs CPU Hard");
        System.out.println();

        Scanner reader = new Scanner(System.in);

        while (GAMEMODE != 1 && GAMEMODE != 2 && GAMEMODE != 3 && GAMEMODE != 4 && GAMEMODE != 5 && GAMEMODE != 6 && GAMEMODE != 7 && GAMEMODE != 8) {
            GAMEMODE = reader.nextInt();
        }
    }

    private static void analyseProgram() throws Exception {
        winnersMessages = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            GAMEMODE = i + 3;
            game = new Game(DISPLAY, GAMEMODE);
            runManyTimes();
        }
        System.out.println("\n\nResultados:");
        printWinnersMessages();
    }

    private static void runManyTimes() {
        int wins_of_A = 0;
        int wins_of_B = 0;
        int winner;

        for (int i = 0; i < 10; i++) {
            winner = game.startGame();
            if (winner == 0)
                wins_of_A++;
            else if (winner == 1)
                wins_of_B++;
        }
        storeWinnersMessages(wins_of_A, wins_of_B);
    }

    private static void storeWinnersMessages(int wins_of_A, int wins_of_B) {
        if (GAMEMODE == 3) {
            winnersMessages.add("\nCPU A Hard venceu: " + wins_of_A);
            winnersMessages.add("CPU B Hard venceu: " + wins_of_B);
        } else if (GAMEMODE == 4) {
            winnersMessages.add("\nCPU Random venceu: " + wins_of_A);
            winnersMessages.add("CPU Easy venceu: " + wins_of_B);
        } else if (GAMEMODE == 5) {
            winnersMessages.add("\nCPU Random venceu: " + wins_of_A);
            winnersMessages.add("CPU Hard venceu: " + wins_of_B);
        } else if (GAMEMODE == 6) {
            winnersMessages.add("\nCPU Easy venceu: " + wins_of_A);
            winnersMessages.add("CPU Medium venceu: " + wins_of_B);
        } else if (GAMEMODE == 7) {
            winnersMessages.add("\nCPU Medium venceu: " + wins_of_A);
            winnersMessages.add("CPU Hard venceu: " + wins_of_B);
        } else if (GAMEMODE == 8) {
            winnersMessages.add("\nCPU Easy venceu: " + wins_of_A);
            winnersMessages.add("CPU Hard venceu: " + wins_of_B);
        }
    }

    private static void printWinnersMessages() {
        for (String s : winnersMessages) {
            System.out.println(s);
        }
    }
}
