import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ChessGUI extends JFrame {
    private Board board;
    private Player whitePlayer;
    private Player blackPlayer;
    private Player currentPlayer;
    private JButton[][] buttons;
    private Position selectedFrom = null;

    public ChessGUI() {
        setTitle("Schach");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);
        setLayout(new GridLayout(8, 8));

        board = new Board();
        whitePlayer = new Player("Weiß", ChessPiece.Color.WHITE);
        blackPlayer = new Player("Schwarz", ChessPiece.Color.BLACK);
        currentPlayer = whitePlayer;

        buttons = new JButton[8][8];

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                JButton button = new JButton();
                button.setFont(new Font("Arial Unicode MS", Font.PLAIN, 32));
                int finalRow = row;
                int finalCol = col;
                button.addActionListener(e -> handleClick(finalRow, finalCol));
                buttons[row][col] = button;
                add(button);
            }
        }

        refreshBoard();
        setVisible(true);
    }

    private void handleClick(int row, int col) {
        Position clicked = new Position(row, col);
        ChessPiece clickedPiece = board.getPiece(clicked);

        if (selectedFrom == null) {
            // Startfeld wählen
            if (clickedPiece != null && clickedPiece.getColor() == currentPlayer.getColor()) {
                selectedFrom = clicked;
                highlightPossibleMoves(clickedPiece);
            }
        } else {
            // Ziel wählen
            ChessPiece movingPiece = board.getPiece(selectedFrom);
            if (movingPiece != null) {
                if (movingPiece.getLegalMoves(board).contains(clicked)) {
                    board.movePiece(selectedFrom, clicked);
                    switchTurn();
                }
            }
            selectedFrom = null;
            refreshBoard();
        }
    }

    private void highlightPossibleMoves(ChessPiece piece) {
        refreshBoard();
        for (Position move : piece.getLegalMoves(board)) {
            buttons[move.getRow()][move.getCol()].setBackground(Color.YELLOW);
        }
        buttons[piece.getPosition().getRow()][piece.getPosition().getCol()].setBackground(Color.CYAN);
    }

    private void refreshBoard() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                ChessPiece piece = board.getPiece(new Position(row, col));
                JButton button = buttons[row][col];
                button.setText(piece != null ? piece.getSymbol() : "");
                button.setBackground((row + col) % 2 == 0 ? Color.WHITE : Color.LIGHT_GRAY);
            }
        }
    }

    private void switchTurn() {
        currentPlayer = (currentPlayer == whitePlayer) ? blackPlayer : whitePlayer;
        setTitle("Schach – " + currentPlayer.getName() + " ist am Zug");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ChessGUI::new);
    }
    // In ChessGUI.java

}

