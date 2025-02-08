package entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public abstract class Entity {
    protected double worldX, worldY;
    protected double speed = 4;
    protected int maxHealth = 100;
    protected int health = maxHealth;

    public Entity(double startX, double startY) {
        this.worldX = startX;
        this.worldY = startY;
    }

    public void move(String direction) {
        switch (direction) {
            case "up": worldY -= speed; break;
            case "down": worldY += speed; break;
            case "left": worldX -= speed; break;
            case "right": worldX += speed; break;
        }
    }

    public abstract void draw(GraphicsContext gc);

    public double getX() { return worldX; }
    public double getY() { return worldY; }
}
