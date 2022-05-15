package game.mageoff.status;

// a status effect
public abstract class StatusEffect {
    private final String name;
    private final int level;
    public StatusEffect(String name, int level) {
        this.name = name;
        this.level = level;
    }

    public final String getName() {
        return name;
    }

    // advances time by dt seconds
    public abstract void tick(double dt);

    // returns whether the status effect is finished
    public abstract boolean isFinished();

    // provides a description of the effect (scaled with level)
    public abstract String description();

    // augments damage attacks do
    public int augmentAttackDamage(int damage) {
        return damage;
    }

    // augments block gained
    public int augmentBlockGain(int block) {
        return block;
    }
}
