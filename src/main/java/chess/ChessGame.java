package chess;

import java.util.Scanner;

public class ChessGame {
    public static void main(String[] args) {
        Game game = new Game();
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Welcome to Chess! ===");
        System.out.println("Enter moves in format: e2 e4 (from to)");
        System.out.println("Type 'quit' to exit\n");

        while (game.getStatus() == GameStatus.IN_PROGRESS) {
            game.displayBoard();

            System.out.print(game.getCurrentPlayer() + "'s turn. Enter move: ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("quit")) {
                System.out.println("Thanks for playing!");
                break;
            }

            String[] parts = input.split("\\s+");
            if (parts.length != 2) {
                System.out.println("Invalid input! Use format: e2 e4");
                continue;
            }

            try {
                Position from = parsePosition(parts[0]);
                Position to = parsePosition(parts[1]);

                if (!game.makeMove(from, to)) {
                    System.out.println("Move failed. Try again.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid position format! Use format like: e2");
            }
        }

        if (game.getStatus() != GameStatus.IN_PROGRESS) {
            game.displayBoard();
            System.out.println("Game Over! Final status: " + game.getStatus());
        }

        scanner.close();
    }

    private static Position parsePosition(String pos) {
        if (pos.length() != 2) {
            throw new IllegalArgumentException("Invalid position");
        }

        char colChar = pos.charAt(0);
        char rowChar = pos.charAt(1);

        if (colChar < 'a' || colChar > 'h' || rowChar < '1' || rowChar > '8') {
            throw new IllegalArgumentException("Invalid position");
        }

        int col = colChar - 'a';
        int row = 8 - (rowChar - '0');

        return new Position(row, col);
    }
}
