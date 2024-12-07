package multi.gameproject.ui;

import multi.gameproject.logic.GameController;
import multi.gameproject.object.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.awt.event.*;

public class GameView extends JFrame {
    private static final double LEFT_TEXT_AREA_RATIO = 0.03;
    private static final double RIGHT_TEXT_AREA_RATIO = 0.30;
    private static final double CENTER_TEXT_AREA_RATIO = 0.67;
    private static final double SCORE_PANEL_WIDTH_RATIO = 0.75;
    private static final double SCORE_PANEL_HEIGHT_RATIO = 0.09;

    private static final int FRAME_WIDTH_SIZE = 1000;
    private static final int FRAME_HEIGHT_SIZE = 600;
    private static final int SCORE_PANEL_X_OFFSET = 43;
    private static final int SCORE_PANEL_Y_OFFSET = 30;

    private JPanel centerPanel;
    private JPanel scorePanel;

    private JTextArea leftTextArea;
    private JTextArea rightTextArea;
    private JTextArea centerTextArea;
    private JLayeredPane layeredPane;

    private int rightTextAreaWidth;
    private int leftTextAreaWidth;
    private int centerTextAreaWidth;
    private int scorePanelWidth;
    private int scorePanelHeight;

    private int cols;
    private int rows;

    private StringBuilder centerContent;

    private int enemySpeed = 500;
    private Timer gameTimer;
    private Timer enemyTimer;
    private Timer bulletTimer;


    private GameController gameController;

