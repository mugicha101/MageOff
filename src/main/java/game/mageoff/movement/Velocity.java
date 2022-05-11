package game.mageoff.movement;

public class Velocity extends Position {
  private double xVelo;
  private double yVelo;
  private double resistance;

  public Velocity(double x, double y, double xVelo, double yVelo, double resistance) {
    super(x, y);
    this.xVelo = xVelo;
    this.yVelo = yVelo;
    this.resistance = resistance;
  }
  public Velocity(double x, double y, double xVelo, double yVelo) {
    this(x, y, xVelo, yVelo, 0);
  }
  public Velocity(Position pos, double xVelo, double yVelo, double resistance) {
    this(pos.getX(), pos.getY(), xVelo, yVelo, resistance);
  }
  public Velocity(Position pos, double xVelo, double yVelo) {
    this(pos, xVelo, yVelo, 0);
  }
  public Velocity(Position pos, double resistance) {
    this(pos, 0, 0, resistance);
  }
  public Velocity() {
    this(new Position(), 0, 0, 0);
  }
  public double getXVelo() {
    return xVelo;
  }
  public double getYVelo() {
    return yVelo;
  }
  public double getResistance() {
    return resistance;
  }
  public void setResistance(double resistance) {
    this.resistance = resistance;
  }
  public void setXVelo(double xVelo) {
    this.xVelo = xVelo;
  }
  public void setYVelo(double yVelo) {
    this.yVelo = yVelo;
  }
  public void changeXVelo(double xVelo) {
    this.xVelo += xVelo;
  }
  public void changeYVelo(double yVelo) {
    this.yVelo += yVelo;
  }
  public void tick() {
    changeX(xVelo);
    changeY(yVelo);
    xVelo -= xVelo * resistance;
    yVelo -= yVelo * resistance;
  }
  public void add(double x, double y) {
    this.xVelo += x;
    this.yVelo += y;
  }
  public Velocity clone() {
    return new Velocity(getX(), getY(), xVelo, yVelo, resistance);
  }
}
