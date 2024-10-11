package multi.gameproject.logic;

import multi.gameproject.object.BulletObject;
import multi.gameproject.object.EnemyObject;
import multi.gameproject.object.GameObject;
import multi.gameproject.object.PlayerObject;
import multi.gameproject.ui.GameView;

import java.awt.event.KeyEvent;
import java.util.*;

public class GameController {
    private GameView gameView;
    private GameObject player;
    private GameObject enemies[];
    private List<BulletObject> bullets;
    private HashSet<Integer> pressedKeys;

    public GameController() {
        gameView = new GameView(this);
        player = new PlayerObject();
        pressedKeys = new HashSet<>();
        bullets = new ArrayList<>();
    }

    public GameObject getPlayer() {
        return player;
    }

    public List<BulletObject> getBullets() {
        return bullets;
    }

    public void handlePlayerMovement(int keyCode, int centerPanelWidth, int centerPanelHeight) {
        pressedKeys.add(keyCode);

        int dx = 0;
        int dy = 0;

        if (pressedKeys.contains(KeyEvent.VK_UP)) {
            dy -= 10;
        }
        if (pressedKeys.contains(KeyEvent.VK_DOWN)) {
            dy += 10;
        }
        if (pressedKeys.contains(KeyEvent.VK_RIGHT)) {
            dx += 10;
        }
        if (pressedKeys.contains(KeyEvent.VK_LEFT)) {
            dx -= 10;
        }

        if (pressedKeys.contains(KeyEvent.VK_SPACE)) {
            fireBullet();
        }

        int playerPosX = player.getPosX();
        int playerPosY = player.getPosY();

        int newPosX = playerPosX + dx;
        int newPosY = playerPosY + dy;

        if (newPosX >= 0 && newPosX <= centerPanelWidth - player.getWidth()) {
            player.move(newPosX - playerPosX, 0);
        }

        if (newPosY >= 0 && newPosY <= centerPanelHeight - player.getHeight()) {
            player.move(0, newPosY - playerPosY);
        }
    }

    public void keyReleased(KeyEvent e) {
        pressedKeys.remove(e.getKeyCode());
    }

    public void fireBullet() {
        int bulletPosX = player.getPosX() + (player.getWidth() / 2);
        int bulletPosY = player.getPosY() - 1;
        BulletObject bulletObject = new BulletObject(bulletPosX, bulletPosY, gameView, this);
        bullets.add(bulletObject);
        new Thread(bulletObject).start();
    }

    public void removeBullet(BulletObject bullet) {
        bullets.remove(bullet);
    }

    public GameObject[] spawnEnemies(int enemyCount, int enemyStartX, int enemyStartY) {
        enemies = new GameObject[enemyCount];
        for (int i = 0; i < enemyCount; i++) {
            enemies[i] = new EnemyObject(enemyStartX + 50 * i, enemyStartY - (i % 2 == 1 ? 0 : 25));
        }

        return enemies;
    }
}
