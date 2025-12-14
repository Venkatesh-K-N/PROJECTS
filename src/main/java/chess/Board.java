package chess;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private final Piece[][] board;

    public Board() {
        board = new Piece[8][8];
        initializeBoard();
    }

    private void initializeBoard() {
        // Clear the board first
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                board[row][col] = null;
            }
        }

        // Set up black pieces (top - row 0)
        board[0][0] = new Rook(Color.BLACK);
        board[0][1] = new Knight(Color.BLACK);
        board[0][2] = new Bishop(Color.BLACK);
        board[0][3] = new Queen(Color.BLACK);
        board[0][4] = new King(Color.BLACK);
        board[0][5] = new Bishop(Color.BLACK);
        board[0][6] = new Knight(Color.BLACK);
        board[0][7] = new Rook(Color.BLACK);

        for (int i = 0; i < 8; i++) {
            board[1][i] = new Pawn(Color.BLACK);
        }

        // Set up white pieces (bottom - row 7)
        for (int i = 0; i < 8; i++) {
            board[6][i] = new Pawn(Color.WHITE);
        }

        board[7][0] = new Rook(Color.WHITE);
        board[7][1] = new Knight(Color.WHITE);
        board[7][2] = new Bishop(Color.WHITE);
        board[7][3] = new Queen(Color.WHITE);
        board[7][4] = new King(Color.WHITE);
        board[7][5] = new Bishop(Color.WHITE);
        board[7][6] = new Knight(Color.WHITE);
        board[7][7] = new Rook(Color.WHITE);
    }

    public Piece getPiece(Position position) {
        if (!position.isValid()) return null;
        return board[position.getRow()][position.getCol()];
    }

    public void setPiece(Position position, Piece piece) {
        if (position.isValid()) {
            board[position.getRow()][position.getCol()] = piece;
        }
    }

    // NEW: Method to place a piece without marking it as moved
    public void placePiece(Position position, Piece piece) {
        if (position.isValid()) {
            board[position.getRow()][position.getCol()] = piece;
        }
    }

    public void movePiece(Position from, Position to) {
        Piece piece = getPiece(from);
        if (piece != null) {
            setPiece(to, piece);
            setPiece(from, null);
            piece.setMoved(true);
        }
    }

    public Position findKing(Color color) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Position pos = new Position(row, col);
                Piece piece = getPiece(pos);
                if (piece != null && piece.getType() == PieceType.KING && piece.getColor() == color) {
                    return pos;
                }
            }
        }
        return null;
    }

    // NEW: Reset the board to initial state
    public void reset() {
        initializeBoard();
    }

    public void display() {
        System.out.println("\n  a b c d e f g h");
        System.out.println("  ---------------");
        for (int row = 0; row < 8; row++) {
            System.out.print((8 - row) + "|");
            for (int col = 0; col < 8; col++) {
                Piece piece = board[row][col];
                if (piece == null) {
                    System.out.print("· ");
                } else {
                    System.out.print(piece.getSymbol() + " ");
                }
            }
            System.out.println("|" + (8 - row));
        }
        System.out.println("  ---------------");
        System.out.println("  a b c d e f g h\n");
    }

    public Board copy() {
        Board newBoard = new Board();
        // Clear the new board first
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = this.board[row][col];
                if (piece != null) {
                    Piece newPiece = createPieceCopy(piece);
                    newBoard.board[row][col] = newPiece;
                } else {
                    newBoard.board[row][col] = null;
                }
            }
        }
        return newBoard;
    }

    private Piece createPieceCopy(Piece piece) {
        Piece newPiece;
        switch (piece.getType()) {
            case KING: 
                newPiece = new King(piece.getColor());
                break;
            case QUEEN: 
                newPiece = new Queen(piece.getColor());
                break;
            case ROOK: 
                newPiece = new Rook(piece.getColor());
                break;
            case BISHOP: 
                newPiece = new Bishop(piece.getColor());
                break;
            case KNIGHT: 
                newPiece = new Knight(piece.getColor());
                break;
            case PAWN: 
                newPiece = new Pawn(piece.getColor());
                break;
            default: 
                return null;
        }
        newPiece.setMoved(piece.hasMoved());
        return newPiece;
    }

    // NEW: Get all pieces of a specific color
    public List<Position> getPiecesByColor(Color color) {
        List<Position> pieces = new ArrayList<>();
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Position pos = new Position(row, col);
                Piece piece = getPiece(pos);
                if (piece != null && piece.getColor() == color) {
                    pieces.add(pos);
                }
            }
        }
        return pieces;
    }
    
    // Add after existing methods
    /**
     * Retrieves the piece at the given row and column indices.
     * @param row The row index (0-7).
     * @param col The column index (0-7).
     * @return The Piece object, or null if the position is invalid or empty.
     */
    public Piece getPieceAt(int row, int col) {
        if (row >= 0 && row < 8 && col >= 0 && col < 8) {
            return board[row][col];
        }
        return null;
    }

    /**
     * Checks if the given row and column indices are within the board bounds.
     * @param row The row index (0-7).
     * @param col The column index (0-7).
     * @return true if the position is valid, false otherwise.
     */
    public boolean isValidPosition(int row, int col) {
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }
}