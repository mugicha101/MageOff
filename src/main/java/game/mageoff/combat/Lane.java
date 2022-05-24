package game.mageoff.combat;

import game.mageoff.Game;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

// represents a combat lane that attacks are sent down
public class Lane {
    public static final double width = Game.width * 0.08;
    public static final Group laneGroup = new Group();
    public static final Lane[] lanes = initLanes();
    private static Lane[] initLanes() {
        Lane[] lanes = new Lane[5];
        for (int i = 0; i < lanes.length; i++)
            lanes[i] = new Lane(i);
        return lanes;
    }

    public static void setupLanes(Unit player, Unit opponent) {
        for (Lane lane : lanes) lane.setup(player, opponent);
    }

    public static void tickLanes(double dt) {
        for (Lane lane : lanes)
            lane.tick(dt);
    }

    public static double getX(int index) {
        return Game.width * 0.5 + (index - 2) * Game.width * 0.15;
    }

    private static double getArc(int index) {
        return Math.pow(Math.abs(index-2), 2) * 0.01;
    }

    private static double getTopY(int index) {
        return Game.height * (0.15 + getArc(index));
    }

    private static double getBottomY(int index) {
        return Game.height * (0.75 + getArc(index));
    }

    private final Group group;
    private final Rectangle rect;
    private final LaneSide playerSide;
    private final LaneSide opposingSide;
    private final int index;
    private Lane(int index) {
        this.index = index;
        playerSide = new LaneSide();
        opposingSide = new LaneSide();

        group = new Group();
        rect = new Rectangle();
        laneGroup.getChildren().add(group);
        group.getChildren().setAll(rect);
    }

    private void updateGroup() {
        rect.setTranslateX(getX(index));
        rect.setFill(Color.RED);
        rect.setHeight(getBottomY(index) - getTopY(index));
        rect.setY(getTopY(index));
        rect.setX(width * -0.5);
        rect.setWidth(width);
    }

    public void setup(Unit player, Unit opponent) {
        playerSide.setup(player, opponent);
        opposingSide.setup(opponent, player);
    }

    public void tick(double dt) {
        playerSide.tick(dt, getX(index), getBottomY(index), getTopY(index));
        opposingSide.tick(dt, getX(index), getTopY(index), getBottomY(index));
        updateGroup();
    }

    private void addAttack(LaneSide side, Attack attack) {
        side.addAttack(attack);
    }

    public void addPlayerAttack(Attack attack) {
        addAttack(playerSide, attack);
    }

    public void addOpposingAttack(Attack attack) {
        addAttack(opposingSide, attack);
    }
}
