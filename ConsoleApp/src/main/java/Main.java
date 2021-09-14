import domain.Grid;
import domain.Point;
import opponent.ExampleOpponent;
import opponent.Opponent;
import opponent.RandomOpponent;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class Main {
    static GameController controller;
    public static void main(String[] args) {
        final Opponent[] opponents = new Opponent[]{
                new RandomOpponent(),
                new ExampleOpponent(),
        };

        System.out.println("Welcome to TicTacToe, select a opponent: ");
        /*
         IntStream.range(0, opponents.length)
                .mapToObj(i -> i+1 + ": " + opponents[i].getClass().getSimpleName() + " | ")
               .forEachOrdered(System.out::println);
         */
        for (int i = 0; i < opponents.length; i++) {
            System.out.println(i + 1 + ": " + opponents[i].getClass().getSimpleName());
        }

        int choice;

        System.out.println("Select the number of the opponent you want to play against");
        do {
            try {
                choice = new Scanner(System.in).nextInt()-1;
                if (choice < 0 || choice >= opponents.length) {
                    System.err.println("Invalid Number, please choose again");
                }
            } catch (java.util.InputMismatchException e) {
                System.err.println("Not a number, please choose again");
                choice = -1;
            }
        } while (choice < 0 || choice >= opponents.length);

        final Opponent opponent = opponents[choice];
         controller = new GameController(opponent);
        selectPoint();

    }

    private static Point selectPoint() {
        int x=0,y=0;
        Point point = new Point(x,y);
        int[] coords = new int[2];

        System.out.println("Choose your Point (x,y): ");

                do {
                    String playerPoint = new Scanner(System.in).next();
                    try {
                        coords[0] = Integer.parseInt(playerPoint.split(",")[0]);
                        coords[1] = Integer.parseInt(playerPoint.split(",")[1]);
                    } catch (NumberFormatException numberFormatException) {
                        System.err.println("Not a number, please choose again");
                    }

                    if(coords[0] < 0 || coords[0] > 2 || coords[1] < 0 || coords[1] > 2) {
                        System.err.println("Invalid Number, please choose again");
                    }


                } while(coords[0] < 0 || coords[0] > 2 || coords[1] < 0 || coords[1] > 2);

        controller.setPoint(new Point(0,0));
        return point;
    }
}
