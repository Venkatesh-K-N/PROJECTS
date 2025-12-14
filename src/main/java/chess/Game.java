package chess;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Game {
    private final Board board;
    private Color currentPlayer;
    private final List<Move> moveHistory;
    private GameStatus status;

    public Game() {
        this.board = new Board();
        this.currentPlayer = Color.WHITE;
        this.moveHistory = new ArrayList<>();
        this.status = GameStatus.IN_PROGRESS;
    }

    public Board getBoard() {
        return board;
    }

    public Color getCurrentPlayer() {
        return currentPlayer;
    }

    public GameStatus getStatus() {
        return status;
    }
    
    /**
     * Checks if the game has concluded (Checkmate or Stalemate).
     * This method is useful for the GUI to know when to stop interaction.
     * @return true if the game is over, false otherwise.
     */
    public boolean isGameOver() {
        return status != GameStatus.IN_PROGRESS;
    }

    public boolean makeMove(Position from, Position to) {
        Piece piece = board.getPiece(from);

        if (piece == null) {
            System.out.println("No piece at that position!");
            return false;
        }

        if (piece.getColor() != currentPlayer) {
            System.out.println("It's not your turn!");
            return false;
        }

        // Use Set for valid moves to match BoardPanel expectations
        Set<Position> validMoves = getValidMoves(from);
        if (!validMoves.contains(to)) {
            System.out.println("Invalid move!");
            return false;
        }

        Piece capturedPiece = board.getPiece(to);
        
        // Temporarily store the piece being moved before it's moved on the board copy/reference
        Piece movingPiece = piece; 
        
        board.movePiece(from, to);
        
        // CRITICAL CHANGE: Using the updated Move constructor (from, to, piece, capturedPiece)
        moveHistory.add(new Move(from, to, movingPiece, capturedPiece));

        // Check for pawn promotion
        if (piece.getType() == PieceType.PAWN && (to.getRow() == 0 || to.getRow() == 7)) {
            // Auto-promote to queen for now - the GUI will handle interactive promotion
            promotePawn(to, PieceType.QUEEN);
        }

        // Check game status
        if (isCheckmate(currentPlayer.opposite())) {
            status = currentPlayer == Color.WHITE ? GameStatus.WHITE_WINS : GameStatus.BLACK_WINS;
            System.out.println("Checkmate! " + currentPlayer + " wins!");
        } else if (isStalemate(currentPlayer.opposite())) {
            status = GameStatus.STALEMATE;
            System.out.println("Stalemate! It's a draw!");
        } else if (isInCheck(currentPlayer.opposite())) {
            System.out.println(currentPlayer.opposite() + " is in check!");
        }

        currentPlayer = currentPlayer.opposite();
        return true;
    }

    // Updated to return Set<Position> to match BoardPanel expectations
    public Set<Position> getValidMoves(Position from) {
        Piece piece = board.getPiece(from);
        if (piece == null) return new HashSet<>();

        List<Position> possibleMoves = piece.getPossibleMoves(from, board);
        Set<Position> validMoves = new HashSet<>();

        for (Position to : possibleMoves) {
            if (!wouldBeInCheck(from, to, piece.getColor())) {
                validMoves.add(to);
            }
        }

        return validMoves;
    }

    private boolean wouldBeInCheck(Position from, Position to, Color color) {
        Board tempBoard = board.copy();
        tempBoard.movePiece(from, to);

        Position kingPos = tempBoard.findKing(color);
        if (kingPos == null) return true;

        return isPositionUnderAttack(kingPos, color.opposite(), tempBoard);
    }

    public boolean isInCheck(Color color) {
        Position kingPos = board.findKing(color);
        if (kingPos == null) return false;
        return isPositionUnderAttack(kingPos, color.opposite(), board);
    }

    private boolean isPositionUnderAttack(Position position, Color attackingColor, Board checkBoard) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Position pos = new Position(row, col);
                Piece piece = checkBoard.getPiece(pos);
                if (piece != null && piece.getColor() == attackingColor) {
                    // Assuming piece.getPossibleMoves includes attacks and captures
                    List<Position> moves = piece.getPossibleMoves(pos, checkBoard); 
                    if (moves.contains(position)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isCheckmate(Color color) {
        if (!isInCheck(color)) return false;
        return hasNoValidMoves(color);
    }

    private boolean isStalemate(Color color) {
        if (isInCheck(color)) return false;
        return hasNoValidMoves(color);
    }

    private boolean hasNoValidMoves(Color color) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Position pos = new Position(row, col);
                Piece piece = board.getPiece(pos);
                if (piece != null && piece.getColor() == color) {
                    Set<Position> validMoves = getValidMoves(pos);
                    if (!validMoves.isEmpty()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    // NEW: Method for pawn promotion
    public void promotePawn(Position pos, PieceType newType) {
        Piece pawn = board.getPiece(pos);
        if (pawn != null && pawn.getType() == PieceType.PAWN) {
            Piece newPiece = createPiece(newType, pawn.getColor());
            board.placePiece(pos, newPiece);
            System.out.println("Pawn promoted to " + newType);
        }
    }

    // NEW: Helper method to create pieces
    private Piece createPiece(PieceType type, Color color) {
        // NOTE: This assumes constructors for Queen, Rook, Bishop, Knight are accessible
        switch (type) {
            case QUEEN: return new Queen(color);
            case ROOK: return new Rook(color);
            case BISHOP: return new Bishop(color);
            case KNIGHT: return new Knight(color);
            // Defaulting to Queen for any unsupported type in promotion context
            default: return new Queen(color); 
        }
    }

    // NEW: Reset game method
    public void reset() {
        // Clear the board and reinitialize
        board.reset();
        currentPlayer = Color.WHITE;
        moveHistory.clear();
        status = GameStatus.IN_PROGRESS;
    }

    public void displayBoard() {
        board.display();
        System.out.println("Current player: " + currentPlayer);
        System.out.println("Status: " + status);
    }

    public List<Move> getMoveHistory() {
        return new ArrayList<>(moveHistory);
    }
}