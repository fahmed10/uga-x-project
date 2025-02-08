package entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Player extends Entity {
    private static final int SIZE = 128;
    private Image avatar;
    private Image legs;

    public Player(double startX, double startY) {
        super(startX, startY);
        avatar = new Image(getClass().getResourceAsStream("/sprites/arm_facing_left.png"));
        legs = new Image(getClass().getResourceAsStream("/sprites/legs_left.png"));
    }

    @Override
    public void draw(GraphicsContext gc) {
        //gc.setFill(Color.BLUE);
        //gc.fillOval(worldX, worldY, SIZE, SIZE);
        if (avatar != null) {
            gc.drawImage(legs, worldX+1, worldY+36, SIZE, SIZE);
            gc.drawImage(avatar, worldX, worldY, SIZE, SIZE);
        }
        
    }
}
