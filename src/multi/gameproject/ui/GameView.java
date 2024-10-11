package multi.gameproject.ui;

import multi.gameproject.logic.GameController;
import multi.gameproject.object.BulletObject;
import multi.gameproject.object.GameObject;

import javax.swing.*;
import java.awt.*;
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
    private JLayeredPane layeredPane;

    private int rightTextAreaWidth;
    private int leftTextAreaWidth;
    private int centerTextAreaWidth;
    private int scorePanelWidth;
    private int scorePanelHeight;

    private GameController gameController;

    public GameView(GameController gameController) {
        this.gameController = gameController;
        createFrame();
    }

    private void createFrame() {
        setTitle("Space Invader");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(FRAME_WIDTH_SIZE, FRAME_HEIGHT_SIZE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        leftTextArea = createTextArea();
        rightTextArea = createTextArea();
        centerPanel = createCenterPanel();

        setTextAreaSize();

        createJLayeredPanel();

        add(leftTextArea, BorderLayout.WEST);
        add(centerPanel, BorderLayout.CENTER);
        add(layeredPane, BorderLayout.EAST);

        pack();

        centerPanel.requestFocusInWindow();

        setVisible(true);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updatePanels();
            }
        });
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
        centerPanel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                GameObject player = gameController.getPlayer();
                drawGameObject(g, player);

                GameObject[] enemies = setEnemiesPosition();
                for (GameObject enemy : enemies) {
                    drawGameObject(g, enemy);
                }
                for (BulletObject bullet : gameController.getBullets()) {
                    drawGameObject(g, bullet);
                }
            }
        };
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setFocusable(true);
        setKeyBinding(centerPanel);
        centerPanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                gameController.keyReleased(e);
            }
        });


        return centerPanel;
    }

    private void drawGameObject(Graphics g, GameObject gameObject) {
        if (gameObject != null) {
            int objectWidth = getObjectWidth(gameObject);
            int objectHeight = getObjectHeight();

            gameObject.setWidth(objectWidth);
            gameObject.setHeight(objectHeight);
            g.drawString(gameObject.getImage(), gameObject.getPosX(), gameObject.getPosY() + objectHeight);
        }
    }

    private JPanel createScorePanel() {
        scorePanel = new JPanel();
        scorePanel.setFocusable(false);
        scorePanel.setBackground(Color.WHITE);
        scorePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        int scores = 0;

        JLabel scoreLabel = new JLabel("SCORE: " + scores);
        scoreLabel.setFont(new Font("Consolas", Font.PLAIN, 18));
        scorePanel.add(scoreLabel);

        return scorePanel;
    }

    public int getObjectWidth(GameObject gameObject) {
        FontMetrics fm = getGraphics().getFontMetrics();
        return fm.stringWidth(gameObject.getImage());
    }

    public int getObjectHeight() {
        FontMetrics fm = getGraphics().getFontMetrics();
        return fm.getHeight();
    }

    private void bindingKey(InputMap inputMap, ActionMap actionMap, int keyCode) {
        KeyStroke keyStroke = KeyStroke.getKeyStroke(keyCode, 0);

        inputMap.put(keyStroke, String.valueOf(keyCode));
        actionMap.put(String.valueOf(keyCode), new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameController.handlePlayerMovement(keyCode, centerPanel.getWidth(), centerPanel.getHeight());
                centerPanel.repaint();
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

        leftTextArea.setPreferredSize(new Dimension(leftTextAreaWidth, getHeight()));
        rightTextArea.setPreferredSize(new Dimension(rightTextAreaWidth, getHeight()));
        centerPanel.setPreferredSize(new Dimension(centerTextAreaWidth, getHeight()));

        scorePanelWidth = (int) (rightTextArea.getWidth() * SCORE_PANEL_WIDTH_RATIO);
        scorePanelHeight = (int) (rightTextArea.getHeight() * SCORE_PANEL_HEIGHT_RATIO);
    }

    private void updatePanels() {
        setTextAreaSize();

        rightTextArea.setBounds(0, 0, rightTextArea.getWidth(), getHeight());

        scorePanel.setBounds(SCORE_PANEL_X_OFFSET, SCORE_PANEL_Y_OFFSET, scorePanelWidth, scorePanelHeight);

        fillTextArea(leftTextArea.getWidth(), leftTextArea.getHeight(), leftTextArea);
        fillTextArea(rightTextArea.getWidth(), rightTextArea.getHeight(), rightTextArea);
    }

    private void fillTextArea(int panelWidth, int panelHeight, JTextArea textArea) {
        FontMetrics fontMetrics = textArea.getFontMetrics(textArea.getFont());
        int charWidth = fontMetrics.charWidth('A');
        int charHeight = fontMetrics.getHeight();

        int cols = panelWidth / charWidth;
        int rows = panelHeight / charHeight;

        StringBuilder content = getStringBuilder(textArea, rows, cols);

        textArea.setText(content.toString());
    }

    private StringBuilder getStringBuilder(JTextArea textArea, int rows, int cols) {
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

    public GameObject[] setEnemiesPosition() {
        int enemyCount = 8;

        int enemyStartX = (int) (centerPanel.getWidth() * 0.2);
        int enemyStartY = (int) (centerPanel.getHeight() * 0.2);
        GameObject[] enemies = gameController.spawnEnemies(enemyCount, enemyStartX, enemyStartY);

        return enemies;
    }
}
