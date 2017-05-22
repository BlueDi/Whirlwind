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
    private static int NUMBER_OF_LOOPS = 100;
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
        for (GAMEMODE = 3; GAMEMODE < 12; GAMEMODE++) {
            long start = System.nanoTime();
            runManyTimes();
            long diff = System.nanoTime() - start;
            double elapsedTime = (double) diff / 1000000000.0;
            winnersMessages.add("Tempo de execução: " + elapsedTime + " segundos.");
            System.out.println("Gamemode " + GAMEMODE + " done.");
        }
        System.out.println("\n\nResultados:");
        printWinnersMessages();
    }

    private static void runManyTimes() throws Exception {
        int wins_of_W = 0;
        int wins_of_B = 0;
        int winner;

        for (int i = 1; i < NUMBER_OF_LOOPS+1; i++) {
            game = new Game(DISPLAY, GAMEMODE);
            winner = game.startGame();
            if (winner == 0)
                wins_of_W++;
            else if (winner == 1)
                wins_of_B++;

            progressbar(i, NUMBER_OF_LOOPS+1);
        }
        storeWinnersMessages(wins_of_B, wins_of_W);
    }

    private static void progressbar(int done, int total) {
        String format = "\r%3d%% ";
        int percent = (++done * 100) / total;
        System.out.printf(format, percent);

        if (done >= total)
            System.out.flush();
    }

    private static void storeWinnersMessages(int wins_of_B, int wins_of_W) {
        if (GAMEMODE == 3) {
            winnersMessages.add("\nCPU A Hard venceu: " + wins_of_B);
            winnersMessages.add("CPU B Hard venceu: " + wins_of_W);
        } else if (GAMEMODE == 4) {
            winnersMessages.add("\nCPU Random venceu: " + wins_of_B);
            winnersMessages.add("CPU Easy venceu: " + wins_of_W);
        } else if (GAMEMODE == 5) {
            winnersMessages.add("\nCPU Random venceu: " + wins_of_B);
            winnersMessages.add("CPU Hard venceu: " + wins_of_W);
        } else if (GAMEMODE == 6) {
            winnersMessages.add("\nCPU Easy venceu: " + wins_of_B);
            winnersMessages.add("CPU Medium venceu: " + wins_of_W);
        } else if (GAMEMODE == 7) {
            winnersMessages.add("\nCPU Medium venceu: " + wins_of_B);
            winnersMessages.add("CPU Hard venceu: " + wins_of_W);
        } else if (GAMEMODE == 8) {
            winnersMessages.add("\nCPU Easy venceu: " + wins_of_B);
            winnersMessages.add("CPU Hard venceu: " + wins_of_W);
        } else if (GAMEMODE == 9) {
            winnersMessages.add("\nCPU A Easy venceu: " + wins_of_B);
            winnersMessages.add("CPU B Easy venceu: " + wins_of_W);
        } else if (GAMEMODE == 10) {
            winnersMessages.add("\nCPU A Medium venceu: " + wins_of_B);
            winnersMessages.add("CPU B Medium venceu: " + wins_of_W);
        } else if (GAMEMODE == 11) {
            winnersMessages.add("\nCPU A Random venceu: " + wins_of_B);
            winnersMessages.add("CPU B Random venceu: " + wins_of_W);
        }
    }

    private static void printWinnersMessages() {
        for (String s : winnersMessages) {
            System.out.println(s);
        }
    }
}
