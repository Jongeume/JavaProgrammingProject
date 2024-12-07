package multi.gameproject.object;

public class BulletObject extends GameObject {
    public BulletObject(int posX, int posY) {
        super(posX, posY, "!");
    }

    @Override
    public void move(int dx, int dy) {
        super.move(dx, dy);
    }
}
