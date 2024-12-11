package multi.gameproject.ui;


import multi.gameproject.object.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class LeftPanel extends JPanel {
    private GameObject player;
    private List<BulletObject> bullets;
    private List<EnemyObject> enemies;
    private List<EnemyBulletObject> enemyBullets;
    private List<BonusObject> bonusList;

    public LeftPanel(GameObject player, List<BulletObject> bullets, List<EnemyObject> enemies, List<EnemyBulletObject> enemyBullets, List<BonusObject> bonusList) {
        this.player = player;
        this.bullets = bullets;
        this.enemies = enemies;
        this.enemyBullets = enemyBullets;
        this.bonusList = bonusList;
        this.setFocusable(true);
        this.requestFocusInWindow();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        if (player != null) {
            g2d.drawImage(player.getImage(), player.getPosX(), player.getPosY(), null);
        }

        if (bullets != null) {
            for (BulletObject bullet : bullets) {
                g2d.drawImage(bullet.getImage(), bullet.getPosX(), bullet.getPosY(), null);
            }
        }

        if (enemies != null) {
            for (EnemyObject enemy : enemies) {
                g2d.drawImage(enemy.getImage(), enemy.getPosX(), enemy.getPosY(), null);
            }
        }

        if (enemyBullets != null) {
            for (EnemyBulletObject enemyBullet : enemyBullets) {
                g2d.drawImage(enemyBullet.getImage(), enemyBullet.getPosX(), enemyBullet.getPosY(), null);
            }
        }

        if (bonusList != null) {
            for (BonusObject bonus : bonusList) {
                g2d.drawImage(bonus.getImage(), bonus.getPosX(), bonus.getPosY(), null);
            }
        }
    }
}
