import java.io.Serializable;
import java.util.List;
import java.util.List;

public abstract class ChessPiece implements Serializable {
    public enum Color {
        WHITE, BLACK;

        public Color opposite() {
            return this == WHITE ? BLACK : WHITE;
        }
    }

    protected Color color;
    protected Position position;
    protected boolean hasMoved = false;

    public ChessPiece(Color color, Position position) {
        this.color = color;
        this.position = position;
    }

    public Color getColor() {
        return color;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position newPosition) {
        this.position = newPosition;
    }

    public boolean isWhite() {
        return color == Color.WHITE;
    }

    public boolean isBlack() {
        return color == Color.BLACK;
    }

    public boolean hasMoved() {
        return hasMoved;
    }

    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }

    public abstract List<Position> getLegalMoves(Board board);

    public List<Position> getSafeMoves(Board board) {
        List<Position> legal = getLegalMoves(board);
        List<Position> safe = new java.util.ArrayList<>();

        for (Position target : legal) {
            Position from = this.position;
            ChessPiece captured = board.getPiece(target);

            board.setPiece(from, null);
            board.setPiece(target, this);
            this.position = target;

            boolean inCheck = board.isKingInCheck(this.color);

            board.setPiece(from, this);
            board.setPiece(target, captured);
            this.position = from;

            if (!inCheck) {
                safe.add(target);
            }
        }

        return safe;
    }

    public abstract String getSymbol();
}
