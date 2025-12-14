package chess;

import java.util.*;

public class ChessAI {
    private DifficultyLevel difficulty;
    private Random random;
    
    public enum DifficultyLevel {
        EASY, MEDIUM, HARD
    }
    
    public ChessAI(DifficultyLevel difficulty) {
        this.difficulty = difficulty;
        this.random = new Random();
    }
    
    public void setDifficulty(DifficultyLevel difficulty) {
        this.difficulty = difficulty;
    }
    
    // Main method - takes Game object
    public Move getBestMove(Game game, Color color) {
        List<Move> allMoves = getAllLegalMoves(game, color);
        
        if (allMoves.isEmpty()) {
            return null;
        }
        
        switch (difficulty) {
            case EASY:
                return getEasyMove(allMoves);
            case MEDIUM:
                return getMediumMove(allMoves);
            case HARD:
                return getHardMove(allMoves);
            default:
                return allMoves.get(0);
        }
    }
    
    private Move getEasyMove(List<Move> moves) {
        // Random moves with 30% capture preference
        List<Move> captures = new ArrayList<>();
        for (Move m : moves) {
            if (m.getCapturedPiece() != null) {
                captures.add(m);
            }
        }
        
        if (!captures.isEmpty() && random.nextDouble() < 0.3) {
            return captures.get(random.nextInt(captures.size()));
        }
        
        return moves.get(random.nextInt(moves.size()));
    }
    
    private Move getMediumMove(List<Move> moves) {
        // Evaluate moves and pick best one
        Move bestMove = null;
        int bestScore = Integer.MIN_VALUE;
        
        for (Move move : moves) {
            int score = evaluateMoveBasic(move);
            // Add randomness for variety
            score += random.nextInt(Math.max(1, score / 5 + 1)) - Math.max(1, score / 10);
            
            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }
        
        return bestMove;
    }
    
    private Move getHardMove(List<Move> moves) {
        // Similar to medium but with better evaluation
        Move bestMove = null;
        int bestScore = Integer.MIN_VALUE;
        
        for (Move move : moves) {
            int score = evaluateMoveAdvanced(move);
            
            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }
        
        return bestMove;
    }
    
    private int evaluateMoveBasic(Move move) {
        int score = 0;
        
        // Capture value
        if (move.getCapturedPiece() != null) {
            score += getPieceValue(move.getCapturedPiece().getType()) * 10;
        }
        
        // Center control bonus
        Position to = move.getTo();
        int toRow = to.getRow();
        int toCol = to.getCol();
        if (toRow >= 3 && toRow <= 4 && toCol >= 3 && toCol <= 4) {
            score += 5;
        }
        
        return score;
    }
    
    private int evaluateMoveAdvanced(Move move) {
        int score = evaluateMoveBasic(move);
        
        // Additional advanced evaluation
        Piece piece = move.getPiece();
        Position from = move.getFrom();
        Position to = move.getTo();
        
        // Piece development bonus
        if (piece.getType() == PieceType.KNIGHT || piece.getType() == PieceType.BISHOP) {
            int fromRow = from.getRow();
            if ((piece.getColor() == Color.BLACK && fromRow == 0) ||
                (piece.getColor() == Color.WHITE && fromRow == 7)) {
                score += 8; // Bonus for developing pieces
            }
        }
        
        // Pawn advancement
        if (piece.getType() == PieceType.PAWN) {
            if (piece.getColor() == Color.BLACK) {
                score += (to.getRow() - from.getRow()) * 2;
            } else {
                score += (from.getRow() - to.getRow()) * 2;
            }
        }
        
        return score;
    }
    
    private int getPieceValue(PieceType type) {
        switch (type) {
            case PAWN:   return 100;
            case KNIGHT: return 320;
            case BISHOP: return 330;
            case ROOK:   return 500;
            case QUEEN:  return 900;
            case KING:   return 20000;
            default:     return 0;
        }
    }
    
    private List<Move> getAllLegalMoves(Game game, Color color) {
        List<Move> moves = new ArrayList<>();
        Board board = game.getBoard();
        
        // Iterate through all board positions
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Position from = new Position(row, col);
                Piece piece = board.getPiece(from);
                
                // If there's a piece of the right color
                if (piece != null && piece.getColor() == color) {
                    // Get valid moves for this piece (returns Set)
                    Set<Position> validMoves = game.getValidMoves(from);
                    
                    // Create Move objects
                    for (Position to : validMoves) {
                        Piece captured = board.getPiece(to);
                        Move move = new Move(from, to, piece, captured);
                        moves.add(move);
                    }
                }
            }
        }
        
        return moves;
    }
}