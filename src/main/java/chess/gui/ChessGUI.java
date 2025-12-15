package chess.gui;

import chess.Game;
import chess.Move;
import java.awt.*;
import java.util.List;
import javax.swing.*;

public class ChessGUI extends JFrame {

    private Game game;
    private BoardPanel boardPanel;
    private GameInfoPanel infoPanel;
    private JTextArea leftHistoryArea;

    public ChessGUI() {
        super("Professional Chess Game");
        initUI();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initUI() {
        this.game = new Game();

        // LEFT PANEL - Move History
        JPanel leftPanel = createHistoryPanel();

        // RIGHT PANEL - AI Controls
        infoPanel = new GameInfoPanel(game);
        infoPanel.setPreferredSize(new Dimension(280, 600));

        // CENTER - Board
        boardPanel = new BoardPanel(game, infoPanel);

        // Connect panels
        infoPanel.setBoardPanel(boardPanel);

        // Main Layout: HISTORY | BOARD | AI CONTROLS
        JPanel layoutPanel = new JPanel(new BorderLayout());
        layoutPanel.add(leftPanel, BorderLayout.WEST);

        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.add(boardPanel);
        centerWrapper.setOpaque(false);
        layoutPanel.add(centerWrapper, BorderLayout.CENTER);

        layoutPanel.add(infoPanel, BorderLayout.EAST);

        // Scroll pane
        JScrollPane scrollPane = new JScrollPane(
                layoutPanel,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );

        scrollPane.getVerticalScrollBar().setUnitIncrement(40);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(40);

        getContentPane().add(scrollPane, BorderLayout.CENTER);

        // Pass reference to ChessGUI to BoardPanel
        boardPanel.setChessGUI(this);
        
        // Restart callback
        infoPanel.setRestartCallback(() -> {
            Game newGame = new Game();
            this.game = newGame;
            boardPanel.setGame(newGame);
            infoPanel.setGame(newGame);
            updateHistory();
            boardPanel.repaint();
            infoPanel.updateAll();
        });
    }

    private JPanel createHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(250, 600));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Title
        JLabel titleLabel = new JLabel("📜 Move History");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        panel.add(titleLabel, BorderLayout.NORTH);

        // History Area
        leftHistoryArea = new JTextArea();
        leftHistoryArea.setEditable(false);
        leftHistoryArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        leftHistoryArea.setLineWrap(false);
        JScrollPane scrollPane = new JScrollPane(leftHistoryArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    public void updateHistory() {
        StringBuilder sb = new StringBuilder();
        List<Move> moves = game.getMoveHistory();

        int moveNum = 1;
        for (int i = 0; i < moves.size(); i++) {
            if (i % 2 == 0) {
                sb.append(String.format("%3d. ", moveNum++));
            }
            sb.append(String.format("%-10s", moves.get(i).toString()));
            if (i % 2 == 0) {
                sb.append(" ");
            } else {
                sb.append("\n");
            }
        }

        leftHistoryArea.setText(sb.toString());
        leftHistoryArea.setCaretPosition(leftHistoryArea.getDocument().getLength());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new ChessGUI();
        });
    }
}