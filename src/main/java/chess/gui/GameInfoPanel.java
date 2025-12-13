package chess.gui;

import chess.Game;
import chess.Move;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GameInfoPanel extends JPanel {
    private Game game;

    private final JLabel turnLabel;
    private final JLabel statusLabel;
    private final JTextArea historyArea;
    private final JButton restartButton;

    private Runnable restartCallback;

    public GameInfoPanel(Game game) {
        this.game = game;

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(250, 600));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ===== TOP PANEL =====
        JPanel top = new JPanel();
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));

        turnLabel = new JLabel();
        turnLabel.setFont(new Font("Arial", Font.BOLD, 16));
        top.add(turnLabel);

        statusLabel = new JLabel();
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        top.add(statusLabel);

        top.add(Box.createVerticalStrut(10));

        restartButton = new JButton("Restart Game");
        restartButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        restartButton.addActionListener(e -> {
            if (restartCallback != null) restartCallback.run();
        });

        top.add(restartButton);

        add(top, BorderLayout.NORTH);

        // ===== MOVE HISTORY =====
        historyArea = new JTextArea();
        historyArea.setEditable(false);
        historyArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        JScrollPane scrollPane = new JScrollPane(historyArea);
        add(scrollPane, BorderLayout.CENTER);

        updateAll();
    }

    // Assign new Game instance during restart
    public void setGame(Game game) {
        this.game = game;
        updateAll();
    }

    public void setRestartCallback(Runnable callback) {
        this.restartCallback = callback;
    }

    // Update entire panel
    public void updateAll() {
        if (game == null) return;

        turnLabel.setText("Turn: " + game.getCurrentPlayer());
        statusLabel.setText("Status: " + game.getStatus());

        updateHistory();
    }

    // Update move history area
    private void updateHistory() {
        StringBuilder sb = new StringBuilder();
        List<Move> moves = game.getMoveHistory();

        int moveNum = 1;
        for (Move m : moves) {
            sb.append(moveNum++).append(". ").append(m.toString()).append("\n");
        }

        historyArea.setText(sb.toString());
        historyArea.setCaretPosition(historyArea.getDocument().getLength());
    }

    // Called by BoardPanel on checkmate, stalemate, etc.
    public void showGameOver(String message) {
        statusLabel.setText(message);
        turnLabel.setText("Game Over");
    }

    // General status printing
    public void showStatus(String msg) {
        statusLabel.setText(msg);
    }
}
