package game.mageoff;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;

public class Game extends Application {
    private static Game activeGame;
    public static final double width = 960;
    public static final double height = 540;
    public static final double border = 5;
    public static final Color marginColor = Color.color(0, 0.5, 0.5);
    public static final Color borderColor = Color.color(0.5, 1, 1);
    public static final Color backFill = Color.BLACK;
    public static Game getActiveGame() {
        return activeGame;
    }

    private Stage stage;
    private Scene scene;
    private BorderPane root;
    private StackPane center;
    private AnchorPane display; // scaled pane (content coordinate pane maxed at width and height)
    private Rectangle paddingLeft;
    private Rectangle paddingRight;
    private Rectangle paddingTop;
    private Rectangle paddingBottom;
    private int tickCount;

    private double zoomMulti;

    public int getTick() {
        return tickCount;
    }

    @Override
    public void start(Stage stage) {
        setupStage(stage);
        setupGame();
        activeGame = this;
    }

    private void setupStage(Stage stage) {
        this.stage = stage;
        root = new BorderPane();
        scene = new Scene(root);
        center = new StackPane();
        display = new AnchorPane();
        display.setMinWidth(width);
        display.setMaxWidth(width);
        display.setMinHeight(height);
        display.setMaxHeight(height);
        center.getChildren().add(display);
        paddingTop = new Rectangle();
        paddingBottom = new Rectangle();
        paddingLeft = new Rectangle();
        paddingRight = new Rectangle();
        root.setCenter(center);
        root.setTop(paddingTop);
        root.setBottom(paddingBottom);
        root.setLeft(paddingLeft);
        root.setRight(paddingRight);
        paddingLeft.setFill(Color.TRANSPARENT);
        paddingRight.setFill(Color.TRANSPARENT);
        paddingTop.setFill(Color.TRANSPARENT);
        paddingBottom.setFill(Color.TRANSPARENT);
        root.setBackground(new Background(new BackgroundFill(marginColor, CornerRadii.EMPTY, Insets.EMPTY)));
        center.setBackground(new Background(new BackgroundFill(borderColor, CornerRadii.EMPTY, Insets.EMPTY)));
        display.setBackground(new Background(new BackgroundFill(backFill, CornerRadii.EMPTY, Insets.EMPTY)));
        display.setClip(new Rectangle(0, 0, width, height));
        stage.setScene(scene);
        stage.setTitle("Mage Off");
        stage.setResizable(true);
        stage.show();
        stage.setWidth(width);
        stage.setHeight(height);
        resizeScreen();
        stage.widthProperty().addListener((obs, oldVal, newVal) -> resizeScreen());
        stage.heightProperty().addListener((obs, oldVal, newVal) -> resizeScreen());
        stage.centerOnScreen();
        Input.init(stage);
        zoomMulti = 1;
        tickCount = -1;
        Timeline tl = new Timeline(new KeyFrame(Duration.millis(17), e -> tick()));
        tl.setCycleCount(Timeline.INDEFINITE);
        tl.play();
    }

    private void setupGame() {

    }

    public double getScreenWidth() {
        return width + border * 2;
    }

    public double getScreenHeight() {
        return height + border * 2;
    }

    private void resizeScreen() {
        double w = getScreenWidth();
        double h = getScreenHeight();
        double topOffset = stage.getScene().getWindow().getHeight() - stage.getScene().getHeight();
        double sideOffset = stage.getScene().getWindow().getWidth() - stage.getScene().getWidth();
        double sw = stage.getWidth() - sideOffset;
        double sh = stage.getHeight() - topOffset;
        double scaleVal = Math.min(sw / w, sh / h);
        double hPadding = sw - scaleVal * w;
        double vPadding = sh - scaleVal * h;
        paddingLeft.setWidth(hPadding/2);
        paddingRight.setWidth(hPadding/2);
        paddingLeft.setHeight(sh - vPadding);
        paddingRight.setHeight(sh - vPadding);
        paddingTop.setHeight(vPadding/2);
        paddingBottom.setHeight(vPadding/2);
        paddingTop.setWidth(sw);
        paddingBottom.setWidth(sw);
        center.setMinWidth(w * scaleVal);
        center.setMaxWidth(w * scaleVal);
        center.setMinHeight(h * scaleVal);
        center.setMaxHeight(h * scaleVal);
        display.setScaleX(scaleVal);
        display.setScaleY(scaleVal);
    }

    private void tick() {
        tickCount++;
        Input.keyTick();
        resizeScreen();
    }

    public static void main(String[] args) {
        launch();
    }
}