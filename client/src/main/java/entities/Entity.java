package entities;
import javafx.scene.input.KeyEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;


import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import static javafx.scene.input.MouseEvent.MOUSE_PRESSED;

public abstract class Entity {
    protected double worldX, worldY;
    protected double speed = 4;
    protected int maxHealth = 100;
    protected int health = maxHealth;
    protected boolean attacking = false;

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

    public double getX() { return worldX; }
    public double getY() { return worldY; }
}
