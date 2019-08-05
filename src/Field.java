import javafx.scene.image.ImageView;

enum FieldType {
    WALL, LADDER, ROPE, TREASURE, EMPTY, EXIT
}

/**
 * Represents field at the game.
 */
public class Field {

    /**
     * x position at map.
     */
    public int row;
    /**
     * y position at map.
     */
    public int col;

    /**
     * Field type.
     */
    public FieldType type;
    /**
     * Image of the field.
     */
    public ImageView fieldImage;

    /**
     * Field constructor.
     * @param col y position at map.
     * @param row x position at map.
     * @param charType char loaded from level.
     */
    public Field(int col, int row, char charType) {
        this.row = row;
        this.col = col;

        switch (charType) {
            case '.':
            case 'R':
            case 'B':
            case 'X':
                type = FieldType.EMPTY;
                fieldImage = new ImageView();
                break;
            case '|':
                type = FieldType.LADDER;
                fieldImage = new ImageView("file:ladder.png");
                break;
            case '#':
                type = FieldType.WALL;
                fieldImage = new ImageView("file:wall.png");
                break;
            case '-':
                type = FieldType.ROPE;
                fieldImage = new ImageView("file:rope.png");
                break;
            case '€':
                type = FieldType.TREASURE;
                fieldImage = new ImageView("file:treasure.png");
                break;
        }
        fieldImage.setX(row * LodeRunner.iconSize);
        fieldImage.setY(col * LodeRunner.iconSize);
    }

}
