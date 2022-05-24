package game.mageoff.deck;

import game.mageoff.combat.Attack;
import game.mageoff.combat.Lane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class CardFactory {
    public static Card testAttackCard() {
        return new Card() {
            @Override
            public void action(int laneIndex) {
                Lane.lanes[laneIndex].addPlayerAttack(new Attack(new Circle(0, 0, 5, Color.GREEN), 10, 1));
            }
        };
    }
}
