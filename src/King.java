import java.util.ArrayList;
import java.util.List;

public class King extends ChessPiece {

    public King(Color color, Position position) {
        super(color, position);
    }

    @Override
    public List<Position> getLegalMoves(Board board) {
        List<Position> moves = new ArrayList<>();

        int[][] deltas = {
                {-1, -1}, {-1, 0}, {-1, 1},
                {0, -1},           {0, 1},
                {1, -1},  {1, 0},  {1, 1}
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
        return isWhite() ? "♔" : "♚";
    }
}

