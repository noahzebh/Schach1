import java.util.Scanner;
import java.util.List;

public class ChessGame {
    private Board board;
    private Player whitePlayer;
    private Player blackPlayer;
    private Player currentPlayer;
    private Scanner scanner;

    public ChessGame() {
        this.board = new Board();
        this.whitePlayer = new Player("Weiß", ChessPiece.Color.WHITE);
        this.blackPlayer = new Player("Schwarz", ChessPiece.Color.BLACK);
        this.currentPlayer = whitePlayer;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        while (true) {
            board.printBoard();
            System.out.println(currentPlayer + " ist am Zug.");
            System.out.print("Zug eingeben (z. B. e2 e4): ");
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("exit")) break;

            String[] parts = input.split(" ");
            if (parts.length != 2) {
                System.out.println("Ungültiges Format!");
                continue;
            }

            Position from = parsePosition(parts[0]);
            Position to = parsePosition(parts[1]);
            if (from == null || to == null) {
                System.out.println("Ungültige Eingabe!");
                continue;
            }

            ChessPiece piece = board.getPiece(from);
            if (piece == null) {
                System.out.println("Kein Stein auf dem Startfeld.");
                continue;
            }

            if (piece.getColor() != currentPlayer.getColor()) {
                System.out.println("Das ist nicht deine Figur!");
                continue;
            }

            List<Position> legalMoves = piece.getLegalMoves(board);
            if (!legalMoves.contains(to)) {
                System.out.println("Ungültiger Zug!");
                continue;
            }

            board.movePiece(from, to);
            switchTurn();
        }
    }

    private Position parsePosition(String s) {
        if (s.length() != 2) return null;
        char file = s.charAt(0);
        char rank = s.charAt(1);
        int col = file - 'a';
        int row = 8 - (rank - '0');
        Position pos = new Position(row, col);
        return pos.isValid() ? pos : null;
    }

    private void switchTurn() {
        currentPlayer = (currentPlayer == whitePlayer) ? blackPlayer : whitePlayer;
    }

    public static void main(String[] args) {
        new ChessGame().start();
    }
}

