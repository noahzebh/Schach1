import java.util.ArrayList;
import java.util.List;

public class Rook extends ChessPiece {

    public Rook(Color color, Position position) {
        super(color, position);
    }

    @Override
    public List<Position> getLegalMoves(Board board) {
        List<Position> moves = new ArrayList<>();

        int[][] directions = {
                {-1, 0}, // oben
                {1, 0},  // unten
                {0, -1}, // links
                {0, 1}   // rechts
        };

        for (int[] dir : directions) {
            int row = position.getRow();
            int col = position.getCol();
            while (true) {
                row += dir[0];
                col += dir[1];
                Position next = new Position(row, col);
                if (!next.isValid()) break;

                ChessPiece piece = board.getPiece(next);
                if (piece == null) {
                    moves.add(next);
                } else {
                    if (piece.getColor() != this.color) {
                        moves.add(next); // schlagen erlaubt
                    }
                    break; // eigene Figur oder gegnerische blockiert
                }
            }
        }

        return moves;
    }

    @Override
    public String getSymbol() {
        return isWhite() ? "♖" : "♜";
    }
}
