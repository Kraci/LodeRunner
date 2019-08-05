import javafx.scene.image.ImageView;

/**
 * Enemy bots.
 */
public class Bot extends Character {

    private Runner runner;

    /**
     * Bot contructor.
     * @param runner player reference, so bot can follow his position.
     */
    Bot(Runner runner) {
        super();
        this.avatar = new ImageView("file:bot.png");
        this.runner = runner;
    }

    @Override
    protected void left() {
        dx = -2;
        dy = 0;
    }

    @Override
    protected void right() {
        dx = 2;
        dy = 0;
    }

    @Override
    protected void up() {
        dx = 0;
        dy = -2;
    }

    @Override
    protected void down() {
        dx = 0;
        dy = 2;
    }

    /**
     * Returns if bot contains coordinates.
     * @param x x coordinate.
     * @param y y coordinate.
     * @return Returns boolean value whether point hits bot.
     */
    public boolean isPointIn(double x, double y) {
        if (x >= avatar.getX() && x <= avatar.getX() + LodeRunner.iconSize &&
                y >= avatar.getY() && y <= avatar.getY() + LodeRunner.iconSize) {
            return true;
        }
        return false;
    }

    /**
     * Bots movement, based on player position.
     */
    public void move() {
        if (isCanMoveUp() && (getY() > runner.getY())) {
            setMovingLeft(false);
            setMovingRight(false);
            setMovingUp(true);
            setMovingDown(false);
            return;
        }
        if (isCanMoveDown() && (getY() < runner.getY())) {
            setMovingLeft(false);
            setMovingRight(false);
            setMovingUp(false);
            setMovingDown(true);
            return;
        }
        if (isCanMoveLeft() && (getX() > runner.getX())) {
            setMovingLeft(true);
            setMovingRight(false);
            setMovingUp(false);
            setMovingDown(false);
            return;
        }
        if (isCanMoveRight() && (getX() < runner.getX())) {
            setMovingLeft(false);
            setMovingRight(true);
            setMovingUp(false);
            setMovingDown(false);
            return;
        }
    }

    /**
     * Updates bot movement (Bots brain).
     */
    public void checkMoves() {
        int runnerLeftX = leftX();
        int runnerRightX = rightX();
        int runnerUpperY = upperY();
        int runnerLowerY = lowerY();
        int runnerLeftActualX = leftActualX();
        int runnerRightActualX = rightActualX();
        int runnerUpperActualY = upperActualY();
        int runnerLowerActualY = lowerActualY();

        if (bounderies()) {
            return;
        }

        Field leftFieldWithUpper = map.get(runnerUpperActualY).get(runnerLeftX);
        Field rightFieldWithUpper = map.get(runnerUpperActualY).get(runnerRightX);

        Field leftFieldWithLower = map.get(runnerLowerActualY).get(runnerLeftX);
        Field rightFieldWithLower = map.get(runnerLowerActualY).get(runnerRightX);

        if (leftFieldWithUpper.type.equals(FieldType.WALL) || leftFieldWithLower.type.equals(FieldType.WALL)) {
            setCanMoveLeft(false);
        } else {
            setCanMoveLeft(true);
        }

        if (rightFieldWithUpper.type.equals(FieldType.WALL) || rightFieldWithLower.type.equals(FieldType.WALL)) {
            setCanMoveRight(false);
        } else {
            setCanMoveRight(true);
        }

        Field leftActualField = map.get(runnerLowerActualY).get(runnerLeftActualX);
        Field rightActualField = map.get(runnerLowerActualY).get(runnerRightActualX);

        if ( (leftActualField.type.equals(FieldType.LADDER) && rightActualField.type.equals(FieldType.LADDER)) ||
                (leftActualField.type.equals(FieldType.EXIT) && rightActualField.type.equals(FieldType.EXIT))) {
            setCanMoveUp(true);
            setCanMoveDown(true);
        } else {
            setCanMoveUp(false);
            setCanMoveDown(false);
        }

        Field fieldGroundWithLeft = map.get(runnerLowerY).get(runnerLeftActualX);
        Field fieldGroundWithRight = map.get(runnerLowerY).get(runnerRightActualX);

        if (((leftActualField.type.equals(FieldType.EMPTY) && rightActualField.type.equals(FieldType.EMPTY)) || ((leftActualField.type.equals(FieldType.ROPE) && rightActualField.type.equals(FieldType.ROPE))))
                && fieldGroundWithLeft.type.equals(FieldType.LADDER) && fieldGroundWithRight.type.equals(FieldType.LADDER)) {
            setFalling(false);
            setCanMoveDown(true);
        }

        if (fieldGroundWithLeft.type.equals(FieldType.EMPTY) && fieldGroundWithRight.type.equals(FieldType.EMPTY) && leftActualField.type.equals(FieldType.ROPE) && rightActualField.type.equals(FieldType.ROPE)) {
            setCanMoveDown(true);
        }

        if (fieldGroundWithLeft.type.equals(FieldType.WALL)) {
            setFalling(false);
            setCanMoveDown(false);
        }

        if ((fieldGroundWithLeft.type.equals(FieldType.EMPTY) && fieldGroundWithRight.type.equals(FieldType.EMPTY)) ||
                (fieldGroundWithLeft.type.equals(FieldType.ROPE) && fieldGroundWithRight.type.equals(FieldType.ROPE))) {
            if ( !( (leftFieldWithUpper.type.equals(FieldType.ROPE) && leftFieldWithLower.type.equals(FieldType.ROPE)) || (rightFieldWithUpper.type.equals(FieldType.ROPE) && rightFieldWithLower.type.equals(FieldType.ROPE)) )) {
                setFalling(true);
            } else {
                setFalling(false);
            }

        }

    }

    private boolean bounderies() {
        if (avatar.getX() <= 0) {
            setCanMoveLeft(false);
            return true;
        } else {
            setCanMoveLeft(true);
        }

        if (avatar.getX() + 40 >= 1200) {
            setCanMoveRight(false);
            return true;
        } else {
            setCanMoveRight(true);
        }
        return false;
    }

}
