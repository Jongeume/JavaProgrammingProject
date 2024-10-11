package multi.gameproject.object;

import multi.gameproject.logic.GameController;
import multi.gameproject.ui.GameView;

public class BulletObject extends GameObject implements Runnable {
    private static final int BULLET_SPEED = 10;
    private GameView gameView;
    private GameController gameController;

    public BulletObject(int posX, int posY, GameView gameView, GameController gameController) {
        super(posX, posY, "!");
        this.gameView = gameView;
        this.gameController = gameController;
    }

    @Override
    public void run() {
        while (getPosY() + getHeight() > 0) {
            gameView.repaint();
            move(0, BULLET_SPEED);

            if (getPosY() + getHeight() <= 0) {
                gameController.removeBullet(this);
                break;
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void move(int dx, int dy) {
        setPosition(getPosX(), getPosY() - dy);
    }
}
