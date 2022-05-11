package game.mageoff.movement;

import java.util.HashSet;
import java.util.Set;

public class Position {
  private double x;
  private double y;
  private Position parent;
  private final Set<Position> childSet;

  public Position(double x, double y) {
    parent = null;
    childSet = new HashSet<>();
    this.x = x;
    this.y = y;
  }

  public Position() {
    this(0, 0);
  }

  // if keepParent, parent is kept but children are not
  public Position(Position pos, boolean keepParent) {
    this(pos.x, pos.y);
    if (keepParent) setParent(pos.parent);
  }

  public Position(Position pos) {
    this(pos, false);
  }

  // gets x relative to parent
  public double getX() {
    return x;
  }

  // gets y relative to parent
  public double getY() {
    return y;
  }

  // gets absolute x
  public double getAbsX() {
    return (parent == null? 0 : parent.getX()) + x;
  }

  // gets absolute y
  public double getAbsY() {
    return (parent == null? 0 : parent.getY()) + y;
  }

  // gets absolute position
  public Position getAbsPos() {
    return new Position(getAbsX(), getAbsY());
  }

  // sets x relative to parent
  public void setX(double x) {
    this.x = x;
  }

  // sets y relative to parent
  public void setY(double y) {
    this.y = y;
  }

  // sets position relative to parent
  public void set(double x, double y) {
    this.x = x;
    this.y = y;
  }

  // sets position relative to parent
  public void set(Position pos) {
    set(pos.getX(), pos.getY());
  }

  // changes x relative to parent
  public void changeX(double x) {
    setX(getX() + x);
  }

  // changes y relative to parent
  public void changeY(double y) {
    setY(getY() + y);
  }

  public Position move(double x, double y, double multi) {
    this.x += x * multi;
    this.y += y * multi;
    return this;
  }

  public Position move(double x, double y) {
    return move(x, y, 1);
  }

  public Position move(Position pos) {
    return move(pos.x, pos.y);
  }

  public Position moveInDir(double dir, double amount) {
    x += Math.cos(dir * Math.PI / 180) * amount;
    y -= Math.sin(dir * Math.PI / 180) * amount;
    return this;
  }

  // if cloneTree, clone shares same parent but children are not cloned
  public Position clone(boolean keepParent) {
    Position pos = new Position(x, y);
    if (keepParent) setParent(parent);
    return pos;
  }

  public Position clone() {
    return clone(false);
  }

  public double distSqd(double x, double y) {
    return Math.pow(this.x - x, 2) + Math.pow(this.y - y, 2);
  }

  public double distSqd(Position pos) {
    return distSqd(pos.x, pos.y);
  }

  public double dist(double x, double y) {
    return Math.sqrt(distSqd(x, y));
  }

  public double dist(Position pos) {
    return Math.sqrt(distSqd(pos));
  }

  public String toString() {
    return "(" + x + "," + y + ")";
  }

  public double dirTo(Position pos) {
    return Math.atan2(pos.y - y, pos.x - x) * 180 / Math.PI;
  }

  public void setParent(Position pos) {
    if (parent != null) parent.removeChild(this);
    parent = pos;
    if (pos != null) pos.addChild(this);
  }

  public void setParent() {
    setParent(null);
  }

  private void addChild(Position pos) {
    childSet.add(pos);
  }

  private void removeChild(Position pos) {
    childSet.remove(pos);
  }
}
