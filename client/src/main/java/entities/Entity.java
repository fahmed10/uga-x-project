package entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import shared.Vector2;

public abstract class Entity {
    protected Vector2 position;
    protected float speed = 4;
    protected int maxHealth = 100;
    protected int health = maxHealth;

    public Entity(float startX, float startY) {
        this.position = new Vector2(startX, startY);
    }

    public void move(Vector2 inputVector) {
        inputVector.normalize();
        inputVector.scale(speed);
        position.add(inputVector.x, inputVector.y);
    }

    public abstract void draw(GraphicsContext gc);
}
