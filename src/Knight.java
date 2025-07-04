import java.util.ArrayList;
import java.util.List;

public class Knight extends ChessPiece {

    public Knight(Color color, Position position) {
        super(color, position);
    }

    @Override
    public List<Position> getLegalMoves(Board board) {
        List<Position> moves = new ArrayList<>();

        int[][] deltas = {
                {-2, -1}, {-2, 1}, {-1, -2}, {-1, 2},
                {1, -2}, {1, 2}, {2, -1}, {2, 1}
        };

        for (int[] d : deltas) {
            Position next = new Position(position.getRow() + d[0], position.getCol() + d[1]);
            if (next.isValid()) {
                ChessPiece target = board.getPiece(next);
                if (target == null || target.getColor() != this.color) {
                    moves.add(next);
                }
            }
        }

        return moves;
    }

    @Override
    public String getSymbol() {
        return isWhite() ? "♘" : "♞";
    }
}
