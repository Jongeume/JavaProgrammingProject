package multi.gameproject.object;

import java.awt.*;

public class PlayerObject extends GameObject {
    public PlayerObject(int posX, int posY, Image image) {
        super(posX, posY, image);
    }

    public void playerMove(int dx, int dy) {
        posX += dx;
        posY += dy;
    }
}
