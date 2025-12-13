package chess;

import java.util.ArrayList;
import java.util.List;

public class Bishop extends Piece {
    public Bishop(Color color) {
        super(color);
    }

    @Override
    public List<Position> getPossibleMoves(Position from, Board board) {
        List<Position> moves = new ArrayList<>();
        int[][] directions = {{-1, -1}, {-1, 1}, {1, -1}, {1, 1}};

        for (int[] dir : directions) {
            int newRow = from.getRow() + dir[0];
            int newCol = from.getCol() + dir[1];

            while (true) {
                Position newPos = new Position(newRow, newCol);
                if (!newPos.isValid()) break;

                Piece targetPiece = board.getPiece(newPos);
                if (targetPiece == null) {
                    moves.add(newPos);
                } else {
                    if (targetPiece.getColor() != this.color) {
                        moves.add(newPos);
                    }
                    break;
                }

                newRow += dir[0];
                newCol += dir[1];
            }
        }

        return moves;
    }

    @Override
    public String getSymbol() {
        return color == Color.WHITE ? "♗" : "♝";
    }

    @Override
    public PieceType getType() {
        return PieceType.BISHOP;
    }
}
