package chess.gui;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class TutorialDialog extends JDialog {
    private JTextArea contentArea;
    private JButton prevButton, nextButton, closeButton;
    private int currentLesson = 0;
    private List<TutorialLesson> lessons;
    private JLabel lessonLabel;
    
    public TutorialDialog(JFrame parent) {
        super(parent, "Chess Tutorial", true);
        setLayout(new BorderLayout(10, 10));
        setSize(700, 600);
        setLocationRelativeTo(parent);
        
        initializeLessons();
        createUI();
        loadLesson(0);
    }
    
    private void initializeLessons() {
        lessons = new ArrayList<>();
        
        lessons.add(new TutorialLesson(
            "Welcome to Chess!",
            "Chess is a strategic board game played between two players.\n\n" +
            "OBJECTIVE:\n" +
            "Checkmate your opponent's King - put it under attack with no escape.\n\n" +
            "SETUP:\n" +
            "• 8x8 board with 64 squares\n" +
            "• Each player starts with 16 pieces\n" +
            "• White moves first\n" +
            "• Players alternate turns\n\n" +
            "BASIC RULES:\n" +
            "• You must move on your turn\n" +
            "• You cannot move into check\n" +
            "• If in check, you must escape\n" +
            "• Game ends in checkmate, stalemate, or draw"
        ));
        
        lessons.add(new TutorialLesson(
            "The Pieces - Part 1",
            "♔ KING (Value: Priceless)\n" +
            "• Moves one square in any direction\n" +
            "• Cannot move into check\n" +
            "• Must be protected!\n\n" +
            "♕ QUEEN (Value: 9 points)\n" +
            "• Most powerful piece\n" +
            "• Moves any distance in any direction\n" +
            "• Combines rook + bishop power\n\n" +
            "♖ ROOK (Value: 5 points)\n" +
            "• Moves any distance horizontally or vertically\n" +
            "• Powerful in endgames\n" +
            "• Best on open files"
        ));
        
        lessons.add(new TutorialLesson(
            "The Pieces - Part 2",
            "♗ BISHOP (Value: 3 points)\n" +
            "• Moves any distance diagonally\n" +
            "• Stays on same color squares\n" +
            "• Long-range attacker\n\n" +
            "♘ KNIGHT (Value: 3 points)\n" +
            "• Moves in L-shape: 2+1 squares\n" +
            "• ONLY piece that jumps over others\n" +
            "• Great for forks and tricks\n\n" +
            "♙ PAWN (Value: 1 point)\n" +
            "• Moves forward one square\n" +
            "• Captures diagonally\n" +
            "• Promotes to Queen at end\n" +
            "• Cannot move backward"
        ));
        
        lessons.add(new TutorialLesson(
            "Check, Checkmate & Stalemate",
            "CHECK:\n" +
            "• King is under attack\n" +
            "• Must be resolved by:\n" +
            "  1. Moving the king\n" +
            "  2. Blocking the attack\n" +
            "  3. Capturing attacker\n\n" +
            "CHECKMATE:\n" +
            "• King in check with no escape\n" +
            "• Game over - attacker wins!\n\n" +
            "STALEMATE:\n" +
            "• No legal moves but NOT in check\n" +
            "• Game is a DRAW\n\n" +
            "OTHER DRAWS:\n" +
            "• Not enough pieces to mate\n" +
            "• Same position 3 times\n" +
            "• 50 moves without capture/pawn move"
        ));
        
        lessons.add(new TutorialLesson(
            "Strategy Tips",
            "OPENING (First 10 moves):\n" +
            "• Control the center (e4, d4, e5, d5)\n" +
            "• Develop knights and bishops\n" +
            "• Castle early for king safety\n" +
            "• Don't move same piece twice\n\n" +
            "MIDDLEGAME:\n" +
            "• Look for tactics (forks, pins, skewers)\n" +
            "• Create threats\n" +
            "• Protect your king\n" +
            "• Trade when ahead\n\n" +
            "ENDGAME:\n" +
            "• Activate your king\n" +
            "• Push passed pawns\n" +
            "• Rooks on 7th rank are strong\n\n" +
            "GENERAL:\n" +
            "• Think before moving\n" +
            "• Look for checks, captures, threats\n" +
            "• Consider opponent's threats"
        ));
        
        lessons.add(new TutorialLesson(
            "Using This Game",
            "GAME MODES:\n" +
            "• 2-Player: Play with a friend\n" +
            "• vs AI Easy: Good for learning\n" +
            "• vs AI Medium: Moderate challenge\n" +
            "• vs AI Hard: Advanced play\n\n" +
            "FEATURES:\n" +
            "• Click piece to select\n" +
            "• Green circles = valid moves\n" +
            "• Use hints wisely (3 per game)\n" +
            "• Move history tracks all moves\n" +
            "• Sound effects for feedback\n\n" +
            "SHORTCUTS:\n" +
            "• Click 'Change Mode' to switch\n" +
            "• Click 'Get Hint' for help\n" +
            "• Click 'Restart Game' to start over\n\n" +
            "Good luck and have fun!"
        ));
    }
    
    private void createUI() {
        contentArea = new JTextArea();
        contentArea.setEditable(false);
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        contentArea.setFont(new Font("Arial", Font.PLAIN, 14));
        contentArea.setMargin(new Insets(15, 15, 15, 15));
        
        JScrollPane scrollPane = new JScrollPane(contentArea);
        add(scrollPane, BorderLayout.CENTER);
        
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        prevButton = new JButton("← Previous");
        prevButton.addActionListener(e -> previousLesson());
        navPanel.add(prevButton);
        
        lessonLabel = new JLabel("Lesson 1 of " + lessons.size());
        lessonLabel.setFont(new Font("Arial", Font.BOLD, 14));
        navPanel.add(lessonLabel);
        
        nextButton = new JButton("Next →");
        nextButton.addActionListener(e -> nextLesson());
        navPanel.add(nextButton);
        
        closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        navPanel.add(closeButton);
        
        add(navPanel, BorderLayout.SOUTH);
    }
    
    private void loadLesson(int index) {
        if (index >= 0 && index < lessons.size()) {
            currentLesson = index;
            TutorialLesson lesson = lessons.get(index);
            
            StringBuilder content = new StringBuilder();
            content.append("═══════════════════════════════════════\n");
            content.append("  ").append(lesson.title).append("\n");
            content.append("═══════════════════════════════════════\n\n");
            content.append(lesson.content);
            
            contentArea.setText(content.toString());
            contentArea.setCaretPosition(0);
            
            lessonLabel.setText("Lesson " + (index + 1) + " of " + lessons.size());
            prevButton.setEnabled(index > 0);
            nextButton.setEnabled(index < lessons.size() - 1);
        }
    }
    
    private void previousLesson() {
        if (currentLesson > 0) {
            loadLesson(currentLesson - 1);
        }
    }
    
    private void nextLesson() {
        if (currentLesson < lessons.size() - 1) {
            loadLesson(currentLesson + 1);
        }
    }
    
    private static class TutorialLesson {
        String title;
        String content;
        
        TutorialLesson(String title, String content) {
            this.title = title;
            this.content = content;
        }
    }
}