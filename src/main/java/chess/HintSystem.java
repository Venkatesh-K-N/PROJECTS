package chess;

public class HintSystem {
    private ChessAI ai;
    private int hintsRemaining;
    private final int MAX_HINTS = 3;
    
    public HintSystem() {
        this.ai = new ChessAI(ChessAI.DifficultyLevel.HARD);
        this.hintsRemaining = MAX_HINTS;
    }
    
    public Move getHint(Game game, Color playerColor) {
        if (hintsRemaining <= 0) {
            return null;
        }
        
        hintsRemaining--;
        return ai.getBestMove(game, playerColor);
    }
    
    public int getHintsRemaining() {
        return hintsRemaining;
    }
    
    public void resetHints() {
        hintsRemaining = MAX_HINTS;
    }
    
    public boolean hasHintsRemaining() {
        return hintsRemaining > 0;
    }
}