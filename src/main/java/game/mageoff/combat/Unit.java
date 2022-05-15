package game.mageoff.combat;

// represents a participant in combat (occupies lane sides)
public class Unit {
    private int maxHealth;
    private int health;
    public Unit(int health) {
        maxHealth = health;
        this.health = health;
    }

    // handle damage to self and return remaining block
    public int damage(int block, int damage) {
        int blockChange = Math.min(block, damage);
        block -= blockChange;
        damage -= blockChange;
        if (damage > 0) {
            health -= damage;
            if (health < 0)
                health = 0;
        }
        return block;
    }
}
