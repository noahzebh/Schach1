public class Board {
    private ChessPiece[][] grid;
    private Position lastMoveFrom;
    private Position lastMoveTo;

    public Board() {
        grid = new ChessPiece[8][8];
        setupInitialPosition();
    }

    public ChessPiece getPiece(Position pos) {
        if (!pos.isValid()) return null;
        return grid[pos.getRow()][pos.getCol()];
    }

    public void setPiece(Position pos, ChessPiece piece) {
        if (pos.isValid()) {
            grid[pos.getRow()][pos.getCol()] = piece;
        }
    }

    public void movePiece(Position from, Position to) {
        ChessPiece piece = getPiece(from);
        if (piece != null) {
            lastMoveFrom = from;
            lastMoveTo = to;

            // En-passant-Schlag
            if (piece instanceof Pawn && from.getCol() != to.getCol() && getPiece(to) == null) {
                int direction = piece.isWhite() ? 1 : -1;
                Position enemyPos = new Position(to.getRow() + direction, to.getCol());
                setPiece(enemyPos, null);
            }

            // Normale Bewegung
            setPiece(to, piece);
            piece.setPosition(to);
            setPiece(from, null);

            // Umwandlung
            if (piece instanceof Pawn) {
                int endRow = piece.isWhite() ? 0 : 7;
                if (to.getRow() == endRow) {
                    setPiece(to, new Queen(piece.getColor(), to));
                }
            }

            // Rochade
            if (piece instanceof King) {
                int row = from.getRow();
                if (Math.abs(from.getCol() - to.getCol()) == 2) {
                    // kurze Rochade
                    if (to.getCol() == 6) {
                        Position rookFrom = new Position(row, 7);
                        Position rookTo = new Position(row, 5);
                        ChessPiece rook = getPiece(rookFrom);
                        if (rook != null) {
                            setPiece(rookTo, rook);
                            rook.setPosition(rookTo);
                            setPiece(rookFrom, null);
                        }
                    }
                    // lange Rochade
                    if (to.getCol() == 2) {
                        Position rookFrom = new Position(row, 0);
                        Position rookTo = new Position(row, 3);
                        ChessPiece rook = getPiece(rookFrom);
                        if (rook != null) {
                            setPiece(rookTo, rook);
                            rook.setPosition(rookTo);
                            setPiece(rookFrom, null);
                        }
                    }
                }
            }
        }
    }

    public Position getLastMoveFrom() {
        return lastMoveFrom;
    }

    public Position getLastMoveTo() {
        return lastMoveTo;
    }

    public void setupInitialPosition() {
        for (int col = 0; col < 8; col++) {
            setPiece(new Position(6, col), new Pawn(ChessPiece.Color.WHITE, new Position(6, col)));
            setPiece(new Position(1, col), new Pawn(ChessPiece.Color.BLACK, new Position(1, col)));
        }

        // Weiße Figuren
        setPiece(new Position(7, 0), new Rook(ChessPiece.Color.WHITE, new Position(7, 0)));
        setPiece(new Position(7, 1), new Knight(ChessPiece.Color.WHITE, new Position(7, 1)));
        setPiece(new Position(7, 2), new Bishop(ChessPiece.Color.WHITE, new Position(7, 2)));
        setPiece(new Position(7, 3), new Queen(ChessPiece.Color.WHITE, new Position(7, 3)));
        setPiece(new Position(7, 4), new King(ChessPiece.Color.WHITE, new Position(7, 4)));
        setPiece(new Position(7, 5), new Bishop(ChessPiece.Color.WHITE, new Position(7, 5)));
        setPiece(new Position(7, 6), new Knight(ChessPiece.Color.WHITE, new Position(7, 6)));
        setPiece(new Position(7, 7), new Rook(ChessPiece.Color.WHITE, new Position(7, 7)));

        // Schwarze Figuren
        setPiece(new Position(0, 0), new Rook(ChessPiece.Color.BLACK, new Position(0, 0)));
        setPiece(new Position(0, 1), new Knight(ChessPiece.Color.BLACK, new Position(0, 1)));
        setPiece(new Position(0, 2), new Bishop(ChessPiece.Color.BLACK, new Position(0, 2)));
        setPiece(new Position(0, 3), new Queen(ChessPiece.Color.BLACK, new Position(0, 3)));
        setPiece(new Position(0, 4), new King(ChessPiece.Color.BLACK, new Position(0, 4)));
        setPiece(new Position(0, 5), new Bishop(ChessPiece.Color.BLACK, new Position(0, 5)));
        setPiece(new Position(0, 6), new Knight(ChessPiece.Color.BLACK, new Position(0, 6)));
        setPiece(new Position(0, 7), new Rook(ChessPiece.Color.BLACK, new Position(0, 7)));
    }

    public void printBoard() {
        for (int row = 0; row < 8; row++) {
            System.out.print((8 - row) + " ");
            for (int col = 0; col < 8; col++) {
                ChessPiece piece = grid[row][col];
                System.out.print((piece != null ? piece.getSymbol() : ".") + " ");
            }
            System.out.println();
        }
        System.out.println("  a b c d e f g h");
    }

    public boolean isKingInCheck(ChessPiece.Color color) {
        Position kingPos = findKing(color);
        if (kingPos == null) return false;

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                ChessPiece piece = getPiece(new Position(row, col));
                if (piece != null && piece.getColor() != color) {
                    if (piece.getLegalMoves(this).contains(kingPos)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isCheckmate(ChessPiece.Color color) {
        if (!isKingInCheck(color)) return false;

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                ChessPiece piece = getPiece(new Position(row, col));
                if (piece != null && piece.getColor() == color) {
                    if (!piece.getSafeMoves(this).isEmpty()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean isStalemate(ChessPiece.Color color) {
        if (isKingInCheck(color)) return false;

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                ChessPiece piece = getPiece(new Position(row, col));
                if (piece != null && piece.getColor() == color) {
                    if (!piece.getSafeMoves(this).isEmpty()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public Position findKing(ChessPiece.Color color) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                ChessPiece piece = getPiece(new Position(row, col));
                if (piece instanceof King && piece.getColor() == color) {
                    return piece.getPosition();
                }
            }
        }
        return null;
    }
}
