package chess;

public class Move {
    private final Position from;
    private final Position to;
    private final Piece capturedPiece;

    public Move(Position from, Position to, Piece capturedPiece) {
        this.from = from;
        this.to = to;
        this.capturedPiece = capturedPiece;
    }

    public Position getFrom() {
        return from;
    }

    public Position getTo() {
        return to;
    }

    public Piece getCapturedPiece() {
        return capturedPiece;
    }

    @Override
    public String toString() {
        String move = from.toString() + " -> " + to.toString();
        if (capturedPiece != null) {
            move += " (captures " + capturedPiece.getSymbol() + ")";
        }
        return move;
    }
}
