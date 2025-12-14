package chess;

public class Move {
    private final Position from;
    private final Position to;
    private final Piece piece; // Added: The piece that is moving
    private final Piece capturedPiece;

   
    public Move(Position from, Position to, Piece piece, Piece capturedPiece) {
        this.from = from;
        this.to = to;
        this.piece = piece;
        this.capturedPiece = capturedPiece;
    }

    /**
     * @return The starting position of the move.
     */
    public Position getFrom() {
        return from;
    }

    /**
     * @return The ending position of the move.
     */
    public Position getTo() {
        return to;
    }

    /**
     * @return The piece that was moved.
     */
    public Piece getPiece() {
        return piece;
    }

    /**
     * @return The piece captured at the destination, or null if it was an empty square.
     */
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