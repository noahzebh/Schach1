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

        // Rochade (nur wenn König noch nicht gezogen hat)
        if (!hasMoved) {
            int row = position.getRow();

            // kurze Rochade (rechts)
            Position rookPos = new Position(row, 7);
            ChessPiece rook = board.getPiece(rookPos);
            if (rook instanceof Rook && !rook.hasMoved()) {
                if (board.getPiece(new Position(row, 5)) == null &&
                        board.getPiece(new Position(row, 6)) == null &&
                        !board.isKingInCheck(color) &&
                        !wouldBeInCheck(board, new Position(row, 5)) &&
                        !wouldBeInCheck(board, new Position(row, 6))) {
                    moves.add(new Position(row, 6));
                }
            }

            // lange Rochade (links)
            rookPos = new Position(row, 0);
            rook = board.getPiece(rookPos);
            if (rook instanceof Rook && !rook.hasMoved()) {
                if (board.getPiece(new Position(row, 1)) == null &&
                        board.getPiece(new Position(row, 2)) == null &&
                        board.getPiece(new Position(row, 3)) == null &&
                        !board.isKingInCheck(color) &&
                        !wouldBeInCheck(board, new Position(row, 2)) &&
                        !wouldBeInCheck(board, new Position(row, 3))) {
                    moves.add(new Position(row, 2));
                }
            }
        }

        return moves;
    }

    private boolean wouldBeInCheck(Board board, Position pos) {
        Position oldPos = this.position;
        ChessPiece captured = board.getPiece(pos);

        board.setPiece(oldPos, null);
        board.setPiece(pos, this);
        this.position = pos;

        boolean check = board.isKingInCheck(this.color);

        board.setPiece(pos, captured);
        board.setPiece(oldPos, this);
        this.position = oldPos;

        return check;
    }

    @Override
    public String getSymbol() {
        return isWhite() ? "♔" : "♚";
    }
}
