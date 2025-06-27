import java.util.ArrayList;
import java.util.List;

public class Pawn extends ChessPiece {

    public Pawn(Color color, Position position) {
        super(color, position);
    }

    @Override
    public List<Position> getLegalMoves(Board board) {
        List<Position> moves = new ArrayList<>();
        int dir = isWhite() ? -1 : 1; // Weiß zieht nach oben (niedrigere Zeile), Schwarz nach unten

        // Ein Feld geradeaus
        Position oneStep = new Position(position.getRow() + dir, position.getCol());
        if (oneStep.isValid() && board.getPiece(oneStep) == null) {
            moves.add(oneStep);

            // Zwei Felder beim ersten Zug
            boolean isStartRow = (isWhite() && position.getRow() == 6) || (isBlack() && position.getRow() == 1);
            Position twoSteps = new Position(position.getRow() + 2 * dir, position.getCol());
            if (isStartRow && board.getPiece(twoSteps) == null) {
                moves.add(twoSteps);
            }
        }

        // Diagonal schlagen
        int[] dx = {-1, 1};
        for (int d : dx) {
            Position diag = new Position(position.getRow() + dir, position.getCol() + d);
            if (diag.isValid()) {
                ChessPiece target = board.getPiece(diag);
                if (target != null && target.getColor() != this.color) {
                    moves.add(diag);
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
