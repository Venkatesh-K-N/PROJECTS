package chess.gui;

import chess.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.*;

public class BoardPanel extends JPanel {
    public static final int TILE_SIZE = 120;
    public static final int COORDINATE_MARGIN = 20;

    private Game game;
    private GameInfoPanel infoPanel;
    private chess.gui.ChessGUI chessGUI;

    private Position selected = null;
    private final Set<Position> highlighted = new HashSet<>();
    private chess.Move hintMove = null;

    private final Map<String, BufferedImage> imageCache = new HashMap<>();

    private boolean gameOver = false;

    private final java.awt.Color LIGHT_SQUARE = new java.awt.Color(240, 217, 181);
    private final java.awt.Color DARK_SQUARE = new java.awt.Color(181, 136, 99);
    private final java.awt.Color CHECK_COLOR = new java.awt.Color(255, 0, 0, 150);
    private final java.awt.Color SELECTION_COLOR = new java.awt.Color(255, 255, 0, 120);
    private final java.awt.Color MOVE_HIGHLIGHT = new java.awt.Color(0, 150, 0, 150);
    private final java.awt.Color TURN_INDICATOR_BG = new java.awt.Color(0, 0, 0, 120);
    private static final java.awt.Color HINT_COLOR = new java.awt.Color(255, 255, 0, 180);

