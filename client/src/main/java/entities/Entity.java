package entities;
import com.example.ugaxproject.Direction;
import com.example.ugaxproject.GameView;
import javafx.scene.input.KeyEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;


import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import shared.Vector2;

import java.awt.*;

import static javafx.scene.input.MouseEvent.MOUSE_PRESSED;

public abstract class Entity {
    protected Vector2 position;
    protected float speed = 500;
    protected int maxHealth = 100;
    protected int health = maxHealth;
    protected float rollCounter = 0.0f;
    protected boolean attacking = false;

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
        GameView.worldX += inputVector.x;
        GameView.worldY += inputVector.y;
        return isRolling;
    }

    public void moveTo(Vector2 position) {
        this.position = position;
    }

//    public void attack(MouseEvent event) {
//        switch((MouseEvent) event.getEventType()){
//            case MOUSE_PRESSED: attacking = true; break;
//        }
//    }

    public void attack() {
        // Handle other cases if needed
            attacking = true;
            System.out.println("attacking is true");
    }




    public abstract void draw(GraphicsContext gc);

    //public abstract void draw(GraphicsContext gc, Dimension direction);

    //public abstract void draw(GraphicsContext gc, Direction direction);
}
