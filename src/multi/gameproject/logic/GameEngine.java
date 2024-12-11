package multi.gameproject.logic;



import multi.gameproject.Game;
import multi.gameproject.object.*;
import multi.gameproject.ui.GameView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GameEngine {
    private final Timer gameTimer;
    private final GameModel model;
    private final GameView view;
    private Timer enemyTimer;
    private Timer dieTimer;
    private int newPosX;
    private int newPosY;
    private int score;
    private int enemySpeed;
    private boolean gameStatus;
    private boolean reachedRightBoundary;
    private boolean reachedBoundary;
    private Random random = new Random();

    private int topScore;

    public GameEngine(GameModel model, GameView view, int topScore) {
        this.model = model;
        this.view = view;
        this.topScore = topScore;
        score = 0;
        enemySpeed = 100;
        gameStatus = true;
        reachedBoundary = false;

        view.setTopScore(topScore);
        spawnEnemies();
        gameTimer = new Timer(16, e -> {
            updateGameLoop();
        });
        enemyTimer = new Timer(enemySpeed, e -> updateEnemies());

        startGameLoop();
    }

    private void startGameLoop() {
        gameTimer.start();
        enemyTimer.start();
    }

    public void stopGameLoop() {
        gameTimer.stop();
        enemyTimer.stop();
    }

    public void updateGameLoop() {
        updateTopScore();

        if (!gameStatus) {
            stopGameLoop();
            view.displayAgainMessage("Lose");
        } else if (model.getEnemies().isEmpty()) {
            stopGameLoop();
            view.displayAgainMessage("Win");
        }

        updateBullets();
        updateEnemyBullets();
        updateBonus();
        userActionHandler();
        view.repaintLeftPanel();
        view.repaintTopScorePanel();
    }

    public void updateTopScore() {
        if (score > topScore) {
            topScore = score;
            Game.saveTopScore(topScore);
            view.setTopScore(topScore);
        }
    }

    private boolean isCollision(int posX1, int posY1, int width1, int height1,
                                int posX2, int posY2, int width2, int height2) {
        Rectangle rect1 = new Rectangle(posX1, posY1, width1, height1);
        Rectangle rect2 = new Rectangle(posX2, posY2, width2, height2);
        return rect1.intersects(rect2);
    }

    private boolean checkHitOnPlayer(EnemyBulletObject enemyBullet) {
        return isCollision(model.getPlayer().getPosX(), model.getPlayer().getPosY(), model.getPlayer().getImage().getWidth(null), model.getPlayer().getImage().getHeight(null),
                enemyBullet.getPosX(), enemyBullet.getPosY(), enemyBullet.getImage().getWidth(null), enemyBullet.getImage().getHeight(null));
    }

    private boolean checkPlayerEnemyCollision(EnemyObject enemy) {
        return isCollision(enemy.getPosX(), enemy.getPosY(), enemy.getImage().getWidth(null), enemy.getImage().getHeight(null),
                model.getPlayer().getPosX(), model.getPlayer().getPosY(), model.getPlayer().getImage().getWidth(null), model.getPlayer().getImage().getHeight(null));
    }

    private boolean checkHitsOnEnemies(EnemyObject enemy, BulletObject bullet) {
        return isCollision(enemy.getPosX(), enemy.getPosY(), enemy.getImage().getWidth(null), enemy.getImage().getHeight(null),
                bullet.getPosX(), bullet.getPosY(), bullet.getImage().getWidth(null), bullet.getImage().getHeight(null));
    }

    private boolean checkHitsOnBonus(BonusObject bonus, BulletObject bullet) {
        return isCollision(bonus.getPosX(), bonus.getPosY(), bonus.getImage().getWidth(null), bonus.getImage().getHeight(null),
                bullet.getPosX(), bullet.getPosY(), bullet.getImage().getWidth(null), bullet.getImage().getHeight(null));
    }

    private void userActionHandler() {
        int panelWidth = view.getLeftPanelWidth();
        int panelHeight = view.getLeftPanelHeight();

        int dx = 0;
        int dy = 0;

        if (view.getKeyCode().contains(KeyEvent.VK_UP)) {
            dy -= 20;
        }
        if (view.getKeyCode().contains(KeyEvent.VK_DOWN)) {
            dy += 20;
        }
        if (view.getKeyCode().contains(KeyEvent.VK_RIGHT)) {
            dx += 20;
        }
        if (view.getKeyCode().contains(KeyEvent.VK_LEFT)) {
            dx -= 20;
        }
        if (view.isPressedSpace()) {
            fireBullet();
        }

        movePlayer(dx, dy, panelWidth, panelHeight);
    }

    private void movePlayer(int dx, int dy, int panelWidth, int panelHeight) {
        newPosX = model.getPlayer().getPosX() + dx;
        newPosY = model.getPlayer().getPosY() + dy;

        if (!model.getPlayer().checkIsDead()) {

            if (newPosX < 0) {
                newPosX = 0;
            } else if (newPosX + model.getPlayer().getImage().getWidth(null) > panelWidth) {
                newPosX = panelWidth - model.getPlayer().getImage().getWidth(null) - 5;
            }

            if (newPosY < 0) {
                newPosY = 0;
            } else if (newPosY + model.getPlayer().getImage().getHeight(null) > panelHeight) {
                newPosY = panelHeight - model.getPlayer().getImage().getHeight(null);
            }

            model.getPlayer().playerMove(newPosX - model.getPlayer().getPosX(), newPosY - model.getPlayer().getPosY());
        }
    }

    private void fireBullet() {
        int bulletPosX = newPosX + model.getPlayer().getImage().getWidth(null) / 2 - model.getBulletImg().getWidth(null) / 2;
        int bulletPosY = newPosY - model.getBulletImg().getHeight(null);

        BulletObject bullet = new BulletObject(bulletPosX, bulletPosY, model.getBulletImg());
        model.setBullets(bullet);
    }

    private void handleDyingEffect(GameObject object, Image deadImage, Runnable action) {
        object.setDead(true);
        object.changeImage(deadImage);
        dieTimer = new Timer(300, e -> action.run());
        dieTimer.setRepeats(false);
        dieTimer.start();
    }

    private void moveBullets() {
        for (int i = model.getBullets().size() - 1; i >= 0; i--) {
            BulletObject bullet = model.getBullets().get(i);
            bullet.bulletSpeed(-10);

            if (bullet.getPosY() < 0) {
                model.getBullets().remove(bullet);
                continue;
            }

            for (int j = model.getEnemies().size() - 1; j >= 0; j--) {
                EnemyObject enemy = model.getEnemies().get(j);

                if (checkHitsOnEnemies(enemy, bullet) && !enemy.checkIsDead()) {
                    model.getBullets().remove(bullet);
                    score += 10;
                    handleDyingEffect(enemy, model.getEnemyDeadImg(), () -> model.getEnemies().remove(enemy));
                    break;
                }
            }

            if (model.getBonusList() != null) {
                for (int k = model.getBonusList().size() - 1; k >= 0; k--) {
                    BonusObject bonus = model.getBonusList().get(k);

                    if (checkHitsOnBonus(bonus, bullet) && !bonus.checkIsDead()) {
                        model.getBullets().remove(bullet);
                        score += 100;
                        handleDyingEffect(bonus, model.getEnemyDeadImg(), () -> model.getBonusList().remove(bonus));
                        break;
                    }
                }
            }
        }
    }

    private void updateBullets() {
        moveBullets();
        view.setScore(score);
        view.repaintScorePanel();
    }

    private void spawnBonus() {
        if (random.nextInt(200) <= 1) {
            model.getBonusList().add(new BonusObject(0, 50, model.getBonusImg()));
        }
    }

    private void moveBonus(int panelWidth) {
        for (int i = model.getBonusList().size() - 1; i >= 0; i--) {
            BonusObject bonus = model.getBonusList().get(i);
            if (!bonus.checkIsDead()) {
                bonus.bonusMove(10);
                if (bonus.getPosX() + bonus.getImage().getWidth(null) > panelWidth) {
                    model.getBonusList().remove(bonus);
                }
            }
        }
    }

    private void updateBonus() {
        spawnBonus();
        moveBonus(view.getLeftPanelWidth());
    }

    private void updateEnemyBullets() {
        shootEnemyBullet();
        moveEnemyBullets(view.getLeftPanelHeight());
    }

    private void shootEnemyBullet() {
        for (EnemyObject enemy : model.getEnemies()) {
            if (random.nextInt(80) <= 1) {
                model.getEnemyBullets().add(new EnemyBulletObject(enemy.getPosX() + enemy.getImage().getWidth(null) / 2, enemy.getPosY() + enemy.getImage().getHeight(null) / 2, model.getEnemyBulletImg()));
            }
        }
    }

    private void updateEnemies() {
        boolean changeDirection = moveEnemies(view.getLeftPanelWidth(), view.getLeftPanelHeight());
        if (changeDirection) {
            if (enemySpeed > 50) {
                enemySpeed -= 10;
            }
            enemyTimer.setDelay(enemySpeed);
        }
    }

    private void moveEnemyBullets(int panelHeight) {
        for (int i = model.getEnemyBullets().size() - 1; i >= 0; i--) {
            EnemyBulletObject enemyBullet = model.getEnemyBullets().get(i);
            enemyBullet.enemyBulletSpeed(15);

            if (enemyBullet.getPosY() + enemyBullet.getImage().getHeight(null) > panelHeight) {
                model.getEnemyBullets().remove(enemyBullet);
            }

            if (checkHitOnPlayer(enemyBullet)) {
                model.getEnemyBullets().remove(enemyBullet);
                handleDyingEffect(model.getPlayer(), model.getPlayerDeadImg(), () -> gameStatus = false);
                break;
            }
        }
    }

    private void spawnEnemies() {
        int enemyCount = 8;
        int InitEnemyPosX = 60;
        int InitEnemyPosY = 50;

        for (int i = 0; i < enemyCount; i++) {
            model.setEnemies(new EnemyObject(InitEnemyPosX + 90 * i, InitEnemyPosY - (i % 2 == 1 ? -30 : 30), model.getEnemyImg()));
        }
    }

    private boolean moveEnemies(int panelWidth, int panelHeight) {
        if (reachedBoundary) {
            reachedRightBoundary = !reachedRightBoundary;
            for (EnemyObject enemy : model.getEnemies()) {

                if (checkPlayerEnemyCollision(enemy)) {
                    gameStatus = false;
                    break;
                }
                enemy.enemyVerticalMove(enemy.getImage().getHeight(null));
            }
            reachedBoundary = false;
        }

        for (EnemyObject enemy : model.getEnemies()) {
            if (!enemy.checkIsDead()) {
                enemy.changeImage(reachedRightBoundary ? model.getLeftMoveImg() : model.getEnemyImg());

                if (checkPlayerEnemyCollision(enemy)) {
                    gameStatus = false;
                    break;
                }

                if (!reachedRightBoundary) {
                    enemy.enemySideMove(15);
                    if (enemy.getPosX() + enemy.getImage().getWidth(null) >= panelWidth) {
                        reachedBoundary = true;
                    }
                } else {
                    enemy.enemySideMove(-15);
                    if (enemy.getPosX() < 1) {
                        reachedBoundary = true;
                    }
                }
                if (enemy.getPosY() + enemy.getImage().getHeight(null) >= panelHeight) {
                    gameStatus = false;
                    break;
                }
            }
        }
        return reachedBoundary;
    }
}
