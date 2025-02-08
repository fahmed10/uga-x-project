package entities;

import com.example.ugaxproject.Direction;
import com.example.ugaxproject.Input;
import com.example.ugaxproject.State;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import shared.Vector2;

import java.util.Set;

public class Player extends Entity {
    private static final int SIZE = 128;
    private Image avatar;
    private Image legs;
    private Image guitar;
    private Direction direction = Direction.LEFT;
    private State state = State.NOT_ATTACKING;
    protected float walkCounter = 0.0f;
    //public boolean runningLeft = true;

    public Player(float startX, float startY) {
        super(startX, startY);
        avatar = new Image(getClass().getResourceAsStream("/sprites/arm_facing_left.png"));
        legs = new Image(getClass().getResourceAsStream("/sprites/legs_left.png"));
        guitar = new Image(getClass().getResourceAsStream("/sprites/guitar_diagonal_swing_left.png"));
    }

    public Vector2 getPosition() {
        return position;
    }

    @Override
    public void draw(GraphicsContext gc) {
        //gc.setFill(Color.BLUE);
        //gc.fillOval(position.x, position.y, SIZE, SIZE);

        if(state == State.NOT_ATTACKING) {
            if (direction == Direction.LEFT) {
                gc.drawImage(guitar, position.x, position.y, SIZE, SIZE);
                gc.drawImage(legs, position.x + 1, position.y + 36, SIZE, SIZE);
                gc.drawImage(avatar, position.x, position.y, SIZE, SIZE);
            } else {
                gc.drawImage(legs, position.x + 1, position.y + 36, SIZE, SIZE);
                gc.drawImage(avatar, position.x, position.y, SIZE, SIZE);
                gc.drawImage(guitar, position.x - 5, position.y, SIZE, SIZE);
            }
        }
        else if(state == State.IS_ATTACKING){
            if (direction == Direction.LEFT) {
                gc.drawImage(legs, position.x + 1, position.y + 36, SIZE, SIZE);
                gc.drawImage(avatar, position.x, position.y, SIZE, SIZE);
                gc.drawImage(guitar, position.x, position.y-25, SIZE, SIZE);
            } else {
                gc.drawImage(legs, position.x + 1, position.y + 36, SIZE, SIZE);
                gc.drawImage(avatar, position.x, position.y, SIZE, SIZE);
                gc.drawImage(guitar, position.x , position.y-25, SIZE, SIZE);
            }
        }
        else if(state == State.AFTER_ATTACKING_LEFT) {
            gc.drawImage(legs, position.x + 1, position.y + 36, SIZE, SIZE);
            gc.drawImage(avatar, position.x, position.y, SIZE, SIZE);
            gc.drawImage(guitar, position.x - 30, position.y+25, SIZE, SIZE);
        } else if(state == State.AFTER_ATTACKING_RIGHT) {
            gc.drawImage(legs, position.x + 1, position.y + 36, SIZE, SIZE);
            gc.drawImage(avatar, position.x, position.y, SIZE, SIZE);
            gc.drawImage(guitar, position.x + 30, position.y+25, SIZE, SIZE);
        }
    }


