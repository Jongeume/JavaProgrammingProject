package multi.gameproject.object;

import java.awt.*;

public abstract class GameObject {
    protected int posX;
    protected int posY;
    protected Image image;
    protected boolean isDead = false;

    public GameObject(int posX, int posY, Image image) {
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

    public Image getImage() {
        return image;
    }

    public void changeImage(Image image){
        this.image = image;
    }

    public boolean checkIsDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }

}
