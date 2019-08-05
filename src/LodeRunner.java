import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.ArrayList;

/**
 * Main class, represents application context.
 */
public class LodeRunner extends Application {

    /**
     * Size of one field in the game.
     */
    static final int iconSize = 40;

    static private final int allTreasures = 6;
    private char[][] charMap = Levels.level1;
    private final int width = charMap[0].length * iconSize;
    private final int height = charMap.length * iconSize;
    private Stage primaryStage;
    private LodeRunnerPanel menuPane;

    private ArrayList<Field> exitFields = new ArrayList<>();
    private ArrayList<ArrayList<Field>> map = new ArrayList<>();
    private ArrayList<Bot> bots = new ArrayList<>();
    private Runner runner = new Runner();
    private int treasures = 0;
    private boolean died = false;
    private Field nextLevelField;

    private KeyCode left = KeyCode.LEFT;
    private KeyCode right = KeyCode.RIGHT;
    private KeyCode up = KeyCode.UP;
    private KeyCode down = KeyCode.DOWN;

    private Text leftBindedText;
    private Text rightBindedText;
    private Text upBindedText;
    private Text downBindedText;
    private boolean bindChange = false;

    @Override
    public void start(Stage primaryStage) {
        LodeRunnerPanel menuPane = new LodeRunnerPanel();
        Scene menuScene = new Scene(menuPane, 500, 300, Color.BLACK);
        this.primaryStage = primaryStage;
        this.menuPane = menuPane;
        primaryStage.setTitle("LodeRunner");
        primaryStage.setScene(menuScene);
        primaryStage.show();
        setupMenu();
    }

    private void setupMenu() {
        Rectangle startGameButton = new Rectangle(50, 100, 100, 60);
        startGameButton.setStroke(Color.WHITE);
        Text startGameText = new Text(65, 135, "Start Game");
        startGameText.setFill(Color.WHITE);

        startGameButton.setOnMouseClicked(e -> {
            startGame();
        });

        leftBindedText = new Text(410, 60, "Left");
        rightBindedText = new Text(410, 120, "Right");
        upBindedText = new Text(410, 180, "Up");
        downBindedText = new Text(410, 240, "Down");

        leftBindedText.setFill(Color.WHITE);
        rightBindedText.setFill(Color.WHITE);
        upBindedText.setFill(Color.WHITE);
        downBindedText.setFill(Color.WHITE);

        Rectangle leftBind = new Rectangle(400, 30, 50, 50);
        Rectangle rightBind = new Rectangle(400, 90, 50, 50);
        Rectangle upBind = new Rectangle(400, 150, 50, 50);
        Rectangle downBind = new Rectangle(400, 210, 50, 50);

        leftBind.setStroke(Color.WHITE);
        rightBind.setStroke(Color.WHITE);
        upBind.setStroke(Color.WHITE);
        downBind.setStroke(Color.WHITE);

        leftBind.setOnMouseClicked(e -> {
            if (bindChange) {
                return;
            }
            bindChange = true;
            leftBind.setFill(Color.WHITE);
            leftBind.requestFocus();
        });

        leftBind.setOnKeyReleased(e -> {
            if (leftBind.getFill().equals(Color.WHITE)) {
                left = e.getCode();
                leftBind.setFill(Color.BLACK);
                leftBindedText.setText(left.getName());
                bindChange = false;
            }
        });

        rightBind.setOnMouseClicked(e -> {
            if (bindChange) {
                return;
            }
            bindChange = true;
            rightBind.setFill(Color.WHITE);
            rightBind.requestFocus();
        });

        rightBind.setOnKeyReleased(e -> {
            if (rightBind.getFill().equals(Color.WHITE)) {
                right = e.getCode();
                rightBind.setFill(Color.BLACK);
                rightBindedText.setText(right.getName());
                bindChange = false;
            }
        });

        upBind.setOnMouseClicked(e -> {
            if (bindChange) {
                return;
            }
            bindChange = true;
            upBind.setFill(Color.WHITE);
            upBind.requestFocus();
        });

        upBind.setOnKeyReleased(e -> {
            if (upBind.getFill().equals(Color.WHITE)) {
                up = e.getCode();
                upBind.setFill(Color.BLACK);
                upBindedText.setText(up.getName());
                bindChange = false;
            }
        });

        downBind.setOnMouseClicked(e -> {
            if (bindChange) {
                return;
            }
            bindChange = true;
            downBind.setFill(Color.WHITE);
            downBind.requestFocus();
        });

        downBind.setOnKeyReleased(e -> {
            if (downBind.getFill().equals(Color.WHITE)) {
                down = e.getCode();
                downBind.setFill(Color.BLACK);
                downBindedText.setText(down.getName());
                bindChange = false;
            }
        });

        menuPane.getChildren().addAll(startGameButton, startGameText, leftBind, rightBind, upBind, downBind, leftBindedText, rightBindedText, upBindedText, downBindedText);
    }

