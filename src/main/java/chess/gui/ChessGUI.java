package chess.gui;

import chess.Game;

import javax.swing.*;
import java.awt.*;

public class ChessGUI extends JFrame {

    private Game game;
    private BoardPanel boardPanel;
    private GameInfoPanel infoPanel;

    public ChessGUI() {
        super("Chess GUI");
        initUI();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initUI() {

        this.game = new Game();

        // Panels
        infoPanel = new GameInfoPanel(game);
        infoPanel.setPreferredSize(new Dimension(220, 600));  // Smaller Game Info Panel

        boardPanel = new BoardPanel(game, infoPanel);
        Dimension boardSize = boardPanel.getPreferredSize();

        JPanel leftSpace = new JPanel();
        leftSpace.setPreferredSize(new Dimension(200, boardSize.height));  // Future feature area
        leftSpace.setOpaque(false);

        // Panel that holds: LEFT SPACE | CENTERED BOARD | INFO PANEL
        JPanel layoutPanel = new JPanel(new BorderLayout());
        layoutPanel.add(leftSpace, BorderLayout.WEST);

        // Center wrapper to center board horizontally
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

        // Restart callback
        infoPanel.setRestartCallback(() -> {
            Game newGame = new Game();
            boardPanel.setGame(newGame);
            infoPanel.setGame(newGame);

            boardPanel.repaint();
            infoPanel.updateAll();
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ChessGUI::new);
    }
}
