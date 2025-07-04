import java.util.ArrayList;
import java.util.List;

public class Pawn extends ChessPiece {

    public Pawn(Color color, Position position) {
        super(color, position);
    }

    @Override
    public List<Position> getLegalMoves(Board board) {
        List<Position> moves = new ArrayList<>();
        int dir = isWhite() ? -1 : 1;

        // 1 Feld geradeaus
        Position oneStep = new Position(position.getRow() + dir, position.getCol());
        if (oneStep.isValid() && board.getPiece(oneStep) == null) {
            moves.add(oneStep);

            // 2 Felder beim ersten Zug
            boolean isStartRow = (isWhite() && position.getRow() == 6) || (isBlack() && position.getRow() == 1);
            Position twoSteps = new Position(position.getRow() + 2 * dir, position.getCol());
            if (isStartRow && board.getPiece(twoSteps) == null) {
                moves.add(twoSteps);
            }
        }

        // Diagonal schlagen
        int[] dCols = {-1, 1};
        for (int d : dCols) {
            Position diag = new Position(position.getRow() + dir, position.getCol() + d);
            if (diag.isValid()) {
                ChessPiece target = board.getPiece(diag);
                if (target != null && target.getColor() != this.color) {
                    moves.add(diag);
                }
            }
        }

        // En passant
        Position lastFrom = board.getLastMoveFrom();
        Position lastTo = board.getLastMoveTo();

        if (lastFrom != null && lastTo != null) {
            ChessPiece lastMoved = board.getPiece(lastTo);
            if (lastMoved instanceof Pawn && lastMoved.getColor() != this.color) {
                if (Math.abs(lastTo.getRow() - lastFrom.getRow()) == 2 &&
                        lastTo.getRow() == this.position.getRow() &&
                        Math.abs(lastTo.getCol() - this.position.getCol()) == 1) {

                    Position enPassantTarget = new Position(position.getRow() + dir, lastTo.getCol());
                    if (enPassantTarget.isValid()) {
                        moves.add(enPassantTarget);
                    }
                }
            }
        }

        return moves;
    }

    @Override
    public String getSymbol() {
        return isWhite() ? "♙" : "♟";
    }
}
