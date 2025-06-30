public record PositionPair(Position from, Position to) {
    public static PositionPair fromString(String str)
    {
        String[] pieces = str.split("=");
        Position from = Position.fromString(pieces[0]);
        Position to = Position.fromString(pieces[1]);
        return new PositionPair(from, to);
    }

    public String toString()
    {
        return from.toString() + "=" + to.toString();
    }
}
