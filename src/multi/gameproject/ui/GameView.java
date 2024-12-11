package multi.gameproject.ui;

import multi.gameproject.Game;
import multi.gameproject.object.GameModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

public class GameView extends JPanel {
    private final HashSet<Integer> keyPressed = new HashSet<>();
    private JPanel leftPanel;
    private JPanel rightPanel;
    private JPanel scorePanel;
    private JPanel topScorePanel;
    private JPanel namePanel;
    private JLabel scoreLabel;
    private JLabel topScoreLabel;
    private GameModel model;
    private int width;
    private int height;
    private boolean isFiring;

    public GameView(int width, int height, GameModel model) {
        this.width = width;
        this.height = height;
        this.model = model;
        addKeyBindings();
        setLayout(new BorderLayout());
        setLeftPanel();
        add(leftPanel, BorderLayout.CENTER);
        setRightPanel();
        add(rightPanel, BorderLayout.EAST);
        this.setPreferredSize(new Dimension(this.width, this.height));
    }

    public Set<Integer> getKeyCode() {
        return keyPressed;
    }

    private void addKeyBindings() {
        addKeyBindings(KeyEvent.VK_DOWN);
        addKeyBindings(KeyEvent.VK_LEFT);
        addKeyBindings(KeyEvent.VK_RIGHT);
        addKeyBindings(KeyEvent.VK_UP);
        addKeyBindings(KeyEvent.VK_SPACE);
    }

    public void keyReleased(int keyCode) {
        if (keyCode == KeyEvent.VK_SPACE && isFiring) {
            isFiring = false;
        }
        keyPressed.remove(keyCode);
    }

    public boolean isPressedSpace() {
        if (keyPressed.contains(KeyEvent.VK_SPACE) && !isFiring) {
            isFiring = true;
            return true;
        }
        return false;
    }

    private void addKeyBindings(int keyCode) {
        getInputMap().put(KeyStroke.getKeyStroke(keyCode, 0, false), keyCode + "Pressed");
        getActionMap().put(keyCode + "Pressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                keyPressed.add(keyCode);
            }
        });

        getInputMap().put(KeyStroke.getKeyStroke(keyCode, 0, true), keyCode + "Released");
        getActionMap().put(keyCode + "Released", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                keyReleased(keyCode);
            }
        });
    }

    public void repaintLeftPanel() {
        leftPanel.repaint();
    }

    public void repaintTopScorePanel() {
        topScorePanel.repaint();
    }

    public void repaintScorePanel() {
        scorePanel.repaint();
    }

    public int getLeftPanelWidth() {
        return leftPanel.getWidth();
    }

    public int getLeftPanelHeight() {
        return leftPanel.getHeight();
    }


    public void setLeftPanel() {
        leftPanel = new LeftPanel(model.getPlayer(), model.getBullets(), model.getEnemies(), model.getEnemyBullets(), model.getBonusList());
        leftPanel.setBackground(Color.BLACK);
        leftPanel.repaint();
    }

    public void displayAgainMessage(String msg) {
        int result = JOptionPane.showConfirmDialog(this, "Play again?", "You " + msg, JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            Window topLevelWindow = SwingUtilities.getWindowAncestor(this);
            if (topLevelWindow != null) {
                topLevelWindow.dispose();
            }
            new Game();
        } else if (result == JOptionPane.NO_OPTION) {
            System.exit(0);
        }
    }

    public void setTopScore(int topScore) {
        System.out.println(topScore);
        topScoreLabel.setText(String.valueOf(topScore));
    }

    public void setScore(int score) {
        scoreLabel.setText(String.valueOf(score));
    }

    private void setScorePanel() {
        scorePanel = new JPanel();
        scorePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        scorePanel.setOpaque(false);

        JLabel scoreStrLabel = new JLabel("SCORE:");
        scoreStrLabel.setFont(new Font("Consolas", Font.BOLD, 30));
        scoreStrLabel.setForeground(Color.WHITE);
        scorePanel.add(scoreStrLabel);

        scoreLabel = new JLabel();
        scoreLabel.setFont(new Font("Consolas", Font.BOLD, 30));
        scoreLabel.setForeground(Color.RED);
        scorePanel.add(scoreLabel);
    }

    private void setTopScorePanel() {
        topScorePanel = new JPanel();
        topScorePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        topScorePanel.setOpaque(false);

        JLabel topScoreStrLabel = new JLabel("TOP SCORE:");
        topScoreStrLabel.setFont(new Font("Consolas", Font.BOLD, 18));
        topScoreStrLabel.setForeground(Color.WHITE);
        topScorePanel.add(topScoreStrLabel);

        topScoreLabel = new JLabel();
        topScoreLabel.setFont(new Font("Consolas", Font.BOLD, 18));
        topScoreLabel.setForeground(Color.RED);
        topScorePanel.add(topScoreLabel);
    }

    private void setNamePanel() {
        namePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        namePanel.setOpaque(false);
        JLabel myNameLabel = new JLabel("by JongHwi.Yoon");
        myNameLabel.setFont(new Font("Consolas", Font.BOLD, 12));
        myNameLabel.setForeground(Color.WHITE);
        namePanel.add(myNameLabel);
    }

    public void setRightPanel() {
        rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(Color.BLUE);

        rightPanel.add(Box.createRigidArea(new Dimension(0, 70)));

        setScorePanel();
        rightPanel.add(scorePanel);

        rightPanel.add(Box.createRigidArea(new Dimension(0, 50)));

        setTopScorePanel();
        rightPanel.add(topScorePanel);

        rightPanel.add(Box.createRigidArea(new Dimension(0, 150)));

        ImageIcon originalIcon = new ImageIcon("images/MainImg.png");
        Image startImage = originalIcon.getImage().getScaledInstance(250, 180, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(startImage);
        JLabel mainImgLabel = new JLabel(scaledIcon);
        mainImgLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        rightPanel.add(mainImgLabel);

        rightPanel.add(Box.createRigidArea(new Dimension(0, 100)));

        setNamePanel();
        rightPanel.add(namePanel);

        rightPanel.add(Box.createVerticalGlue());
    }
}
