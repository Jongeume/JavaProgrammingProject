package multi.gameproject.object;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GameModel {
    private PlayerObject player;
    private List<BulletObject> bullets;
    private List<EnemyObject> enemies;
    private List<EnemyBulletObject> enemyBullets;
    private List<BonusObject> bonusList;

    private Image playerBulletImg;
    private Image enemyImg;
    private Image leftMoveEnemyImg;
    private Image enemyBulletImg;
    private Image playerImg;
    private Image bonusImg;
    private Image enemyDeadImg;
    private Image playerDeadImg;

    public GameModel() {
        initImg();
        initObject();
    }

    private void initImg() {
        playerImg = new ImageIcon("images/BattleCrusier.png").getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        enemyImg = new ImageIcon("images/RightMutal.png").getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        leftMoveEnemyImg = new ImageIcon("images/LeftMutal.png").getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        playerBulletImg = new ImageIcon("images/YamatoCannon.png").getImage().getScaledInstance(10, 40, Image.SCALE_SMOOTH);
        enemyBulletImg = new ImageIcon("images/EnemyBullet.png").getImage().getScaledInstance(10, 20, Image.SCALE_SMOOTH);
        bonusImg = new ImageIcon("images/Bonus.png").getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        enemyDeadImg = new ImageIcon("images/EnemyDead.png").getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        playerDeadImg = new  ImageIcon("images/PlayerDead.png").getImage().getScaledInstance(150, 100, Image.SCALE_SMOOTH);
    }


    public void initObject() {
        bullets = new ArrayList<>();
        enemies = new ArrayList<>();
        enemyBullets = new ArrayList<>();
        bonusList = new ArrayList<>();
        player = new PlayerObject(425, 700, this.playerImg);
    }

    public PlayerObject getPlayer() {
        return player;
    }

    public List<BulletObject> getBullets() {
        return bullets;
    }

    public void setBullets(BulletObject bullet) {
        bullets.add(bullet);
    }

    public List<EnemyObject> getEnemies() {
        return enemies;
    }

    public void setEnemies(EnemyObject enemy) {
        enemies.add(enemy);
    }

    public List<EnemyBulletObject> getEnemyBullets() {
        return enemyBullets;
    }

    public List<BonusObject> getBonusList() {
        return bonusList;
    }

    public Image getBulletImg() {
        return playerBulletImg;
    }

    public Image getEnemyImg() {
        return enemyImg;
    }

    public Image getEnemyBulletImg() {
        return enemyBulletImg;
    }

    public Image getBonusImg() {
        return bonusImg;
    }

    public Image getLeftMoveImg(){
        return leftMoveEnemyImg;
    }

    public Image getEnemyDeadImg(){
        return enemyDeadImg;
    }

    public Image getPlayerDeadImg(){
        return playerDeadImg;
    }

}
