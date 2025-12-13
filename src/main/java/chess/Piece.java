package chess;

import java.util.List;

public abstract class Piece {
    protected final Color color;
    protected boolean hasMoved;

    public Piece(Color color) {
        this.color = color;
        this.hasMoved = false;
    }

    public Color getColor() {
        return color;
    }

    public boolean hasMoved() {
        return hasMoved;
    }

    public void setMoved(boolean moved) {
        this.hasMoved = moved;
    }

    public abstract List<Position> getPossibleMoves(Position from, Board board);

    public abstract String getSymbol();

    public abstract PieceType getType();

    // NEW: Helper method to check if a position is valid and empty or contains opponent's piece
    protected boolean isValidTarget(Position pos, Board board, boolean canCapture) {
        if (!pos.isValid()) return false;
        
        Piece target = board.getPiece(pos);
        if (target == null) return true; // Empty square
        if (canCapture && target.getColor() != this.color) return true; // Opponent's piece
        
        return false;
    }

    // NEW: Helper method to add moves in a direction until blocked
    protected void addMovesInDirection(Position from, Board board, List<Position> moves, 
                                     int rowDir, int colDir, int maxSteps) {
        int currentRow = from.getRow() + rowDir;
        int currentCol = from.getCol() + colDir;
        int steps = 0;
        
        while (steps < maxSteps && currentRow >= 0 && currentRow < 8 && currentCol >= 0 && currentCol < 8) {
            Position pos = new Position(currentRow, currentCol);
            Piece target = board.getPiece(pos);
            
            if (target == null) {
                moves.add(pos); // Empty square
            } else if (target.getColor() != this.color) {
                moves.add(pos); // Capture opponent's piece
                break;
            } else {
                break; // Own piece blocking
            }
            
            currentRow += rowDir;
            currentCol += colDir;
            steps++;
        }
    }
}