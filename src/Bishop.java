import java.util.ArrayList;
import java.util.List;

public class Bishop extends ChessPiece {

    public Bishop(Color color, Position position) {
        super(color, position);
    }

    @Override
    public List<Position> getLegalMoves(Board board) {
        List<Position> moves = new ArrayList<>();

        int[][] directions = {
                {-1, -1}, {-1, 1},
                {1, -1},  {1, 1}
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
                        moves.add(next);
                    }
                    break; // blockiert
                }
            }
        }

        return moves;
    }

    @Override
    public String getSymbol() {
        return isWhite() ? "♗" : "♝";
    }
}