    private void startGame() {
        LodeRunnerPanel root = new LodeRunnerPanel();
        root.setFocusTraversable(true);
        Scene scene = new Scene(root, width, height, Color.BLACK);
        primaryStage.setScene(scene);
        primaryStage.show();

        root.createFieldMap();
        root.drawMap();
        root.drawRunner();
        root.requestFocus();

        root.setOnKeyPressed(e -> {
            if (e.getCode().equals(left)) {
                runner.setMovingLeft(true);
            } else if (e.getCode().equals(right)) {
                runner.setMovingRight(true);
            } else if (e.getCode().equals(up)) {
                runner.setMovingUp(true);
            } else if (e.getCode().equals(down)) {
                runner.setMovingDown(true);
            }
        });

        root.setOnKeyReleased(e -> {
            if (e.getCode().equals(left)) {
                runner.setMovingLeft(false);
            } else if (e.getCode().equals(right)) {
                runner.setMovingRight(false);
            } else if (e.getCode().equals(up)) {
                runner.setMovingUp(false);
            } else if (e.getCode().equals(down)) {
                runner.setMovingDown(false);
            }
            runner.stopMove();
        });

        Timeline tl = new Timeline(new KeyFrame(new Duration(25), e -> {
            root.update();
        }));
        tl.setCycleCount(Timeline.INDEFINITE);
        tl.play();
    }

    /**
     * Represents pane for game.
     */
    class LodeRunnerPanel extends Pane {

        /**
         * Game rendering.
         */
        void update() {
            if (died) {
                resetGame();
                return;
            }

            runner.update();

            for (Bot bot : bots) {
                bot.move();
                bot.update();
                bot.checkMoves();
                getChildren().remove(bot.avatar);
                getChildren().add(bot.avatar);
            }

            checkMoves();

            getChildren().remove(runner.avatar);
            getChildren().add(runner.avatar);
        }

        /**
         * Reads array of chars and creates field map.
         */
        void createFieldMap() {
            getChildren().clear();
            for (int i = 0; i < charMap.length; i++) {
                ArrayList<Field> inner = new ArrayList<>();
                for (int j = 0; j < charMap[i].length; j++) {
                    Field field = new Field(i, j, charMap[i][j]);
                    inner.add(field);
                    if (charMap[i][j] == 'X') {
                        exitFields.add(field);
                        if (nextLevelField == null) {
                            nextLevelField = field;
                        }
                    }
                    if (charMap[i][j] == 'R') {
                        runner.setX(j * iconSize);
                        runner.setY(i * iconSize);
                    }
                    if (charMap[i][j] == 'B') {
                        Bot bot = new Bot(runner);
                        bot.setX(j * iconSize);
                        bot.setY(i * iconSize);
                        bot.map = map;
                        bots.add(bot);
                    }
                }
                map.add(inner);
            }
        }

        /**
         * Draws all the map.
         */
        void drawMap() {
            for (int i = 0; i < map.size(); i++) {
                for (int j = 0; j < map.get(i).size(); j++) {
                    getChildren().add(map.get(i).get(j).fieldImage);
                }
            }
        }

        /**
         * Draws the runner.
         */
        void drawRunner() {
            getChildren().add(runner.avatar);
        }

