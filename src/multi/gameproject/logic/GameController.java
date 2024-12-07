package multi.gameproject.logic;

import multi.gameproject.object.*;
import multi.gameproject.ui.GameView;

import java.awt.event.KeyEvent;
import java.util.*;

public class GameController {
    private GameObject player;
    private List<BulletObject> bullets;
    private List<EnemyObject> enemies;
    private List<EnemyBulletObject> enemyBullets;
    private HashSet<Integer> pressedKeys;
    private Random random = new Random();

    private boolean gameStatus;
    private int score;
    private int enemySpeed;
    private boolean isFiring;
    private boolean reachedRightBoundary;
    private boolean reachedBoundary;

    public GameController() {
        player = new PlayerObject(31, 25);
        pressedKeys = new HashSet<>();
        bullets = new ArrayList<>();
        enemies = new ArrayList<>();
        enemyBullets = new ArrayList<>();
        spawnEnemies();
        score = 0;
        gameStatus = true;
        new GameView(this);
    }

    public boolean getGameStatus() {
        return gameStatus;
    }

    public GameObject getPlayer() {
        return player;
    }

    public List<BulletObject> getBullets() {
        return bullets;
    }

    public List<EnemyObject> getEnemies() {
        return enemies;
    }

    public List<EnemyBulletObject> getEnemyBullet() {
        return enemyBullets;
    }

    public int getScore() {
        return score;
    }

    public int getEnemySpeed() {
        return enemySpeed;
    }

    public void handlePlayerMovement(int keyCode, int cols, int rows) {
        pressedKeys.add(keyCode);

        int dx = 0;
        int dy = 0;

        if (pressedKeys.contains(KeyEvent.VK_UP)) {
            dy -= 1;
        }
        if (pressedKeys.contains(KeyEvent.VK_DOWN)) {
            dy += 1;
        }
        if (pressedKeys.contains(KeyEvent.VK_RIGHT)) {
            dx += 1;
        }
        if (pressedKeys.contains(KeyEvent.VK_LEFT)) {
            dx -= 1;
        }

        int newPosX = player.getPosX() + dx;
        int newPosY = player.getPosY() + dy;

        if (newPosX < 0) {
            newPosX = 0;
        } else if (newPosX + player.getImage().length() >= cols) {
            newPosX = cols - player.getImage().length() - 1;
        }

        if (newPosY < 0) {
            newPosY = 0;
        } else if (newPosY >= rows - 1) {
            newPosY = rows - 2;
        }

        player.move(newPosX - player.getPosX(), newPosY - player.getPosY());

        if (pressedKeys.contains(KeyEvent.VK_SPACE) && !isFiring) {
            isFiring = true;
            fireBullet(newPosX, newPosY);
        }
    }

    public void keyReleased(KeyEvent e) {
        pressedKeys.remove(e.getKeyCode());
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            isFiring = false;
        }
    }

    public void fireBullet(int playerPosX, int playerPosY) {
        int bulletPosX = playerPosX + (player.getImage().length() / 2);
        int bulletPosY = playerPosY - 1;
        BulletObject bullet = new BulletObject(bulletPosX, bulletPosY);
        bullets.add(bullet);
    }

    public void spawnEnemies() {
        int enemyCount = 8;
        int InitEnemyPosX = 5;
        int InitEnemyPosY = 10;

        for (int i = 0; i < enemyCount; i++) {
            enemies.add(new EnemyObject(InitEnemyPosX + 5 * i, InitEnemyPosY - (i % 2 == 1 ? 5 : 7)));
        }
    }

    public void shootEnemyBullet() {
        for (EnemyObject enemy : enemies) {
            if (random.nextInt(100) <= 1) {
                enemyBullets.add(new EnemyBulletObject(enemy.getPosX() + enemy.getImage().length() / 2, enemy.getPosY()));
            }
        }
    }

    public void checkHitsOnEnemies() {
        for (int i = bullets.size() - 1; i >= 0; i--) {
            BulletObject bullet = bullets.get(i);
            if (bullet.getPosY() < 0) {
                bullets.remove(i);
                continue;
            }

            for (int j = enemies.size() - 1; j >= 0; j--) {
                EnemyObject enemy = enemies.get(j);
                if (bullet.getPosX() >= enemy.getPosX() && bullet.getPosX() <= enemy.getPosX() + enemy.getImage().length() - 1 && bullet.getPosY() == enemy.getPosY()) {
                    bullets.remove(i);
                    enemies.remove(j);
                    score += 10;
                    break;
                }
            }
        }
    }

    public boolean checkHitOnPlayer(int rows) {
        for (int i = enemyBullets.size() - 1; i >= 0; i--) {
            EnemyBulletObject enemyBullet = enemyBullets.get(i);
            if (enemyBullet.getPosY() >= rows - 1) {
                enemyBullets.remove(i);
                continue;
            }
            if (enemyBullet.getPosX() >= player.getPosX() && enemyBullet.getPosX() <= player.getPosX() + player.getImage().length() - 1 && enemyBullet.getPosY() == player.getPosY()) {
                enemyBullets.remove(i);
                return true;
            }
        }
        return false;
    }

    public void moveEnemyBullets(int cols) {
        for (EnemyBulletObject enemyBullet : getEnemyBullet()) {
            enemyBullet.move(0, 1);
        }
        if (checkHitOnPlayer(cols)) {
            gameStatus = false;
        }
    }

    public void moveBullets() {
        for (BulletObject bullet : getBullets()) {
            bullet.move(0, -1); // 총알을 위로 이동
        }
        checkHitsOnEnemies();
    }

    public boolean moveEnemies(int cols, int enemySpeed) {
        this.enemySpeed = enemySpeed;

        if (reachedBoundary) {
            reachedRightBoundary = !reachedRightBoundary;
            for (EnemyObject enemy : getEnemies()) {
                enemy.move(0, 1);
            }
            if (enemySpeed > 100) {
                this.enemySpeed -= 100;
            }
            reachedBoundary = false;
        }

        for (EnemyObject enemy : getEnemies()) {
            if (!reachedRightBoundary) {
                enemy.move(1, 0);
                if (enemy.getPosX() + enemy.getImage().length() >= cols) {
                    reachedBoundary = true;
                }
            } else {
                enemy.move(-1, 0);
                if (enemy.getPosX() < 1) {
                    reachedBoundary = true;
                }
            }
        }

        return reachedBoundary;
    }

    public void resetObject() {
        enemies.clear();
        bullets.clear();
        enemyBullets.clear();
        score = 0;
        gameStatus = true;
        player = new PlayerObject(31, 25);
    }
}

