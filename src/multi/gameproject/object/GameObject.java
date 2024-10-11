package multi.gameproject.object;

public abstract class GameObject {
    private int posX;
    private int posY;
    private String image;
    private int width;
    private int height;

    public GameObject(int posX, int posY, String image) {
        this.posX = posX;
        this.posY = posY;
        this.image = image;
    }

    public void setPosition(int x, int y) {
        this.posX = x;
        this.posY = y;
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
        posX += dx;
        posY += dy;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
