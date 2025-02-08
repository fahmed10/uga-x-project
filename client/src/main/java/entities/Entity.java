package entities;
import javafx.scene.input.KeyEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;


import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import shared.Vector2;

import static javafx.scene.input.MouseEvent.MOUSE_PRESSED;

public abstract class Entity {
    protected Vector2 position;
    protected float speed = 4;
    protected int maxHealth = 100;
    protected int health = maxHealth;
    protected boolean attacking = false;

    public Entity(float startX, float startY) {
        this.position = new Vector2(startX, startY);
    }

    public void move(Vector2 inputVector) {
        inputVector.normalize();
        inputVector.scale(speed);
        position.add(inputVector.x, inputVector.y);
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
}
