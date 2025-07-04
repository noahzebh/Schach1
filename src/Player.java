import java.io.Serializable;

public class Player implements Serializable {
    private String name;
    private ChessPiece.Color color;
    private long timeLeftMillis;
    private int incrementSeconds;

    // Neuer: Für Konsolenspiel oder Standardverwendung
    public Player(String name, ChessPiece.Color color) {
        this(name, color, 5, 0); // Standard: 5 Minuten, kein Inkrement
    }

    // Hauptkonstruktor für die GUI
    public Player(String name, ChessPiece.Color color, int minutes, int incrementSeconds) {
        this.name = name;
        this.color = color;
        this.timeLeftMillis = minutes * 60L * 1000L;
        this.incrementSeconds = incrementSeconds;
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

    public long getTimeLeftMillis() {
        return timeLeftMillis;
    }

    public void decreaseTime(long millis) {
        timeLeftMillis = Math.max(0, timeLeftMillis - millis);
    }

    public void addIncrement() {
        timeLeftMillis += incrementSeconds * 1000L;
    }

    public String getFormattedTime() {
        long seconds = timeLeftMillis / 1000;
        long minutes = seconds / 60;
        seconds %= 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    @Override
    public String toString() {
        return name + " (" + (isWhite() ? "Weiß" : "Schwarz") + ")";
    }
}
