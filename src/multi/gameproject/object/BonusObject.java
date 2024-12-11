package multi.gameproject.object;

import java.awt.*;

public class BonusObject extends GameObject {
    public BonusObject(int posX, int posY, Image image) {
        super(posX, posY, image);
    }

    public void bonusMove(int dx) {
        this.posX += dx;
    }
}
