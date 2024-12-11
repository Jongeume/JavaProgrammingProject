package multi.gameproject;

import multi.gameproject.logic.GameEngine;
import multi.gameproject.object.GameModel;
import multi.gameproject.ui.GameView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Game extends JFrame {
    private static final String SCORE_FILE = "top_score.txt";
    private static int topScore;
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private GameView view;
    private GameModel model;
    private int width;
    private int height;

    public Game() {
        this.width = 1200;
        this.height = 800;
        mainPanel = new JPanel();
        mainPanel.setPreferredSize(new Dimension(width, height));
        initJFrame();
        initMVC();
        mainPanel.add(view);
    }

    public Game(int width, int height) {
        this.width = width;
        this.height = height;
        initCardLayout();
        initJFrame();
    }

    public static void main(String[] args) {
        new Game(1200, 800);
    }

    public static void saveTopScore(int newTopScore) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SCORE_FILE))) {
            writer.write(String.valueOf(newTopScore));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadTopScore() {
        try {
            if (Files.exists(Paths.get(SCORE_FILE))) {
                BufferedReader reader = new BufferedReader(new FileReader(SCORE_FILE));
                String line = reader.readLine();
                if (line != null) {
                    topScore = Integer.parseInt(line);
                }
                reader.close();
            } else {
                topScore = 0;
            }
        } catch (IOException e) {
            e.printStackTrace();
            topScore = 0;
        }
    }

    private void initCardLayout() {
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.add(createStartPanel(), "StartScreen");
    }

    private JPanel createStartPanel() {
        JPanel startPanel = new JPanel(new BorderLayout());

        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        ImageIcon imageIcon = new ImageIcon("images/mainImg.png");
        Image scaledImage = imageIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        imageIcon = new ImageIcon(scaledImage);
        imageLabel.setIcon(imageIcon);
        startPanel.add(imageLabel, BorderLayout.CENTER);

        JButton startButton = new JButton("Game Start");
        startButton.setFont(new Font("Arial", Font.BOLD, 20));
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                initMVC();
                mainPanel.add(view, "GameScreen");
                cardLayout.show(mainPanel, "GameScreen");
                view.requestFocusInWindow();
            }
        });
        startPanel.add(startButton, BorderLayout.SOUTH);

        return startPanel;
    }

    private void initMVC() {
        loadTopScore();
        model = new GameModel();
        view = new GameView(width, height, model);
        new GameEngine(model, view, topScore);
    }

    private void initJFrame() {
        setContentPane(mainPanel);
        setTitle("Space Invader");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
