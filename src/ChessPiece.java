import java.io.Serializable;
import java.util.List;

public abstract class ChessPiece implements Serializable {
    public enum Color { WHITE, BLACK }

    protected Color color;
    protected Position position;

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

    // Jede Figur implementiert ihre eigenen Regeln für mögliche Züge
    public abstract List<Position> getLegalMoves(Board board);

    // Text-Symbol zur Darstellung der Figur
    public abstract String getSymbol();
}
