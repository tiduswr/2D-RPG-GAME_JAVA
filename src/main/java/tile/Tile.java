package tile;

import java.awt.image.BufferedImage;

public class Tile {
    protected BufferedImage image;
    protected boolean collision = false;

    public BufferedImage getImage() {
        return image;
    }
    public void setImage(BufferedImage image) {
        this.image = image;
    }
    public boolean isCollision() {
        return collision;
    }
    public void setCollision(boolean collision) {
        this.collision = collision;
    }
}
