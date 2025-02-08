package entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Player extends Entity {
    private static final int SIZE = 20;

    public Player(double startX, double startY) {
        super(startX, startY);
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setFill(Color.BLUE);
        gc.fillOval(worldX, worldY, SIZE, SIZE);
    }
}
