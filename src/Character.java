import javafx.scene.image.ImageView;
import java.util.ArrayList;

/**
 * Playable character.
 */
public class Character {

    /**
     * Reference to the map.
     */
    public ArrayList<ArrayList<Field>> map;
    /**
     * Image of the character.
     */
    public ImageView avatar;

    private double x;
    private double y;
    /**
     * Vector x.
     */
    protected double dx;
    /**
     * Vector y.
     */
    protected double dy;

    private boolean movingLeft;
    private boolean movingRight;
    private boolean movingUp;
    private boolean movingDown;

    private boolean canMoveLeft;
    private boolean canMoveRight;
    private boolean canMoveUp;
    private boolean canMoveDown;

    private boolean falling;

    /**
     * Character is moving to the left.
     * @return Boolean value whether character is moving to the left.
     */
    public boolean isMovingLeft() { return movingLeft; }

    /**
     * Sets character movement to the left.
     * @param goLeft sets movingLeft property.
     */
    public void setMovingLeft(boolean goLeft) { this.movingLeft = goLeft; }

    /**
     * Character is moving to the right.
     * @return Boolean value whether character is moving to the right.
     */
    public boolean isMovingRight() { return movingRight; }

    /**
     * Sets character movement to the right.
     * @param goRight sets movingRight property.
     */
    public void setMovingRight(boolean goRight) { this.movingRight = goRight; }

    /**
     * Character is moving up.
     * @return Boolean value whether character is moving up.
     */
    public boolean isMovingUp() { return movingUp; }

    /**
     * Sets character movement up.
     * @param goUp sets movingUp property.
     */
    public void setMovingUp(boolean goUp) { this.movingUp = goUp; }

    /**
     * Character is moving down.
     * @return Boolean value whether character is moving down.
     */
    public boolean isMovingDown() { return movingDown; }

    /**
     * Sets character movement down.
     * @param goDown sets movingDown property.
     */
    public void setMovingDown(boolean goDown) { this.movingDown = goDown; }

    /**
     * Possibility to move left.
     * @return If character can move left.
     */
    public boolean isCanMoveLeft() { return canMoveLeft; }

    /**
     * Sets characters possibility to move left.
     * @param canMoveLeft Sets canMoveLeft.
     */
    public void setCanMoveLeft(boolean canMoveLeft) { this.canMoveLeft = canMoveLeft; }

    /**
     * Possibility to move right.
     * @return If character can move right.
     */
    public boolean isCanMoveRight() { return canMoveRight; }

    /**
     * Sets characters possibility to move right.
     * @param canMoveRight Sets canMoveRight.
     */
    public void setCanMoveRight(boolean canMoveRight) { this.canMoveRight = canMoveRight; }

    /**
     * Possibility to move up.
     * @return If character can move up.
     */
    public boolean isCanMoveUp() { return canMoveUp; }

    /**
     * Sets characters possibility move up.
     * @param canMoveUp Sets canMoveUp.
     */
    public void setCanMoveUp(boolean canMoveUp) { this.canMoveUp = canMoveUp; }

    /**
     * Possibility to move down.
     * @return If character can move down.
     */
    public boolean isCanMoveDown() { return canMoveDown; }

    /**
     * Sets characters possibility move down.
     * @param canMoveDown Sets canMoveDown.
     */
    public void setCanMoveDown(boolean canMoveDown) { this.canMoveDown = canMoveDown; }

    /**
     * Informs if character is falling, so he can't move.
     * @return If character is falling.
     */
    public boolean isFalling() { return falling; }

    /**
     * Sets character to fall.
     * @param fall Sets falling.
     */
    public void setFalling(boolean fall) {
        falling = fall;
        if (fall) {
            setCanMoveLeft(false);
            setCanMoveRight(false);
            setCanMoveUp(false);
            setCanMoveDown(false);
            setMovingDown(true);
        } else {
            setMovingDown(false);
        }
    }

    /**
     * x coordinate of the character.
     * @return x coordinate.
     */
    public double getX() {
        return x;
    }

    /**
     * x coordinate of the character and sets avatar position.
     * @param x coordinate of the character.
     */
    public void setX(double x) {
        this.x = x;
        avatar.setX(x);
    }

    /**
     * y coordinate of the character.
     * @return y coordinate.
     */
    public double getY() {
        return y;
    }

    /**
     * y coordinate of the character and sets avatar position.
     * @param y coordinate of the character.
     */
    public void setY(double y) {
        this.y = y;
        avatar.setY(y);
    }

    /**
     * Character constructor.
     */
    Character() {
        canMoveLeft = true;
        canMoveRight = true;
        canMoveUp = false;
        canMoveDown = false;
    }

    /**
     * Sets movement vectors to the left.
     */
    protected void left() {
        dx = -5;
        dy = 0;
    }

    /**
     * Sets movement vectors to the right.
     */
    protected void right() {
        dx = 5;
        dy = 0;
    }

    /**
     * Sets movement vectors up.
     */
    protected void up() {
        dx = 0;
        dy = -5;
    }

    /**
     * Sets movement vectors down.
     */
    protected void down() {
        dx = 0;
        dy = 5;
    }

    /**
     * Sets movmenet vector to zero.
     */
    public void stopMove() {
        dx = 0;
        dy = 0;
    }

    /**
     * Left field next to the character.
     * @return Returns left field next to the character.
     */
    public int leftX() {
        return (int)((x - 1) / LodeRunner.iconSize);
    }

    /**
     * Actual left field next to the character.
     * @return Returns actual left field next to the character.
     */
    public int leftActualX() {
        return (int)((x) / LodeRunner.iconSize);
    }

    /**
     * Right field next to the character.
     * @return Returns right field next to the character.
     */
    public int rightX() {
        return (int)((x + LodeRunner.iconSize) / LodeRunner.iconSize);
    }

    /**
     * Actual right field next to the character.
     * @return Returns actual right field next to the character.
     */
    public int rightActualX() {
        return (int)((x + LodeRunner.iconSize - 1) / LodeRunner.iconSize);
    }

    /**
     * Field above the character.
     * @return Returns field above the character.
     */
    public int upperY() {
        return (int)((y - 1) / LodeRunner.iconSize);
    }

    /**
     * Actual field above the character.
     * @return Returns actual field above the character.
     */
    public int upperActualY() {
        return (int)(y / LodeRunner.iconSize);
    }

    /**
     * Field below the character.
     * @return Returns field below the character.
     */
    public int lowerY() {
        return (int)((y + LodeRunner.iconSize) / LodeRunner.iconSize);
    }

    /**
     * Actual field below the character.
     * @return Returns actual field below the character.
     */
    public int lowerActualY() {
        return (int)((y + LodeRunner.iconSize - 1) / LodeRunner.iconSize);
    }

    /**
     * Renders character based on movement.
     */
    public void update() {
        if (!isCanMoveLeft() || !isCanMoveRight() || !isCanMoveUp() || !isCanMoveDown()) {
            stopMove();
        }
        if (isMovingLeft() && isCanMoveLeft()) {
            left();
        }
        if (isMovingRight() && isCanMoveRight()) {
            right();
        }
        if (isMovingUp() && isCanMoveUp()) {
            up();
        }
        if (isMovingDown() && (isCanMoveDown() || isFalling())) {
            down();
        }
        setX(getX() + dx);
        setY(getY() + dy);
    }

}
