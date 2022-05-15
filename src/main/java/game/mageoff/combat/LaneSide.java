package game.mageoff.combat;

import game.mageoff.status.StatusEffect;

import java.util.ArrayList;
import java.util.List;

// represents a side of a lane
// all attack actions are sent through this
public class LaneSide {
    private int block;
    private Unit source;
    private Unit target;
    private List<Attack> attacks;
    public LaneSide() {
        attacks = new ArrayList<>();
        reset();
    }

    public int getBlock() {
        return block;
    }

    private void reset() {
        block = 0;
        attacks.clear();
    }

    public void setup(Unit source, Unit target) {
        reset();
        setSource(source);
        setTarget(target);
    }

    public void setSource(Unit source) {
        this.source = source;
    }

    public void setTarget(Unit target) {
        this.target = target;
    }

    public void tick(double dt, double x, double startY, double endY) {
        List<Attack> activeAttacks = new ArrayList<>();
        for (Attack attack : attacks) {
            attack.tick(this, dt, x, startY, endY);
            if (attack.isFinished())
                attack.disable();
            else
                activeAttacks.add(attack);
        }
        attacks = activeAttacks;
    }

    // apply damage to target
    public void damage(int damage) {
        if (target != null)
            target.damage(block, damage);
    }

    // apply status effect to target
    public void applyEffect(StatusEffect statusEffect) {

    }

    // add attack to lane from source
    public void addAttack(Attack attack) {
        attacks.add(attack);
    }
}
