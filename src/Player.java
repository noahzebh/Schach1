import java.io.Serializable;

public class Player implements Serializable {
    private String name;
    private ChessPiece.Color color;

    public Player(String name, ChessPiece.Color color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public ChessPiece.Color getColor() {
        return color;
    }

    public boolean isWhite() {
        return color == ChessPiece.Color.WHITE;
    }

    public boolean isBlack() {
        return color == ChessPiece.Color.BLACK;
    }

    @Override
    public String toString() {
        return name + " (" + (isWhite() ? "Weiß" : "Schwarz") + ")";
    }
}