    public void walkAnimation(Direction direction, Set<Input> inputs, double deltaTime) {
        this.direction = direction;

        if(state == State.NOT_ATTACKING) {
            switch (direction) {
                case Direction.LEFT:
                    legs = new Image(getClass().getResourceAsStream("/sprites/legs_left.png"));
                    guitar = new Image(getClass().getResourceAsStream("/sprites/guitar_diagonal_carry_left.png"));
                    avatar = new Image(getClass().getResourceAsStream("/sprites/arm_facing_left.png"));
                    break;
                case Direction.RIGHT:
                    legs = new Image(getClass().getResourceAsStream("/sprites/legs_right.png"));
                    guitar = new Image(getClass().getResourceAsStream("/sprites/guitar_diagonal_carry_right.png"));
                    avatar = new Image(getClass().getResourceAsStream("/sprites/arm_facing_right.png"));
                    break;
            }
        }else if(state == State.IS_ATTACKING){
            switch (direction) {
                case Direction.LEFT:
                    legs = new Image(getClass().getResourceAsStream("/sprites/legs_left.png"));
                    //guitar = new Image(getClass().getResourceAsStream("/sprites/guitar_overhead_left.png"));
                    //avatar = new Image(getClass().getResourceAsStream("/sprites/left_overhead.png"));
                    break;
                case Direction.RIGHT:
                    legs = new Image(getClass().getResourceAsStream("/sprites/legs_right.png"));
                    //guitar = new Image(getClass().getResourceAsStream("/sprites/guitar_overhead_right.png"));
                    //avatar = new Image(getClass().getResourceAsStream("/sprites/right_overhead.png"));
                    break;
            }
        }
        else{
            switch (direction) {
                case Direction.LEFT:
                    legs = new Image(getClass().getResourceAsStream("/sprites/legs_left.png"));
                    break;
                case Direction.RIGHT:  legs = new Image(getClass().getResourceAsStream("/sprites/legs_right.png")); break;

            }
        }


        if(direction == Direction.LEFT && inputs.contains(Input.MOVE_LEFT)) {
            walkCounter += (float) deltaTime;
            if (walkCounter < 0.25) {
                legs = new Image(getClass().getResourceAsStream("/sprites/legs_left_1.png"));
            } else if (walkCounter < 0.50) {
                legs = new Image(getClass().getResourceAsStream("/sprites/legs_left_2.png"));
            } else if (walkCounter < 0.75) {
                legs = new Image(getClass().getResourceAsStream("/sprites/legs_left_1.png"));
            } else if (walkCounter < 1.00) {
                legs = new Image(getClass().getResourceAsStream("/sprites/legs_left_2.png"));
            } else {
                walkCounter = 0;
            }
        }

        if(direction == Direction.RIGHT && inputs.contains(Input.MOVE_RIGHT)) {
            walkCounter += (float) deltaTime;
            if (walkCounter < 0.25) {
                legs = new Image(getClass().getResourceAsStream("/sprites/legs_right_1.png"));
            } else if (walkCounter < 0.50) {
                legs = new Image(getClass().getResourceAsStream("/sprites/legs_right_2.png"));
            } else if (walkCounter < 0.75) {
                legs = new Image(getClass().getResourceAsStream("/sprites/legs_right_1.png"));
            } else if (walkCounter < 1.00) {
                legs = new Image(getClass().getResourceAsStream("/sprites/legs_right_2.png"));
            } else {
                walkCounter = 0;
            }
        }
    }



    public void attack(){
        if(state == State.IS_ATTACKING) { //Inside the if statement is just to test sprites
            if(direction == Direction.LEFT) {
                avatar = new Image(getClass().getResourceAsStream("/sprites/swing_left_end_frame.png"));
                guitar = new Image(getClass().getResourceAsStream(("/sprites/guitar_smear_frame_diagonal_left.png")));
                state = State.AFTER_ATTACKING_LEFT;
            }
            else{
                avatar = new Image(getClass().getResourceAsStream("/sprites/swing_right_end_frame.png"));
                guitar = new Image(getClass().getResourceAsStream(("/sprites/guitar_smear_frame_diagonal_right.png")));
                state = State.AFTER_ATTACKING_RIGHT;
            }
        }

        if(state == State.NOT_ATTACKING) {
            if(direction == Direction.LEFT) {
                avatar = new Image(getClass().getResourceAsStream("/sprites/left_overhead.png"));
                guitar = new Image(getClass().getResourceAsStream("/sprites/guitar_overhead_left.png"));
            }
            else{
                avatar = new Image(getClass().getResourceAsStream("/sprites/right_overhead.png"));
                guitar = new Image(getClass().getResourceAsStream("/sprites/guitar_overhead_right.png"));
            }

            state = State.IS_ATTACKING;
        }


    }
}
