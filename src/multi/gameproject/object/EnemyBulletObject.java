package multi.gameproject.object;

import java.awt.*;

public class EnemyBulletObject extends GameObject {
    public EnemyBulletObject(int posX, int posY, Image image) {
        super(posX, posY, image);
    }

    public void enemyBulletSpeed(int dy) {
        this.posY += dy;
    }
}
