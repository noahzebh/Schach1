public class Position {
    private int row; // 0-7 (0 = untere Reihe aus Sicht von Weiß)
    private int col; // 0-7 (0 = linke Spalte = a)

    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public boolean isValid() {
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Position)) return false;
        Position other = (Position) obj;
        return this.row == other.row && this.col == other.col;
    }

    @Override
    public int hashCode() {
        return 8 * row + col;
    }

    @Override
    public String toString() {
        char file = (char) ('a' + col);
        int rank = 8 - row;
        return "" + file + rank;
    }

    public String serialize() {
        return row + "," + col;
    }

    public static Position fromString(String str) {
        String[] positionen = str.split(",");
        return new Position(Integer.valueOf(positionen[0]), Integer.valueOf(positionen[1]));
    }
}

