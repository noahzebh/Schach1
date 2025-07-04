import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class ChessGUI extends JFrame {
    private Board board;
    private Player whitePlayer;
    private Player blackPlayer;
    private Player currentPlayer;
    private JButton[][] buttons;
    private Position selectedFrom = null;

    private JLabel whiteClockLabel;
    private JLabel blackClockLabel;
    private Timer activeTimer;
    private long lastTickTime;

    public ChessGUI(int minutes, int increment) {
        setTitle("Schach mit Zeitkontrolle");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        board = new Board();
        whitePlayer = new Player("Weiß", ChessPiece.Color.WHITE, minutes, increment);
        blackPlayer = new Player("Schwarz", ChessPiece.Color.BLACK, minutes, increment);
        currentPlayer = whitePlayer;

        JPanel clockPanel = new JPanel(new GridLayout(1, 2));
        whiteClockLabel = new JLabel("Weiß: 05:00", SwingConstants.CENTER);
        blackClockLabel = new JLabel("Schwarz: 05:00", SwingConstants.CENTER);
        whiteClockLabel.setFont(new Font("Arial", Font.BOLD, 16));
        blackClockLabel.setFont(new Font("Arial", Font.BOLD, 16));
        clockPanel.add(whiteClockLabel);
        clockPanel.add(blackClockLabel);
        add(clockPanel, BorderLayout.NORTH);

        JPanel boardPanel = new JPanel(new GridLayout(8, 8));
        buttons = new JButton[8][8];
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                JButton button = new JButton();
                button.setFont(new Font("Arial Unicode MS", Font.PLAIN, 32));
                int finalRow = row;
                int finalCol = col;
                button.addActionListener(e -> handleClick(finalRow, finalCol));
                buttons[row][col] = button;
                boardPanel.add(button);
            }
        }
        add(boardPanel, BorderLayout.CENTER);

        refreshBoard();
        startTimerForCurrentPlayer();
        setSize(600, 650);
        setVisible(true);
    }

    private void handleClick(int row, int col) {
        Position clicked = new Position(row, col);
        ChessPiece clickedPiece = board.getPiece(clicked);

        if (selectedFrom == null) {
            if (clickedPiece != null && clickedPiece.getColor() == currentPlayer.getColor()) {
                selectedFrom = clicked;
                highlightPossibleMoves(clickedPiece);
            }
        } else {
            ChessPiece movingPiece = board.getPiece(selectedFrom);
            if (movingPiece != null) {
                List<Position> safeMoves = movingPiece.getSafeMoves(board);
                if (safeMoves.contains(clicked)) {
                    board.movePiece(selectedFrom, clicked);
                    currentPlayer.addIncrement(); // Inkrement nach Zug
                    refreshBoard();

                    if (board.isCheckmate(getOpponent().getColor())) {
                        stopActiveTimer();
                        JOptionPane.showMessageDialog(this, "Schachmatt! " + currentPlayer.getName() + " gewinnt!");
                        disableAllButtons();
                        return;
                    }

                    if (board.isStalemate(getOpponent().getColor())) {
                        stopActiveTimer();
                        JOptionPane.showMessageDialog(this, "Patt! Unentschieden.");
                        disableAllButtons();
                        return;
                    }

                    switchTurn();
                }
            }
            selectedFrom = null;
            refreshBoard();
        }
    }

    private void highlightPossibleMoves(ChessPiece piece) {
        refreshBoard();
        for (Position move : piece.getSafeMoves(board)) {
            buttons[move.getRow()][move.getCol()].setBackground(new Color(123, 165, 102)); // Grün für mögliche Züge
        }
        buttons[piece.getPosition().getRow()][piece.getPosition().getCol()].setBackground(new Color(123, 165, 102)); // Dunkelgrün für aktive Figur
    }

    private void refreshBoard() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Position pos = new Position(row, col);
                ChessPiece piece = board.getPiece(pos);
                JButton button = buttons[row][col];

                button.setText(piece != null ? piece.getSymbol() : "");
                // Farben der Felder direkt hier definiert:
                if ((row + col) % 2 == 0) {
                    button.setBackground(new Color(240, 217, 181)); // Helles Feld (beige)
                } else {
                    button.setBackground(new Color(181, 136, 99));  // Dunkles Feld (braun)
                }
            }
        }

        if (board.isKingInCheck(currentPlayer.getColor())) {
            Position kingPos = board.findKing(currentPlayer.getColor());
            if (kingPos != null) {
                buttons[kingPos.getRow()][kingPos.getCol()].setBackground(new Color(246, 246, 105)); // Gelbgrünes Highlight bei Schach
            }
        }

        whiteClockLabel.setText("Weiß: " + whitePlayer.getFormattedTime());
        blackClockLabel.setText("Schwarz: " + blackPlayer.getFormattedTime());
    }

    private void disableAllButtons() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                buttons[row][col].setEnabled(false);
            }
        }
    }

    private void switchTurn() {
        stopActiveTimer();
        currentPlayer = (currentPlayer == whitePlayer) ? blackPlayer : whitePlayer;
        setTitle("Schach – " + currentPlayer.getName() + " ist am Zug");
        startTimerForCurrentPlayer();
    }

    private Player getOpponent() {
        return currentPlayer == whitePlayer ? blackPlayer : whitePlayer;
    }

    private void startTimerForCurrentPlayer() {
        lastTickTime = System.currentTimeMillis();
        activeTimer = new Timer(1000, e -> {
            long now = System.currentTimeMillis();
            long elapsed = now - lastTickTime;
            lastTickTime = now;

            currentPlayer.decreaseTime(elapsed);
            refreshBoard();

            if (currentPlayer.getTimeLeftMillis() <= 0) {
                stopActiveTimer();
                JOptionPane.showMessageDialog(this, getOpponent().getName() + " gewinnt – Zeit abgelaufen!");
                disableAllButtons();
            }
        });
        activeTimer.start();
    }

    private void stopActiveTimer() {
        if (activeTimer != null && activeTimer.isRunning()) {
            activeTimer.stop();
        }
    }
}
