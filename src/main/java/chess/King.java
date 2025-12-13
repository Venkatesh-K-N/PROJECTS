package chess;

import java.util.ArrayList;
import java.util.List;

public class King extends Piece {
    public King(Color color) {
        super(color);
    }

    @Override
    public List<Position> getPossibleMoves(Position from, Board board) {
        List<Position> moves = new ArrayList<>();
        int[][] directions = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};

        for (int[] dir : directions) {
            Position newPos = new Position(from.getRow() + dir[0], from.getCol() + dir[1]);
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
        return color == Color.WHITE ? "♔" : "♚";
    }

    @Override
    public PieceType getType() {
        return PieceType.KING;
    }
}
