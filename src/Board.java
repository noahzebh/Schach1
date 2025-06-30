import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Board implements Serializable {
    private ChessPiece[][] grid;
    private List<PositionPair> movesHistory;
    private File save= new File("src/data/save.txt");

    public Board() {
        movesHistory = new ArrayList<>();
        grid = new ChessPiece[8][8];
        setupInitialPosition();
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
        if (piece != null) {
            setPiece(to, piece);
            piece.setPosition(to);
            setPiece(from, null);
            movesHistory.add(new PositionPair(from, to));
        }
    }

    public void saveToFile() {
        try {
            FileWriter fw = new FileWriter(save);
            for (PositionPair move : movesHistory) {
                fw.write(move.from().serialize() + "=" + move.to().serialize() + "\n");

            }

        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



             public void setupInitialPosition() {
        // Bauern
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
}

