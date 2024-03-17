// Monster.java
// Used for PacMan
package src;

import ch.aplu.jgamegrid.*;
import java.awt.Color;
import java.util.*;

public abstract class Monster extends Actor {
  private Game game;
  private MonsterType type;
  private ArrayList<Location> visitedList = new ArrayList<Location>();
  private final int listLength = 10;
  private boolean stopMoving = false;
  private boolean isFurious = false;
  private int seed = 0;
  private Random randomiser = new Random(0);

  public Monster(Game game, MonsterType type)
  {
    super("sprites/" + type.getImageName());
    this.game = game;
    this.type = type;
  }

  public void setFuriousMode(int seconds) {
    this.isFurious = true;
    final Monster monster = this;
    monster.setSlowDown(2);
    Timer timer = new Timer(); // Instantiate Timer Object
    int SECOND_TO_MILLISECONDS = 1000;
    timer.schedule(new TimerTask() {
      @Override
      public void run() {
        monster.isFurious = false;
        monster.setSlowDown(3);
      }
    }, seconds * SECOND_TO_MILLISECONDS);
  }

  public void setStopMoving(int seconds) {
    this.stopMoving = true;
    Timer timer = new Timer(); // Instantiate Timer Object
    int SECOND_TO_MILLISECONDS = 1000;
    final Monster monster = this;
    timer.schedule(new TimerTask() {
      @Override
      public void run() {
        monster.stopMoving = false;
      }
    }, seconds * SECOND_TO_MILLISECONDS);
  }

  public void setSeed(int seed) {
    this.seed = seed;
    randomiser.setSeed(seed);
  }

  public void act() {
    if (stopMoving) {
      return;
    }
    if (game.getPacActor() != null && game.getPacActor().gameGrid != null) { walkApproach(); }
    if (getDirection() > 150 && getDirection() < 210)
      setHorzMirror(false);
    else
      setHorzMirror(true);
  }

  private void addVisitedList(Location location) {
    visitedList.add(location);

    if (visitedList.size() == listLength) {
      visitedList.remove(0);
    }
  }

  public boolean isVisited(Location location) {
    for (Location loc : visitedList)
      if (loc.equals(location))
        return true;
    return false;
  }

  public boolean canMove(Location location) {
    Color c = getBackground().getColor(location);
    if (c.equals(Color.gray) || location.getX() >= game.getNumHorzCells()
            || location.getX() < 0 || location.getY() >= game.getNumVertCells() || location.getY() < 0)
      return false;
    else
      return true;
  }

  public void walkApproach() {
    Location pacLocation = game.getPacActor().getLocation();
    double oldDirection = getDirection();

    Location.CompassDirection compassDir =
            getLocation().get4CompassDirectionTo(pacLocation);
    Location next = getLocation().getNeighbourLocation(compassDir);
    setDirection(compassDir);

    monsterWalk(oldDirection, next);

    game.getGameCallback().monsterLocationChanged(this);
    this.addVisitedList(this.getLocation());
     // make this specific to TX5 or
  }

  public abstract void monsterWalk(double oldDirection, Location next);

  public void randomWalk(double oldDirection, Location next) {
    int sign = getrandom();
    setDirection(oldDirection);
    turn(sign * 90);
    next = getNextMoveLocation();
    if (canMove(next)) {
      setLocation(next);
    }
    else {
      setDirection(oldDirection);
      next = getNextMoveLocation();
      if (canMove(next)) {
        setLocation(next);
      }
      else {
        setDirection(oldDirection);
        turn(-sign * 90);
        next = getNextMoveLocation();
        if (canMove(next)) {
          setLocation(next);
        }
        else {
          setDirection(oldDirection);
          turn(180);
          next = getNextMoveLocation();
          setLocation(next);
        }
      }
    }
  }

  public void findNext(Location next, Location destination, ArrayList<Location> possibleNeighbourLocations) {
    int minDist = Integer.MAX_VALUE;
    next = null;
    ArrayList<Location> sameDistNeighbour = new ArrayList<Location>();

    for (Location location : possibleNeighbourLocations) {
      int dist = location.getDistanceTo(destination);

      if (dist < minDist) {
        minDist = dist;
        next = location;
      }
    }

    for (Location location : possibleNeighbourLocations) {
      int dist = location.getDistanceTo(destination);

      if (dist == minDist) {
        sameDistNeighbour.add(location);
      }
    }

    if (sameDistNeighbour.size() > 0) {
      Random randomiser = new Random();
      int arrayPosition = randomiser.nextInt(sameDistNeighbour.size());
      next = sameDistNeighbour.get(arrayPosition);
    }

    if (next != null) {
      setLocation(next);
    }
  }

  public void findPossibleNeighbourLocations(Location current, ArrayList<Location> possibleNeighbourLocations) {
    for (Location.CompassDirection direction : Location.CompassDirection.values()) {
      Location next = current.getNeighbourLocation(direction);
      if (isPossible(next)) {
        possibleNeighbourLocations.add(next);
      }
    }
  }

  // Overridden according to requirements of each specific monster
  public boolean isPossible(Location next) {
    return true;
  }

  public int getrandom() {
    int sign = randomiser.nextDouble() < 0.5 ? 1 : -1;
    return sign;
  }

  public MonsterType getType() {
    return type;
  }

  public Game getGame() {
    return game;
  }
}


