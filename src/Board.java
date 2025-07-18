import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Board {
    private ChessPiece[][] grid;
    private Position lastMoveFrom;
    private Position lastMoveTo;
    File fileSave = new File("save.txt");

    private List<PositionPair> moveHistory = new ArrayList<>();

    public Board() {
        grid = new ChessPiece[8][8];
        setupInitialPosition();
        System.out.println(fileSave.getAbsolutePath());
        }

    public ChessPiece getPiece(Position pos) {
        if (!pos.isValid()) return null;
        return grid[pos.getRow()][pos.getCol()];
    }

    public void setPiece(Position pos, ChessPiece piece) {
        if (pos.isValid()) {
            grid[pos.getRow()][pos.getCol()] = piece;
        }
    }

    public void movePiece(Position from, Position to) {
        ChessPiece piece = getPiece(from);
        if (piece == null) return;

        lastMoveFrom = from;
        lastMoveTo = to;

        // Spezialfall: En-passant
        if (piece instanceof Pawn && from.getCol() != to.getCol() && getPiece(to) == null) {
            int dir = piece.isWhite() ? 1 : -1;
            setPiece(new Position(to.getRow() + dir, to.getCol()), null);
        }

        // Figur bewegen
        executeMove(from, to, piece);

        // Spezialfall: Umwandlung
        if (piece instanceof Pawn && (to.getRow() == (piece.isWhite() ? 0 : 7))) {
            setPiece(to, new Queen(piece.getColor(), to));
        }

        // Spezialfall: Rochade
        if (piece instanceof King && Math.abs(from.getCol() - to.getCol()) == 2) {
            performCastling(from, to);
        }
    }

    private void executeMove(Position from, Position to, ChessPiece piece) {
        setPiece(to, piece);
        piece.setPosition(to);
        setPiece(from, null);
        moveHistory.add(new PositionPair(from, to));
    }

    private void performCastling(Position kingFrom, Position kingTo) {
        int row = kingFrom.getRow();
        boolean kingside = kingTo.getCol() == 6;

        Position rookFrom = new Position(row, kingside ? 7 : 0);
        Position rookTo = new Position(row, kingside ? 5 : 3);

        ChessPiece rook = getPiece(rookFrom);
        if (rook != null) {
            executeMove(rookFrom, rookTo, rook);
        }
    }

    public void saveMoves() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileSave))) {
            for (String move : getMoveHistory()) { // getMoveHistory() gibt die Züge als List\<String\> zurück
                writer.write(move);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public List<String> getMoveHistory() {
        List<String> moves = new ArrayList<>();
        for (PositionPair pair : moveHistory) {
            moves.add(pair.toString()); // Stelle sicher, dass PositionPair sinnvoll als String ausgegeben wird, z.B. "e2=e4"
        }
        return moves;
    }

    public Position getLastMoveFrom() {
        return lastMoveFrom;
    }

    public Position getLastMoveTo() {
        return lastMoveTo;
    }

    public void setupInitialPosition() {
        for (int col = 0; col < 8; col++) {
            setPiece(new Position(6, col), new Pawn(ChessPiece.Color.WHITE, new Position(6, col)));
            setPiece(new Position(1, col), new Pawn(ChessPiece.Color.BLACK, new Position(1, col)));
        }

        // Weiße Figuren
        setPiece(new Position(7, 0), new Rook(ChessPiece.Color.WHITE, new Position(7, 0)));
        setPiece(new Position(7, 1), new Knight(ChessPiece.Color.WHITE, new Position(7, 1)));
        setPiece(new Position(7, 2), new Bishop(ChessPiece.Color.WHITE, new Position(7, 2)));
        setPiece(new Position(7, 3), new Queen(ChessPiece.Color.WHITE, new Position(7, 3)));
        setPiece(new Position(7, 4), new King(ChessPiece.Color.WHITE, new Position(7, 4)));
        setPiece(new Position(7, 5), new Bishop(ChessPiece.Color.WHITE, new Position(7, 5)));
        setPiece(new Position(7, 6), new Knight(ChessPiece.Color.WHITE, new Position(7, 6)));
        setPiece(new Position(7, 7), new Rook(ChessPiece.Color.WHITE, new Position(7, 7)));

        // Schwarze Figuren
        setPiece(new Position(0, 0), new Rook(ChessPiece.Color.BLACK, new Position(0, 0)));
        setPiece(new Position(0, 1), new Knight(ChessPiece.Color.BLACK, new Position(0, 1)));
        setPiece(new Position(0, 2), new Bishop(ChessPiece.Color.BLACK, new Position(0, 2)));
        setPiece(new Position(0, 3), new Queen(ChessPiece.Color.BLACK, new Position(0, 3)));
        setPiece(new Position(0, 4), new King(ChessPiece.Color.BLACK, new Position(0, 4)));
        setPiece(new Position(0, 5), new Bishop(ChessPiece.Color.BLACK, new Position(0, 5)));
        setPiece(new Position(0, 6), new Knight(ChessPiece.Color.BLACK, new Position(0, 6)));
        setPiece(new Position(0, 7), new Rook(ChessPiece.Color.BLACK, new Position(0, 7)));
    }

    public void printBoard() {
        for (int row = 0; row < 8; row++) {
            System.out.print((8 - row) + " ");
            for (int col = 0; col < 8; col++) {
                ChessPiece piece = grid[row][col];
                System.out.print((piece != null ? piece.getSymbol() : ".") + " ");
            }
            System.out.println();
        }
        System.out.println("  a b c d e f g h");
    }

    public boolean isKingInCheck(ChessPiece.Color color) {
        Position kingPos = findKing(color);
        if (kingPos == null) return false;

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                ChessPiece piece = getPiece(new Position(row, col));
                if (piece != null && piece.getColor() != color) {
                    if (piece.getLegalMoves(this).contains(kingPos)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isCheckmate(ChessPiece.Color color) {
        if (!isKingInCheck(color)) return false;

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                ChessPiece piece = getPiece(new Position(row, col));
                if (piece != null && piece.getColor() == color) {
                    if (!piece.getSafeMoves(this).isEmpty()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean isStalemate(ChessPiece.Color color) {
        if (isKingInCheck(color)) return false;

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                ChessPiece piece = getPiece(new Position(row, col));
                if (piece != null && piece.getColor() == color) {
                    if (!piece.getSafeMoves(this).isEmpty()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public Position findKing(ChessPiece.Color color) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                ChessPiece piece = getPiece(new Position(row, col));
                if (piece instanceof King && piece.getColor() == color) {
                    return piece.getPosition();
                }
            }
        }
        return null;
    }

    // In Board.java
    public void loadMoves(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileSave))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("=") && line.length() >= 5) {
                    String[] parts = line.split("=");
                    if (parts.length == 2) {
                        Position from = new Position(8 - Character.getNumericValue(parts[0].charAt(1)), parts[0].charAt(0) - 'a');
                        Position to = new Position(8 - Character.getNumericValue(parts[1].charAt(1)), parts[1].charAt(0) - 'a');
                        movePiece(from, to);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