        /**
         * Updates player movement possibilities.
         */
        void checkMoves() {
            int runnerLeftX = runner.leftX();
            int runnerRightX = runner.rightX();
            int runnerUpperY = runner.upperY();
            int runnerLowerY = runner.lowerY();
            int runnerLeftActualX = runner.leftActualX();
            int runnerRightActualX = runner.rightActualX();
            int runnerUpperActualY = runner.upperActualY();
            int runnerLowerActualY = runner.lowerActualY();

            if (bounderies()) {
                return;
            }

            if (runner.avatar.getY() == nextLevelField.fieldImage.getY()) {
                if (charMap == Levels.level1) {
                    charMap = Levels.level2;
                } else {
                    charMap = Levels.level1;
                }
                died = true;
            }

            for (Bot bot : bots) {
                if (bot.isPointIn(runner.avatar.getX(), runner.avatar.getY()) ||
                        bot.isPointIn(runner.avatar.getX() + iconSize, runner.avatar.getY()) ||
                        bot.isPointIn(runner.avatar.getX(), runner.avatar.getY() + iconSize) ||
                        bot.isPointIn(runner.avatar.getX() + iconSize, runner.avatar.getY() + iconSize)) {
                        died = true;
                }
            }

            Field leftFieldWithUpper = map.get(runnerUpperActualY).get(runnerLeftX);
            Field rightFieldWithUpper = map.get(runnerUpperActualY).get(runnerRightX);

            Field leftFieldWithLower = map.get(runnerLowerActualY).get(runnerLeftX);
            Field rightFieldWithLower = map.get(runnerLowerActualY).get(runnerRightX);

            if (leftFieldWithUpper.type.equals(FieldType.WALL) || leftFieldWithLower.type.equals(FieldType.WALL)) {
                runner.setCanMoveLeft(false);
            } else {
                runner.setCanMoveLeft(true);
            }

            if (rightFieldWithUpper.type.equals(FieldType.WALL) || rightFieldWithLower.type.equals(FieldType.WALL)) {
                runner.setCanMoveRight(false);
            } else {
                runner.setCanMoveRight(true);
            }

            if (rightFieldWithUpper.type.equals(FieldType.TREASURE)) {
                pickTreasure(rightFieldWithUpper);
            }

            if (leftFieldWithUpper.type.equals(FieldType.TREASURE)) {
                pickTreasure(leftFieldWithUpper);
            }

            Field leftActualField = map.get(runnerLowerActualY).get(runnerLeftActualX);
            Field rightActualField = map.get(runnerLowerActualY).get(runnerRightActualX);

            if (leftActualField.type.equals(FieldType.LADDER) && rightActualField.type.equals(FieldType.LADDER)) {
                runner.setCanMoveUp(true);
                runner.setCanMoveDown(true);
            } else {
                runner.setCanMoveUp(false);
                runner.setCanMoveDown(false);
            }

            Field fieldGroundWithLeft = map.get(runnerLowerY).get(runnerLeftActualX);
            Field fieldGroundWithRight = map.get(runnerLowerY).get(runnerRightActualX);

            if (((leftActualField.type.equals(FieldType.EMPTY) && rightActualField.type.equals(FieldType.EMPTY)) || ((leftActualField.type.equals(FieldType.ROPE) && rightActualField.type.equals(FieldType.ROPE))))
                    && fieldGroundWithLeft.type.equals(FieldType.LADDER) && fieldGroundWithRight.type.equals(FieldType.LADDER)) {
                runner.setFalling(false);
                runner.setCanMoveDown(true);
            }

            if (fieldGroundWithLeft.type.equals(FieldType.EMPTY) && fieldGroundWithRight.type.equals(FieldType.EMPTY) && leftActualField.type.equals(FieldType.ROPE) && rightActualField.type.equals(FieldType.ROPE)) {
                runner.setCanMoveDown(true);
            }

            if (fieldGroundWithLeft.type.equals(FieldType.WALL)) {
                runner.setFalling(false);
                runner.setCanMoveDown(false);
            }

            if ((fieldGroundWithLeft.type.equals(FieldType.EMPTY) && fieldGroundWithRight.type.equals(FieldType.EMPTY)) ||
                    (fieldGroundWithLeft.type.equals(FieldType.ROPE) && fieldGroundWithRight.type.equals(FieldType.ROPE))) {
                if ( !( (leftFieldWithUpper.type.equals(FieldType.ROPE) && leftFieldWithLower.type.equals(FieldType.ROPE)) || (rightFieldWithUpper.type.equals(FieldType.ROPE) && rightFieldWithLower.type.equals(FieldType.ROPE)) )) {
                    runner.setFalling(true);
                } else {
                    runner.setFalling(false);
                }

            }

        }

        /**
         * Game is set to default state.
         */
        void resetGame() {
            treasures = 0;
            bots.clear();
            map.clear();
            exitFields.clear();
            runner = new Runner();
            createFieldMap();
            drawMap();
            died = false;
        }

        /**
         * Pick a treasure field.
         * @param field Field on what the treasure is.
         */
        void pickTreasure(Field field) {
            getChildren().remove(field.fieldImage);
            field.type = FieldType.EMPTY;
            treasures++;

            if (treasures == allTreasures) {
                unlockExit();
            }
        }

        /**
         * Shows exit ladder, which leads to a next level.
         */
        void unlockExit() {
            for (Field field : exitFields) {
                field.type = FieldType.LADDER;
                field.fieldImage = new ImageView("file:ladder.png");
                field.fieldImage.setX(field.row * LodeRunner.iconSize);
                field.fieldImage.setY(field.col * LodeRunner.iconSize);
                getChildren().add(field.fieldImage);
            }
        }

        /**
         * Checks bounderies of the map.
         * @return If character is at the edge of the map.
         */
        boolean bounderies() {
            if (runner.avatar.getX() <= 0) {
                runner.setCanMoveLeft(false);
                return true;
            } else {
                runner.setCanMoveLeft(true);
            }

            if (runner.avatar.getX() + iconSize >= width) {
                runner.setCanMoveRight(false);
                return true;
            } else {
                runner.setCanMoveRight(true);
            }
            return false;
        }

    }

}
