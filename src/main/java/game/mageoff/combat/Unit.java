package game.mageoff.combat;

import game.mageoff.deck.CardHandler;

// represents a participant in combat (occupies lane sides)
public class Unit {
    protected final CardHandler cardHandler;
    private int maxHealth;
    private int health;
    public Unit(CardHandler cardHandler, int health) {
        this.cardHandler = cardHandler;
        maxHealth = health;
        this.health = health;
        cardHandler.setupBattle();
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

    public void tick(double dt) {
        cardHandler.tick(dt);
    }
}
