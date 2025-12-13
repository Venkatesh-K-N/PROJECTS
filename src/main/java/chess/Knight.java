package chess;

import java.util.ArrayList;
import java.util.List;

public class Knight extends Piece {
    public Knight(Color color) {
        super(color);
    }

    @Override
    public List<Position> getPossibleMoves(Position from, Board board) {
        List<Position> moves = new ArrayList<>();
        int[][] knightMoves = {
            {-2, -1}, {-2, 1}, {-1, -2}, {-1, 2},
            {1, -2}, {1, 2}, {2, -1}, {2, 1}
        };

        for (int[] move : knightMoves) {
            Position newPos = new Position(from.getRow() + move[0], from.getCol() + move[1]);
            if (newPos.isValid()) {
                Piece targetPiece = board.getPiece(newPos);
                if (targetPiece == null || targetPiece.getColor() != this.color) {
                    moves.add(newPos);
                }
            }
        }

        return moves;
    }

    @Override
    public String getSymbol() {
        return color == Color.WHITE ? "♘" : "♞";
    }

    @Override
    public PieceType getType() {
        return PieceType.KNIGHT;
    }
}
