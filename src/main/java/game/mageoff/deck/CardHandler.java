package game.mageoff.deck;

import game.mageoff.Game;
import game.mageoff.combat.Lane;
import game.mageoff.combat.Unit;

import java.util.*;

// collection of cards for a unit
public class CardHandler {
    private final List<Card> cards;
    private final List<Card> hand;
    private final Queue<Card> drawPile;
    private final List<Card> discardPile;
    public CardHandler(Card... cards) {
        this.cards = new ArrayList<>(Arrays.stream(cards).toList());
        this.hand = new ArrayList<>();
        this.drawPile = new LinkedList<>();
        this.discardPile = new ArrayList<>();
        for (Card card : cards)
            card.hide();
    }

    // reset hand for battle
    public void setupBattle() {
        this.drawPile.clear();
        this.discardPile.clear();
        this.hand.clear();
        for (Card card : cards) {
            card.show();
            card.setFaceDown();
            discardPile.add(card);
        }
    }

    // hide cards for out of battle mode
    public void setupOutOfBattle() {
        for (Card card : drawPile)
            card.hide();
        for (Card card : discardPile)
            card.hide();
        for (Card card : hand)
            card.hide();
    }

    // moves discard pile to draw pile shuffled
    public void refillDrawPile() {
        // shuffle discard pile
        for (int i = 0; i < discardPile.size(); i++) {
            int index = (int)(Math.random() * (discardPile.size() - i)) + i;
            Card card1 = discardPile.get(i);
            Card card2 = discardPile.get(index);
            discardPile.set(index, card1);
            discardPile.set(i, card2);
        }
        // move discard pile to draw pile
        while (discardPile.size() > 0) {
            Card card = discardPile.remove(discardPile.size()-1);
            card.moveToBack();
            drawPile.add(card);
        }
    }

    // fills hand
    public void fillHand(int maxHand) {
        while (hand.size() < maxHand) {
            if (drawPile.size() == 0)
                refillDrawPile();
            if (drawPile.size() > 0) {
                Card card = drawPile.poll();
                card.setFaceUp();
                hand.add(card);
            } else
                break;
        }
    }

    // discard card in hand
    public void discardCard(int index) {
        if (index < 0 || index >= hand.size())
            throw new IndexOutOfBoundsException();
        Card card = hand.remove(index);
        card.setFaceDown();
        discardPile.add(card);
    }

    // add card to cards (goes to out of battle copy of cards)
    public void addCard(Card card) {
        card.hide();
        cards.add(card);
    }

    // add card to hand (does not persist outside of battle)
    public void addTempCard(Card card) {
        card.setFaceUp();
        card.show();
        hand.add(card);
    }

    // tick cards
    public void tick(double dt) {
        for (int i = 0; i < hand.size(); i++) {
            Card card = hand.get(i);
            if (card.isPendingActivation()) {
                // find lane
                int lane = -1;
                double minDist = -1;
                for (int l = 0; l < 5; l++) {
                    double dist = Math.abs(Lane.getX(l) - card.getX());
                    if (lane == -1 || dist < minDist) {
                        minDist = dist;
                        lane = l;
                    }
                }
                card.action(lane);
                card.resetPending();
                discardCard(i);
                i--;
                continue;
            } else if (card.isPendingDiscard()) {
                card.resetPending();
                discardCard(i);
                i--;
                continue;
            }
            double x = card.isBeingDragged()? Game.getActiveGame().mouseX : Game.width * 0.5 + (i-(hand.size()-1)/2.0)*50;
            double y = card.isBeingDragged()? Game.getActiveGame().mouseY : Game.height * 0.9;
            card.tick(dt, x, y, 0, card.isBeingDragged());
        }
        for (Card card : discardPile) {
            card.tick(dt, Game.width * 0.9, Game.height * 0.9, 0, false);
        }
        for (Card card : drawPile) {
            card.tick(dt, Game.width * 0.1, Game.height * 0.9, 0, false);
        }
        fillHand(5);
    }
}
