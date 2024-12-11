package multi.gameproject.object;

import java.awt.*;

public class BulletObject extends GameObject {
    public BulletObject(int posX, int posY, Image image) {
        super(posX, posY, image );
    }

    public void bulletSpeed(int dy) {
        this.posY += dy;
    }
}