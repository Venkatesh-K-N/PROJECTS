package chess.gui;

import chess.*;
import java.awt.*;
import javax.swing.*;

public class GameInfoPanel extends JPanel {
    private Game game;
    private ChessAI chessAI;
    private HintSystem hintSystem;
    private boolean vsAI = false;
    private ChessAI.DifficultyLevel aiDifficulty = ChessAI.DifficultyLevel.MEDIUM;

    // UI Components
    private final JLabel turnLabel;
    private final JLabel statusLabel;
    private final JLabel gameModeLabel;
    private final JLabel hintsLabel;
    private final JButton restartButton;
    private final JButton changeModeButton;
    private final JButton hintButton;
    private final JButton tutorialButton;
    private final JComboBox<String> difficultyCombo;

    private Runnable restartCallback;
    private BoardPanel boardPanel;

    public GameInfoPanel(Game game) {
        this.game = game;
        this.hintSystem = new HintSystem();

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(280, 600));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ===== MAIN PANEL =====
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // Title
        JLabel titleLabel = new JLabel("♔ Chess Game ♚");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(15));

        // Game Mode Label
        gameModeLabel = new JLabel("Mode: 2-Player");
        gameModeLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gameModeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(gameModeLabel);
        mainPanel.add(Box.createVerticalStrut(10));

        // Turn Label
        turnLabel = new JLabel();
        turnLabel.setFont(new Font("Arial", Font.BOLD, 16));
        turnLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(turnLabel);

        // Status Label
        statusLabel = new JLabel();
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(statusLabel);
        mainPanel.add(Box.createVerticalStrut(15));

        // Separator
        JSeparator sep1 = new JSeparator();
        sep1.setMaximumSize(new Dimension(260, 2));
        mainPanel.add(sep1);
        mainPanel.add(Box.createVerticalStrut(10));

        // AI Difficulty Section
        JLabel diffLabel = new JLabel("AI Difficulty:");
        diffLabel.setFont(new Font("Arial", Font.BOLD, 12));
        diffLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(diffLabel);
        mainPanel.add(Box.createVerticalStrut(5));

        String[] difficulties = {"Easy", "Medium", "Hard"};
        difficultyCombo = new JComboBox<>(difficulties);
        difficultyCombo.setSelectedIndex(1);
        difficultyCombo.setMaximumSize(new Dimension(200, 30));
        difficultyCombo.setAlignmentX(Component.CENTER_ALIGNMENT);
        difficultyCombo.addActionListener(e -> {
            String selected = (String) difficultyCombo.getSelectedItem();
            if ("Easy".equals(selected)) {
                aiDifficulty = ChessAI.DifficultyLevel.EASY;
            } else if ("Medium".equals(selected)) {
                aiDifficulty = ChessAI.DifficultyLevel.MEDIUM;
            } else {
                aiDifficulty = ChessAI.DifficultyLevel.HARD;
            }
            if (chessAI != null) {
                chessAI.setDifficulty(aiDifficulty);
            }
        });
        difficultyCombo.setEnabled(false);
        mainPanel.add(difficultyCombo);
        mainPanel.add(Box.createVerticalStrut(10));

        // Change Mode Button
        changeModeButton = new JButton("Change Mode");
        changeModeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        changeModeButton.setMaximumSize(new Dimension(200, 35));
        changeModeButton.addActionListener(e -> showGameModeSelection());
        mainPanel.add(changeModeButton);
        mainPanel.add(Box.createVerticalStrut(10));

        // Separator
        JSeparator sep2 = new JSeparator();
        sep2.setMaximumSize(new Dimension(260, 2));
        mainPanel.add(sep2);
        mainPanel.add(Box.createVerticalStrut(10));

        // Hints Section
        hintsLabel = new JLabel("Hints: 3 remaining");
        hintsLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        hintsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(hintsLabel);
        mainPanel.add(Box.createVerticalStrut(5));

        hintButton = new JButton("💡 Get Hint");
        hintButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        hintButton.setMaximumSize(new Dimension(200, 35));
        hintButton.addActionListener(e -> showHint());
        mainPanel.add(hintButton);
        mainPanel.add(Box.createVerticalStrut(5));

        // Tutorial Button
        tutorialButton = new JButton("📚 Tutorial");
        tutorialButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        tutorialButton.setMaximumSize(new Dimension(200, 35));
        tutorialButton.addActionListener(e -> showTutorial());
        mainPanel.add(tutorialButton);
        mainPanel.add(Box.createVerticalStrut(5));

        // Restart Button
        restartButton = new JButton("🔄 Restart Game");
        restartButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        restartButton.setMaximumSize(new Dimension(200, 35));
        restartButton.addActionListener(e -> {
            if (restartCallback != null) {
                hintSystem.resetHints();
                updateHintsLabel();
                restartCallback.run();
            }
        });
        mainPanel.add(restartButton);
        mainPanel.add(Box.createVerticalStrut(10));

        add(mainPanel, BorderLayout.CENTER);

        updateAll();
    }

    public void setBoardPanel(BoardPanel boardPanel) {
        this.boardPanel = boardPanel;
    }

    public void setGame(Game game) {
        this.game = game;
        updateAll();
    }

    public void setRestartCallback(Runnable callback) {
        this.restartCallback = callback;
    }

    private void showGameModeSelection() {
        Object[] options = {
            "2-Player Local",
            "vs AI (Easy)",
            "vs AI (Medium)",
            "vs AI (Hard)"
        };

        int choice = JOptionPane.showOptionDialog(
            this,
            "Select Game Mode:",
            "Game Mode Selection",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[1]
        );

        if (choice >= 0) {
            if (choice == 0) {
                vsAI = false;
                gameModeLabel.setText("Mode: 2-Player");
                difficultyCombo.setEnabled(false);
            } else {
                vsAI = true;
                switch (choice) {
                    case 1: 
                        aiDifficulty = ChessAI.DifficultyLevel.EASY;
                        difficultyCombo.setSelectedIndex(0);
                        break;
                    case 2: 
                        aiDifficulty = ChessAI.DifficultyLevel.MEDIUM;
                        difficultyCombo.setSelectedIndex(1);
                        break;
                    case 3: 
                        aiDifficulty = ChessAI.DifficultyLevel.HARD;
                        difficultyCombo.setSelectedIndex(2);
                        break;
                }
                chessAI = new ChessAI(aiDifficulty);
                String mode = "vs AI (" + options[choice].toString().substring(6);
                gameModeLabel.setText("Mode: " + mode);
                difficultyCombo.setEnabled(true);
            }
            
            if (restartCallback != null) {
                hintSystem.resetHints();
                updateHintsLabel();
                restartCallback.run();
            }
        }
    }

    private void showHint() {
        if (!hintSystem.hasHintsRemaining()) {
            JOptionPane.showMessageDialog(
                this,
                "No hints remaining! You started with 3 hints.",
                "No Hints",
                JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        Move hint = hintSystem.getHint(game, game.getCurrentPlayer());
        if (hint != null) {
            updateHintsLabel();
            
            if (boardPanel != null) {
                boardPanel.highlightHint(hint);
            }

            String message = String.format(
                "Hint: Move from %s to %s\n\nHints remaining: %d",
                hint.getFrom().toChessNotation(),
                hint.getTo().toChessNotation(),
                hintSystem.getHintsRemaining()
            );

            JOptionPane.showMessageDialog(
                this,
                message,
                "Hint",
                JOptionPane.INFORMATION_MESSAGE
            );
        } else {
            JOptionPane.showMessageDialog(
                this,
                "No hint available for current position.",
                "Hint",
                JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    private void showTutorial() {
        TutorialDialog tutorial = new TutorialDialog((JFrame) SwingUtilities.getWindowAncestor(this));
        tutorial.setVisible(true);
    }

    private void updateHintsLabel() {
        hintsLabel.setText("Hints: " + hintSystem.getHintsRemaining() + " remaining");
        hintButton.setEnabled(hintSystem.hasHintsRemaining());
    }

    public void updateAll() {
        if (game == null) return;

        chess.Color current = game.getCurrentPlayer();
        turnLabel.setText("Turn: " + current);
        turnLabel.setForeground(current == chess.Color.WHITE ? 
            new java.awt.Color(50, 150, 50) : new java.awt.Color(150, 50, 50));

        GameStatus status = game.getStatus();
        if (status == GameStatus.WHITE_WINS) {
            statusLabel.setText("White wins!");
            statusLabel.setForeground(java.awt.Color.RED);
        } else if (status == GameStatus.BLACK_WINS) {
            statusLabel.setText("Black wins!");
            statusLabel.setForeground(java.awt.Color.RED);
        } else if (status == GameStatus.STALEMATE) {
            statusLabel.setText("STALEMATE!");
            statusLabel.setForeground(java.awt.Color.BLUE);
        } else if (game.isInCheck(current)) {
            statusLabel.setText("Check!");
            statusLabel.setForeground(java.awt.Color.ORANGE);
        } else {
            statusLabel.setText("Status: " + status);
            statusLabel.setForeground(java.awt.Color.GRAY);
        }

        updateHintsLabel();
    }

    public void showGameOver(String message) {
        statusLabel.setText(message);
        turnLabel.setText("Game Over");
    }

    public void showStatus(String msg) {
        statusLabel.setText(msg);
    }

    public void triggerAIMove() {
        if (vsAI && game.getCurrentPlayer() == chess.Color.BLACK && 
            game.getStatus() == GameStatus.IN_PROGRESS) {
            
            Timer timer = new Timer(500, e -> makeAIMove());
            timer.setRepeats(false);
            timer.start();
        }
    }

    private void makeAIMove() {
        if (chessAI != null && boardPanel != null) {
            Move aiMove = chessAI.getBestMove(game, chess.Color.BLACK);
            if (aiMove != null) {
                boardPanel.executeAIMove(aiMove);
            }
        }
    }
}