    public BoardPanel(Game game, GameInfoPanel infoPanel) {
        this.game = game;
        this.infoPanel = infoPanel;

        setPreferredSize(new Dimension(8 * TILE_SIZE + 2 * COORDINATE_MARGIN,
                8 * TILE_SIZE + 2 * COORDINATE_MARGIN));
        loadAllPieceImages();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleClick(e.getX(), e.getY());
            }
        });

        putClientProperty("JScrollPane.fastWheelScrolling", Boolean.TRUE);
    }

    public void setGame(Game game) {
        this.game = game;
        this.selected = null;
        this.highlighted.clear();
        this.hintMove = null;
        this.gameOver = false;
        repaint();
    }

    public void setInfoPanel(GameInfoPanel infoPanel) {
        this.infoPanel = infoPanel;
    }
    
    public void setChessGUI(chess.gui.ChessGUI gui) {
        this.chessGUI = gui;
    }

    public void resetBoard() {
        selected = null;
        highlighted.clear();
        hintMove = null;
        gameOver = false;
        repaint();
    }

    public void highlightHint(chess.Move hint) {
        this.hintMove = hint;
        
        if (hint != null) {
            selected = null;
            highlighted.clear();
            repaint();
        }
        
        // Clear hint after 3 seconds
        javax.swing.Timer timer = new javax.swing.Timer(3000, e -> {
            hintMove = null;
            repaint();
        });
        timer.setRepeats(false);
        timer.start();
    }

    public void executeAIMove(chess.Move move) {
        if (move == null) return;
        
        Position from = move.getFrom();
        Position to = move.getTo();
        
        Piece piece = game.getBoard().getPiece(from);
        Piece captured = game.getBoard().getPiece(to);
        
        boolean isCapture = captured != null;
        boolean moveSuccess = game.makeMove(from, to);
        
        if (moveSuccess) {
            // Play sound
            if (isCapture) {
                SoundPlayer.playSound("capture.wav");
            } else {
                SoundPlayer.playSound("move.wav");
            }
            
            handlePawnPromotion(to, piece);
            
            if (infoPanel != null) {
                infoPanel.updateAll();
            }
            
            // Update history on left panel
            if (chessGUI != null) {
                chessGUI.updateHistory();
            }
            
            // Check sound
            if (game.isInCheck(game.getCurrentPlayer())) {
                SoundPlayer.playSound("check.wav");
            }
            
            checkForGameOver();
            repaint();
        }
    }

    private void loadAllPieceImages() {
        String[] types = {"pawn", "rook", "knight", "bishop", "queen", "king"};
        String[] colors = {"white", "black"};
        for (String t : types) {
            for (String c : colors) {
                String filename = t + "_" + c + ".png";
                BufferedImage img = loadImageResource(filename);
                if (img != null) imageCache.put(t + "_" + c, img);
            }
        }
    }

    private BufferedImage loadImageResource(String filename) {
        try {
            URL res = getClass().getResource("/pieces/" + filename);
            if (res != null) return ImageIO.read(res);
        } catch (Exception ignored) {}

        try {
            File f = new File("pieces/" + filename);
            if (f.exists()) return ImageIO.read(f);
        } catch (Exception ignored) {}

        try {
            File f = new File("src/pieces/" + filename);
            if (f.exists()) return ImageIO.read(f);
        } catch (Exception ignored) {}

        System.err.println("Could not load image: " + filename);
        BufferedImage placeholder = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = placeholder.createGraphics();
        g2d.setColor(java.awt.Color.RED);
        g2d.fillRect(0, 0, 100, 100);
        g2d.dispose();
        return placeholder;
    }

    private String getPieceImageName(Piece piece) {
        if (piece == null) return null;
        return piece.getType().name().toLowerCase() + "_" + piece.getColor().name().toLowerCase();
    }

    private boolean isPieceWhite(Piece piece) {
        return piece != null && piece.getColor() == chess.Color.WHITE;
    }

    @Override
    protected void paintComponent(Graphics g0) {
        super.paintComponent(g0);
        Graphics2D g = (Graphics2D) g0;

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int pieceSize = TILE_SIZE - 20;
        int offset = (TILE_SIZE - pieceSize) / 2;

        g.setFont(new Font("SansSerif", Font.BOLD, 16));

        for (int row = 0; row < 8; row++) {
            int displayRow = 8 - row;
            String number = String.valueOf(displayRow);
            g.setColor(java.awt.Color.BLACK);

            g.drawString(number, 5,
                    COORDINATE_MARGIN + row * TILE_SIZE + TILE_SIZE / 2 + 5);

            g.drawString(number, 8 * TILE_SIZE + COORDINATE_MARGIN + 5,
                    COORDINATE_MARGIN + row * TILE_SIZE + TILE_SIZE / 2 + 5);
        }

        for (int col = 0; col < 8; col++) {
            char letter = (char) ('a' + col);
            g.drawString(String.valueOf(letter),
                    COORDINATE_MARGIN + col * TILE_SIZE + TILE_SIZE / 2 - 5,
                    8 * TILE_SIZE + COORDINATE_MARGIN + 15);

            g.drawString(String.valueOf(letter),
                    COORDINATE_MARGIN + col * TILE_SIZE + TILE_SIZE / 2 - 5,
                    15);
        }

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Position pos = new Position(row, col);
                boolean isLight = (row + col) % 2 == 0;

                g.setColor(isLight ? LIGHT_SQUARE : DARK_SQUARE);
                g.fillRect(COORDINATE_MARGIN + col * TILE_SIZE,
                        COORDINATE_MARGIN + row * TILE_SIZE,
                        TILE_SIZE, TILE_SIZE);

                if (!gameOver) {
                    chess.Color currentPlayer = game.getCurrentPlayer();
                    boolean inCheck = game.isInCheck(currentPlayer);

                    Position kingPos = game.getBoard().findKing(currentPlayer);
                    if (kingPos != null && kingPos.equals(pos) && inCheck) {
                        g.setColor(CHECK_COLOR);
                        g.fillRect(COORDINATE_MARGIN + col * TILE_SIZE,
                                COORDINATE_MARGIN + row * TILE_SIZE,
                                TILE_SIZE, TILE_SIZE);
                    }
                }

                if (selected != null && selected.equals(pos)) {
                    g.setColor(SELECTION_COLOR);
                    g.fillRect(COORDINATE_MARGIN + col * TILE_SIZE,
                            COORDINATE_MARGIN + row * TILE_SIZE,
                            TILE_SIZE, TILE_SIZE);
                }

                if (highlighted.contains(pos)) {
                    Piece targetPiece = game.getBoard().getPiece(pos);
                    boolean isCapture = targetPiece != null;

                    if (isCapture) {
                        g.setColor(MOVE_HIGHLIGHT);
                        g.setStroke(new BasicStroke(5));
                        g.drawRect(COORDINATE_MARGIN + col * TILE_SIZE + 2,
                                COORDINATE_MARGIN + row * TILE_SIZE + 2,
                                TILE_SIZE - 4, TILE_SIZE - 4);
                        g.setStroke(new BasicStroke(1));
                    } else {
                        int dotSize = TILE_SIZE / 3;
                        int dotX = COORDINATE_MARGIN + col * TILE_SIZE + (TILE_SIZE / 2) - (dotSize / 2);
                        int dotY = COORDINATE_MARGIN + row * TILE_SIZE + (TILE_SIZE / 2) - (dotSize / 2);
                        g.setColor(MOVE_HIGHLIGHT);
                        g.fillOval(dotX, dotY, dotSize, dotSize);
                    }
                }

                // Draw hint highlight
                if (hintMove != null && 
                    (pos.equals(hintMove.getFrom()) || pos.equals(hintMove.getTo()))) {
                    g.setColor(HINT_COLOR);
                    g.setStroke(new BasicStroke(4));
                    g.drawRect(COORDINATE_MARGIN + col * TILE_SIZE + 3,
                            COORDINATE_MARGIN + row * TILE_SIZE + 3,
                            TILE_SIZE - 6, TILE_SIZE - 6);
                    g.setStroke(new BasicStroke(1));
                }

                Piece piece = game.getBoard().getPiece(pos);
                if (piece != null) {
                    String imageName = getPieceImageName(piece);
                    BufferedImage img = imageCache.get(imageName);
                    if (img != null) {
                        g.drawImage(img,
                                COORDINATE_MARGIN + col * TILE_SIZE + offset,
                                COORDINATE_MARGIN + row * TILE_SIZE + offset,
                                pieceSize, pieceSize, null);
                    } else {
                        g.setColor(isPieceWhite(piece) ? java.awt.Color.BLACK : java.awt.Color.WHITE);
                        g.setFont(new Font("SansSerif", Font.BOLD, TILE_SIZE / 2));
                        String symbol = piece.getSymbol();
                        FontMetrics fm = g.getFontMetrics();
                        int textX = COORDINATE_MARGIN + col * TILE_SIZE +
                                (TILE_SIZE - fm.stringWidth(symbol)) / 2;
                        int textY = COORDINATE_MARGIN + row * TILE_SIZE +
                                (TILE_SIZE + fm.getAscent()) / 2 - fm.getDescent();
                        g.drawString(symbol, textX, textY);
                    }
                }
            }
        }

        int boxX = COORDINATE_MARGIN + 8 * TILE_SIZE - 150;
        int boxY = COORDINATE_MARGIN + 8 * TILE_SIZE - 35;

        g.setColor(TURN_INDICATOR_BG);
        g.fillRect(boxX, boxY, 140, 30);

        boolean whiteTurn = game.getCurrentPlayer() == chess.Color.WHITE;
        g.setColor(whiteTurn ? java.awt.Color.WHITE : java.awt.Color.BLACK);
        g.fillRect(boxX + 5, boxY + 5, 20, 20);

        g.setFont(new Font("SansSerif", Font.BOLD, 14));
        g.setColor(java.awt.Color.WHITE);
        g.drawString(gameOver ? "GAME OVER" :
                        (whiteTurn ? "White to move" : "Black to move"),
                boxX + 35, boxY + 20);
    }

    private void handleClick(int mouseX, int mouseY) {
        if (gameOver) return;

        int col = (mouseX - COORDINATE_MARGIN) / TILE_SIZE;
        int row = (mouseY - COORDINATE_MARGIN) / TILE_SIZE;

        if (row < 0 || row > 7 || col < 0 || col > 7) return;

        Position clicked = new Position(row, col);
        Piece clickedPiece = game.getBoard().getPiece(clicked);

        // Clear hint when player clicks
        hintMove = null;

        // ===========================
        // 1. SELECT PIECE
        // ===========================
        if (selected == null) {
            if (clickedPiece == null) return;
            if (clickedPiece.getColor() != game.getCurrentPlayer()) return;

            selected = clicked;
            highlighted.clear();
            highlighted.addAll(game.getValidMoves(selected));

            // 🔊 SOUND: selecting piece
            SoundPlayer.playSound("select.wav");

            repaint();
            return;
        }

        // 2. Deselect
        if (selected.equals(clicked)) {
            selected = null;
            highlighted.clear();
            repaint();
            return;
        }

        // 3. performing move
        Piece selectedPiece = game.getBoard().getPiece(selected);
        if (selectedPiece == null) {
            selected = null;
            highlighted.clear();
            repaint();
            return;
        }

        if (!highlighted.contains(clicked)) {

            // 🔊 invalid move
            SoundPlayer.playSound("invalid.wav");

            if (clickedPiece != null && clickedPiece.getColor() == game.getCurrentPlayer()) {
                selected = clicked;
                highlighted.clear();
                highlighted.addAll(game.getValidMoves(selected));
            } else {
                selected = null;
                highlighted.clear();
            }
            repaint();
            return;
        }

        // ===========================
        // LEGAL MOVE
        // ===========================
        boolean isCapture = clickedPiece != null;
        boolean moveSuccess = game.makeMove(selected, clicked);

        if (moveSuccess) {

            // 🔊 capture / move sound
            if (isCapture) SoundPlayer.playSound("capture.wav");
            else SoundPlayer.playSound("move.wav");

            handlePawnPromotion(clicked, selectedPiece);
            selected = null;
            highlighted.clear();

            if (infoPanel != null) infoPanel.updateAll();
            
            // Update history on left panel
            if (chessGUI != null) chessGUI.updateHistory();

            // CHECK SOUND
            if (game.isInCheck(game.getCurrentPlayer())) {
                SoundPlayer.playSound("check.wav");
            }

            checkForGameOver();
            
            // Trigger AI move if needed
            if (infoPanel != null) {
                infoPanel.triggerAIMove();
            }
        }

        repaint();
    }

    private void handlePawnPromotion(Position pos, Piece movedPiece) {
        if (movedPiece.getType() == PieceType.PAWN) {
            int promotionRow = (movedPiece.getColor() == chess.Color.WHITE) ? 0 : 7;

            if (pos.getRow() == promotionRow) {

                // 🔊 Promotion sound
                SoundPlayer.playSound("promote.wav");

                String[] options = {"Queen", "Rook", "Bishop", "Knight"};
                int choice = JOptionPane.showOptionDialog(this,
                        "Choose promotion piece:",
                        "Pawn Promotion",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null, options, options[0]);

                PieceType newType =
                        (choice == 1) ? PieceType.ROOK :
                        (choice == 2) ? PieceType.BISHOP :
                        (choice == 3) ? PieceType.KNIGHT :
                                PieceType.QUEEN;

                promotePawn(pos, newType);
            }
        }
    }

    private void promotePawn(Position pos, PieceType newType) {
        Piece pawn = game.getBoard().getPiece(pos);
        if (pawn != null && pawn.getType() == PieceType.PAWN) {
            chess.Color color = pawn.getColor();
            Piece newPiece;

            switch (newType) {
                case QUEEN: newPiece = new Queen(color); break;
                case ROOK: newPiece = new Rook(color); break;
                case BISHOP: newPiece = new Bishop(color); break;
                case KNIGHT: newPiece = new Knight(color); break;
                default: newPiece = new Queen(color);
            }

            game.getBoard().setPiece(pos, newPiece);
        }
    }

    private void checkForGameOver() {
        GameStatus status = game.getStatus();

        if (status != GameStatus.IN_PROGRESS) {
            gameOver = true;

            // 🔊 game end sound
            SoundPlayer.playSound("checkmate.wav");

            String message =
                    (status == GameStatus.WHITE_WINS) ? "White wins by CHECKMATE! Play again?" :
                            (status == GameStatus.BLACK_WINS) ? "Black wins by CHECKMATE! Play again?" :
                                    "Draw by STALEMATE! Play again?";

            int choice = JOptionPane.showConfirmDialog(
                    this, message,
                    "Game Over", JOptionPane.YES_NO_OPTION);

            if (choice == JOptionPane.YES_OPTION) {
                resetGame();
            }
        }
    }

    private void resetGame() {
        Game newGame = new Game();
        setGame(newGame);
        if (infoPanel != null) {
            infoPanel.setGame(newGame);
            infoPanel.updateAll();
        }
    }
}