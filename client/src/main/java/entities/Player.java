package entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Player extends Entity {
    private static final int SIZE = 20;
    private Image avatar;

    public Player(double startX, double startY) {
        super(startX, startY);
        avatar = new Image(getClass().getResourceAsStream("/sprites/wimyits.jpg"));
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setFill(Color.BLUE);
        gc.fillOval(worldX, worldY, SIZE, SIZE);
        if (avatar != null) {
            gc.drawImage(avatar, worldX, worldY, SIZE, SIZE);
        }
        
    }
}
