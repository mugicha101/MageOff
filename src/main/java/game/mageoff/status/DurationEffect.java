package game.mageoff.status;

// status effect with a duration
public abstract class DurationEffect extends StatusEffect {
    private double duration; // remaining duration in seconds
    public DurationEffect(String name, int level, double duration) {
        super(name, level);
        this.duration = duration;
    }

    @Override
    public void tick(double dt) {
        duration -= dt;
        if (duration < 0)
            duration = 0;
    }

    @Override
    public boolean isFinished() {
        return duration <= 0;
    }
}
