package cli;

import logic.Game;

import java.awt.*;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    private static int GAMEMODE = 0;

    public static void main(String[] args) throws Exception {
    	int winner=0;
        int DISPLAY = setConfigurations();
        Game game = new Game(DISPLAY, GAMEMODE);
        int nwinsrand=0;
        int nwinsheur=0;
        if(DISPLAY==1){
        }
        else{
        	if(GAMEMODE==4)
        		for(int i=0;i<4;i++){
        	  winner= game.startGame();
        	  if(winner==2){
        		  nwinsrand++;
        	  }
        	  if(winner==1){
        		  nwinsheur++;
        	  }
        	  }
        	else{
        		winner= game.startGame();
        	}
        }
       
        if(GAMEMODE==4){
        	System.out.println("number of wins with no heuristic: "+nwinsrand);
        	System.out.println("number of wins with heuristic: "+nwinsheur);
        }
        else{
        if (winner == 1)
            System.out.println("Black Won!");
        else if (winner == 2)
            System.out.println("White Won!");
       /* else
            System.err.println("Error");*/
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
        System.out.println("1 - Human versus Human");
        System.out.println("2 - Human versus Computer");
        System.out.println("3 - Computer versus Computer");
        System.out.println("4 - CPU vs CPU stats");

        Scanner reader = new Scanner(System.in);

        while (GAMEMODE != 1 && GAMEMODE != 2 && GAMEMODE != 3 && GAMEMODE != 4) {
            GAMEMODE = reader.nextInt();
        }
    }
}