    public GameView(GameController gameController) {
        this.gameController = gameController;
        createFrame();

        gameTimer = new Timer(60, e -> fillCenterTextArea(centerTextArea.getWidth(), centerTextArea.getHeight(), centerTextArea));

        enemyTimer = new Timer(enemySpeed, e -> updateEnemies());

        bulletTimer = new Timer(100, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateBullets();
                updateEnemyBullets();
            }
        });

        gameTimer.start();
        enemyTimer.start();
        bulletTimer.start();
    }

    private void createFrame() {
        setTitle("Space Invader");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(FRAME_WIDTH_SIZE, FRAME_HEIGHT_SIZE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        leftTextArea = createTextArea();
        rightTextArea = createTextArea();
        centerTextArea = createTextArea();

        centerPanel = createCenterPanel();

        setTextAreaSize();

        createJLayeredPanel();

        centerPanel.add(centerTextArea, BorderLayout.CENTER);
        add(leftTextArea, BorderLayout.WEST);
        add(centerPanel, BorderLayout.CENTER);
        add(layeredPane, BorderLayout.EAST);

        pack();

        centerPanel.requestFocusInWindow();

        setVisible(true);
        updatePanels();
    }

    private void createJLayeredPanel() {
        layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(rightTextAreaWidth, getHeight()));
        layeredPane.setLayout(null);

        rightTextArea.setBounds(0, 0, rightTextAreaWidth, getHeight());
        layeredPane.add(rightTextArea, JLayeredPane.DEFAULT_LAYER);

        createScorePanel();
        scorePanel.setBounds(0, 10, rightTextAreaWidth, scorePanelHeight);
        layeredPane.add(scorePanel, JLayeredPane.PALETTE_LAYER);
    }

    private JTextArea createTextArea() {
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFocusable(false);
        textArea.setFont(new Font("Consolas", Font.PLAIN, 18));
        textArea.setBackground(Color.white);
        textArea.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        return textArea;
    }

    private JPanel createCenterPanel() {
        centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setFocusable(true);
        setKeyBinding(centerPanel);
        centerPanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                gameController.keyReleased(e);
                updatePanels();
            }

            @Override
            public void keyPressed(KeyEvent e) {
                gameController.handlePlayerMovement(e.getKeyCode(), cols, rows);
                updatePanels();
            }
        });

        return centerPanel;
    }

    private JPanel createScorePanel() {
        scorePanel = new JPanel();
        scorePanel.setFocusable(false);
        scorePanel.setBackground(Color.WHITE);
        scorePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        JLabel scoreLabel = new JLabel("SCORE: " + gameController.getScore());
        scoreLabel.setFont(new Font("Consolas", Font.PLAIN, 18));
        scorePanel.add(scoreLabel);

        return scorePanel;
    }

    private void updateScore() {
        JLabel scoreLabel = (JLabel) scorePanel.getComponent(0);
        scoreLabel.setText("SCORE: " + gameController.getScore());
    }

    private void bindingKey(InputMap inputMap, ActionMap actionMap, int keyCode) {
        KeyStroke keyStroke = KeyStroke.getKeyStroke(keyCode, 0);

        inputMap.put(keyStroke, String.valueOf(keyCode));
        actionMap.put(String.valueOf(keyCode), new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameController.handlePlayerMovement(keyCode, centerTextArea.getWidth(), centerTextArea.getHeight());
            }
        });
    }

    public void setKeyBinding(JPanel centerPanel) {
        InputMap inputMap = centerPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = centerPanel.getActionMap();

        bindingKey(inputMap, actionMap, KeyEvent.VK_UP);
        bindingKey(inputMap, actionMap, KeyEvent.VK_DOWN);
        bindingKey(inputMap, actionMap, KeyEvent.VK_RIGHT);
        bindingKey(inputMap, actionMap, KeyEvent.VK_LEFT);
        bindingKey(inputMap, actionMap, KeyEvent.VK_SPACE);
    }

    private void setTextAreaSize() {
        leftTextAreaWidth = (int) (getWidth() * LEFT_TEXT_AREA_RATIO);
        rightTextAreaWidth = (int) (getWidth() * RIGHT_TEXT_AREA_RATIO);
        centerTextAreaWidth = (int) (getWidth() * CENTER_TEXT_AREA_RATIO);

        leftTextArea.setPreferredSize(new Dimension(leftTextAreaWidth, FRAME_HEIGHT_SIZE));
        rightTextArea.setPreferredSize(new Dimension(rightTextAreaWidth, FRAME_HEIGHT_SIZE));
        centerPanel.setPreferredSize(new Dimension(centerTextAreaWidth, FRAME_HEIGHT_SIZE));

        scorePanelWidth = (int) (rightTextArea.getWidth() * SCORE_PANEL_WIDTH_RATIO);
        scorePanelHeight = (int) (rightTextArea.getHeight() * SCORE_PANEL_HEIGHT_RATIO);
    }

    private void updatePanels() {
        setTextAreaSize();

        rightTextArea.setBounds(0, 0, rightTextArea.getWidth(), getHeight());

        scorePanel.setBounds(SCORE_PANEL_X_OFFSET, SCORE_PANEL_Y_OFFSET, scorePanelWidth, scorePanelHeight);

        fillSideTextArea(leftTextArea.getWidth(), leftTextArea.getHeight(), leftTextArea);
        fillSideTextArea(rightTextArea.getWidth(), rightTextArea.getHeight(), rightTextArea);
        fillCenterTextArea(centerTextArea.getWidth(), centerTextArea.getHeight(), centerTextArea);
    }

    private void SetColsAndRows(int panelWidth, int panelHeight, JTextArea textArea) {
        FontMetrics fontMetrics = textArea.getFontMetrics(textArea.getFont());
        int charWidth = fontMetrics.charWidth('A');
        int charHeight = fontMetrics.getHeight();

        cols = panelWidth / charWidth;
        rows = panelHeight / charHeight;
    }

    private void fillSideTextArea(int panelWidth, int panelHeight, JTextArea textArea) {
        SetColsAndRows(panelWidth, panelHeight, textArea);

        StringBuilder content = getStringDecoration(textArea, rows, cols);

        textArea.setText(content.toString());
    }

    private void fillCenterTextArea(int panelWidth, int panelHeight, JTextArea textArea) {
        SetColsAndRows(panelWidth, panelHeight, textArea);

        centerContent = new StringBuilder(rows * cols);
        for (int i = 0; i < rows * cols; i++) {
            centerContent.append(" ");
        }

        drawPlayer(centerContent, cols);
        drawBullets(centerContent, cols);
        drawEnemies(centerContent, cols);
        drawEnemyBullets(centerContent, cols);

        if (!gameController.getGameStatus()) {
            stopGame();
            displayAgainMessage(centerContent, "loss");
        } else if (gameController.getEnemies().isEmpty()) {
            stopGame();
            displayAgainMessage(centerContent, "win");
        }

        for (int i = 0; i < rows; i++) {
            centerContent.append(centerContent, i * cols, (i + 1) * cols).append("\n");
        }
        textArea.setText(centerContent.toString());
    }

    private void drawPlayer(StringBuilder content, int cols) {
        GameObject player = gameController.getPlayer();
        int startPosition = player.getPosY() * cols + player.getPosX();
        if (startPosition + player.getImage().length() <= content.length()) {
            content.replace(startPosition, startPosition + player.getImage().length(), player.getImage());
        }
    }

    private void drawBullets(StringBuilder content, int cols) {
        List<BulletObject> bullets = gameController.getBullets();

        for (int i = bullets.size() - 1; i >= 0; i--) {
            BulletObject bullet = bullets.get(i);
            int bulletPos = bullet.getPosY() * cols + bullet.getPosX();

            if (bulletPos >= 0 && bulletPos < content.length()) {
                content.replace(bulletPos, bulletPos + bullet.getImage().length(), bullet.getImage());
            }
        }
    }

    private void drawEnemyBullets(StringBuilder content, int cols) {
        List<EnemyBulletObject> enemyBullets = gameController.getEnemyBullet();

        for (int i = enemyBullets.size() - 1; i >= 0; i--) {
            EnemyBulletObject enemyBullet = enemyBullets.get(i);
            int enemyBulletPos = enemyBullet.getPosY() * cols + enemyBullet.getPosX();

            if (enemyBulletPos >= 0 && enemyBulletPos < content.length()) {
                content.replace(enemyBulletPos, enemyBulletPos + enemyBullet.getImage().length(), enemyBullet.getImage());
            }
        }
    }

    private void drawEnemies(StringBuilder content, int cols) {
        List<EnemyObject> enemies = gameController.getEnemies();

        for (int i = enemies.size() - 1; i >= 0; i--) {
            EnemyObject enemy = enemies.get(i);
            int enemyPos = enemy.getPosY() * cols + enemy.getPosX();

            if (enemyPos >= 0 && enemyPos < content.length()) {
                content.replace(enemyPos, enemyPos + enemy.getImage().length(), enemy.getImage());
            }
        }
    }

    private void updateEnemyBullets() {
        gameController.shootEnemyBullet();
        gameController.moveEnemyBullets(rows);
    }

    private void updateEnemies() {
        boolean changeRows = gameController.moveEnemies(cols, enemySpeed);
        if (changeRows) {
            updateEnemySpeed();
        }
    }

    private void updateEnemySpeed() {
        enemySpeed = gameController.getEnemySpeed();
        enemyTimer.setDelay(enemySpeed);
    }

    private void updateBullets() {
        gameController.moveBullets();
        updateScore();
    }

    private void displayAgainMessage(StringBuilder content, String result) {
        String[] message = {
                "╔═════════════════════════╗",
                result.equals("win") ? "║        You Win!!        ║" : "║        You Lose!!       ║",
                "║                         ║",
                "║    Play Again?(Y / N)   ║",
                "╚═════════════════════════╝"
        };
        int msgPosY = 10;
        int msgPosX = 19;
        for (int i = 0; i < message.length; i++) {
            int msgStartPos = (msgPosY + i) * cols + msgPosX;
            content.replace(msgStartPos, msgStartPos + message[i].length(), message[i]);
        }

        centerTextArea.setFocusable(true);
        centerTextArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                char keyChar = Character.toLowerCase(e.getKeyChar());
                if (keyChar == 'y') {
                    resetGame();
                } else if (keyChar == 'n') {
                    System.exit(0);
                }
            }
        });
        centerTextArea.requestFocusInWindow();
    }

    private void stopGame() {
        enemyTimer.stop();
        bulletTimer.stop();
        gameTimer.stop();
    }

    private StringBuilder getStringDecoration(JTextArea textArea, int rows, int cols) {
        int hashtagSpace = 1;
        StringBuilder content = new StringBuilder(rows * cols);

        for (int i = 0; i < rows; i++) {
            if (textArea == rightTextArea) {
                if (i == rows - 4) {
                    content.append("#");
                    for (int j = hashtagSpace; j < 15; j++) {
                        content.append(".");
                    }
                    content.append("by jonghwi");
                    for (int j = 25; j < cols; j++) {
                        content.append(".");
                    }
                } else {
                    content.append("#");
                    for (int j = 0; j < cols - hashtagSpace; j++) {
                        content.append(".");
                    }
                }
            }

            if (textArea == leftTextArea) {
                for (int j = 0; j < cols - hashtagSpace; j++) {
                    content.append(".");
                }
                content.append("#");
            }

            content.append("\n");
        }
        return content;
    }

    private void resetGame() {
        gameController.resetObject();
        gameController.spawnEnemies();
        enemySpeed = 500;
        enemyTimer.setDelay(enemySpeed);
        centerTextArea.removeKeyListener(centerTextArea.getKeyListeners()[0]);
        centerTextArea.setFocusable(false);
        gameTimer.restart();
        enemyTimer.restart();
        bulletTimer.restart();
    }
}