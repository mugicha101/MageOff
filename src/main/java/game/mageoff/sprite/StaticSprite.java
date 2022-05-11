package game.mageoff.sprite;

import javafx.scene.Group;
import javafx.scene.image.Image;

public class StaticSprite extends Sprite {
  private final Image img;
  private final String imgPath;

  public StaticSprite(Group sceneGroup, String imgPath, double[] offset, double scale, boolean startEnabled) {
    super(sceneGroup, offset, scale, startEnabled);
    this.imgPath = imgPath;
    img = createImage(imgPath);
  }

  protected Image getImage() {
    return img;
  }

  @Override
  public StaticSprite clone() {
    return new StaticSprite(getSceneGroup(), imgPath, new double[] {offset[0], offset[1]}, scale, isEnabled());
  }
}
