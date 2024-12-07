package multi.gameproject.object;

public abstract class GameObject {
    protected int posX;
    protected int posY;
    protected String image;

    public GameObject(int posX, int posY, String image) {
        this.posX = posX;
        this.posY = posY;
        this.image = image;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public String getImage() {
        return image;
    }

    public void move(int dx, int dy) {
        this.posX += dx;
        this.posY += dy;
    }
}
