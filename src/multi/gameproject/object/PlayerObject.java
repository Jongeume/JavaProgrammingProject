package multi.gameproject.object;

public class PlayerObject extends GameObject {
    public PlayerObject() {
        super(300, 500, ">-O-<");
    }

    @Override
    public void move(int dx, int dy) {
        if (dx != 0 && dy != 0) {
            dx /= Math.sqrt(2);
            dy /= Math.sqrt(2);
        }
        super.move(dx, dy);
    }
}
