package multi.gameproject.object;

public class PlayerObject extends GameObject {
    public PlayerObject(int posX, int posY) {
        super(posX, posY, ">-O-<");
    }

    @Override
    public void move(int dx, int dy) {
        super.move(dx, dy);
    }
}
