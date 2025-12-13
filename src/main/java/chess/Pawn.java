package chess;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {
    public Pawn(Color color) {
        super(color);
    }

    @Override
    public List<Position> getPossibleMoves(Position from, Board board) {
        List<Position> moves = new ArrayList<>();
        int direction = (color == Color.WHITE) ? -1 : 1;
        int startRow = (color == Color.WHITE) ? 6 : 1;

        Position oneStep = new Position(from.getRow() + direction, from.getCol());
        if (oneStep.isValid() && board.getPiece(oneStep) == null) {
            moves.add(oneStep);

            if (from.getRow() == startRow) {
                Position twoSteps = new Position(from.getRow() + 2 * direction, from.getCol());
                if (twoSteps.isValid() && board.getPiece(twoSteps) == null) {
                    moves.add(twoSteps);
                }
            }
        }

        int[] captureCols = {-1, 1};
        for (int colOffset : captureCols) {
            Position capturePos = new Position(from.getRow() + direction, from.getCol() + colOffset);
            if (capturePos.isValid()) {
                Piece targetPiece = board.getPiece(capturePos);
                if (targetPiece != null && targetPiece.getColor() != this.color) {
                    moves.add(capturePos);
                }
            }
        }

        return moves;
    }

    @Override
    public String getSymbol() {
        return color == Color.WHITE ? "♙" : "♟";
    }

    @Override
    public PieceType getType() {
        return PieceType.PAWN;
    }
}
