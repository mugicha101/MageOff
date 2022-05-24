package game.mageoff.deck;

import game.mageoff.Game;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

// handles general card behavior and renders base layer
public abstract class Card {
    public static final Group cardGroup = new Group();
    protected static final double moveMulti = 0.01;
    protected static final double quickMoveMulti = 0.1;
    protected static final double width = 40;
    protected static final double height = 60;
    protected static final double arc = 10;
    protected static final double activateY = Game.height * 0.75; // any y <= activeY activates card on mouse drag release
    protected static final double discardX = Game.width * 0.8; // any x >= discardX discards card on mouse drag release (if not in activate zone)

    protected final Group group; // whole card
    protected final Group frontGroup; // front of card
    protected final Group backGroup; // back of card
    private double x;
    private double y;
    private double dir;
    private boolean isFaceDown; // if true, card is face down and front is hidden
    private double flipValue; // for flip animation (-1 is front shown, 1 is back shown)
    private boolean isDragging; // is being dragged by the player
    private boolean pendingActivation; // waiting for activation
    private boolean pendingDiscard; // waiting for discard
    public Card(double spawnX, double spawnY, double spawnDir, boolean spawnFlipped) {
        group = new Group();
        frontGroup = new Group();
        backGroup = new Group();
        x = spawnX;
        y = spawnY;
        dir = spawnDir;
        isFaceDown = spawnFlipped;
        flipValue = 0;
        pendingActivation = false;
        pendingDiscard = false;
        group.getChildren().addAll(backGroup, frontGroup);
        show();

        Rectangle base = new Rectangle(-width*0.5, -height*0.5, width, height);
        base.setFill(Color.GREEN);
        base.setArcWidth(arc);
        base.setArcHeight(arc);
        frontGroup.getChildren().add(base);

        Rectangle back = new Rectangle(-width*0.5, -height*0.5, width, height);
        back.setFill(Color.BROWN);
        back.setArcWidth(arc);
        back.setArcHeight(arc);
        backGroup.getChildren().add(back);

        group.addEventHandler(MouseDragEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                isDragging = true;
            }
        });
        group.addEventHandler(MouseDragEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                isDragging = false;
                if (y <= activateY) {
                    pendingActivation = true;
                    pendingDiscard = false;
                } else if (x >= discardX) {
                    pendingDiscard = true;
                    pendingActivation = false;
                }
            }
        });
    }

    public Card(double spawnX, double spawnY, double spawnDir) {
        this(spawnX, spawnY, spawnDir, false);
    }

    public Card(double spawnX, double spawnY) {
        this(spawnX, spawnY, 0);
    }

    public Card() {
        this(0, 0);
    }

    public void tick(double dt, double targetX, double targetY, double targetDir, boolean quickMove) {
        for (int i = 0; i < dt * 1000; i++) {
            x += (targetX - x) * (quickMove? quickMoveMulti : moveMulti);
            y += (targetY - y) * (quickMove? quickMoveMulti : moveMulti);
            dir += (targetDir - dir) * (quickMove? quickMoveMulti : moveMulti);
        }
        flipValue += (isFaceDown ? 1 : -1) * dt * 10;
        if (flipValue > 1) flipValue = 1;
        if (flipValue < -1) flipValue = -1;

        group.setScaleX(Math.abs(flipValue));
        if (flipValue < 0) {
            frontGroup.setOpacity(1);
            backGroup.setOpacity(0);
        } else {
            backGroup.setOpacity(1);
            frontGroup.setOpacity(0);
        }
        group.setTranslateX(x);
        group.setTranslateY(y);
        group.setRotate(dir);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setDir(double dir) {
        this.dir = dir;
    }

    // make card invisible
    public void hide() {
        cardGroup.getChildren().remove(group);
    }

    public void show() {
        hide();
        cardGroup.getChildren().add(group);
    }

    // makes card render in front of other cards
    public void moveToFront() {
        hide();
        show();
    }

    // makes card render behind other cards
    public void moveToBack() {
        hide();
        cardGroup.getChildren().add(0, group);
    }

    public void setFaceDown() {
        isFaceDown = true;
    }

    public void setFaceUp() {
        isFaceDown = false;
    }

    public boolean isBeingDragged() {
        return isDragging;
    }

    public boolean isPendingActivation() {
        return pendingActivation;
    }

    public boolean isPendingDiscard() {
        return pendingDiscard;
    }

    public void resetPending() {
        pendingActivation = false;
        pendingDiscard = false;
    }

    public abstract void action(int laneIndex); // cards action
}
