package multi.gameproject.object;

import java.awt.*;

public class EnemyObject extends GameObject {
    public EnemyObject(int posX, int posY, Image image) {
        super(posX, posY, image);
    }

    public void enemySideMove(int dx) {
        this.posX += dx;
    }

    public void enemyVerticalMove(int dy) {
        this.posY += dy;
    }
}
