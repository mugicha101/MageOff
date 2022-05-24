package game.mageoff.combat;

import game.mageoff.Game;
import game.mageoff.status.StatusEffect;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;

import java.util.List;

// represents an attack on a lane
public class Attack {
    private static final double hitAnimationTime = 0.25;
    public static final Group attackGroup = new Group();

    private final Node display;
    private final double time; // time in seconds it takes to reach the opponent
    private double age; // age of the attack in seconds (stops incrementing after hit)
    private double hitAge; // age of the attack after hit in seconds
    private final int damage;
    private final List<StatusEffect> effects;
    private boolean hit;
    public Attack(Node display, double time, int damage, StatusEffect... effects) {
        this.display = display;
        display.setOpacity(0);
        attackGroup.getChildren().add(display);
        this.time = time;
        age = 0;
        hitAge = 0;
        this.damage = damage;
        this.effects = List.of(effects);
        hit = false;
    }

    public void tick(LaneSide laneSide, double dt, double x, double startY, double endY) {
        if (hit) {
            hitAge += dt;
            if (hitAge > hitAnimationTime)
                hitAge = hitAnimationTime;
        } else {
            age += dt;
            if (age >= time) {
                hitAge += age - time;
                age = time;
                hit = true;
                apply(laneSide);
            }
        }

        display.setOpacity(hit? 1 - hitAge / hitAnimationTime : 1);
        double scale = hit? 1 + 1.5 * Math.sqrt(hitAge / hitAnimationTime) : Math.min(1, Math.pow(age * 2, 0.5));
        display.setScaleX(scale);
        display.setScaleY(scale);
        Bounds bounds = display.getBoundsInParent();
        display.setTranslateX(x - (bounds.getCenterX() - display.getTranslateX()));
        double dY = (endY < startY)? bounds.getMinY() - display.getTranslateY() : bounds.getMaxY() - display.getTranslateY();
        endY -= dY;
        startY -= dY;
        display.setTranslateY(startY + (endY - startY) * age/time);
    }

    // tells lane if this attack is finished
    public boolean isFinished() {
        return hitAge >= hitAnimationTime;
    }

    // apply attack to enemy
    public void apply(LaneSide laneSide) {
        laneSide.damage(damage);
        for (StatusEffect effect : effects)
            laneSide.applyEffect(effect);
    }

    // prep attack for removal
    public void disable() {
        attackGroup.getChildren().remove(display);
    }
}
