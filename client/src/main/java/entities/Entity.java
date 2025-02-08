package entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import shared.Vector2;

public abstract class Entity {
    protected Vector2 position;
    protected float speed = 500;
    protected int maxHealth = 100;
    protected int health = maxHealth;
    protected float rollCounter = 0.0f;

    public Entity(float startX, float startY) {
        this.position = new Vector2(startX, startY);
    }

    public boolean move(Vector2 inputVector, boolean isRolling, double deltaTime) {
        inputVector.normalize();
        if (isRolling) {
            inputVector.scale(speed*(float) deltaTime*3);
            rollCounter += (float) deltaTime;
            if (rollCounter >= 0.1) {
                rollCounter = 0;
                isRolling = false;
            }
        } else {
            inputVector.scale(speed*(float) deltaTime);
        }
        position.add(inputVector.x, inputVector.y);
        return isRolling;
    }

    public abstract void draw(GraphicsContext gc);
}
