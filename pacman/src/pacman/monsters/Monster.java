// Monster.java
// Used for PacMan
package src.pacman.monsters;

import ch.aplu.jgamegrid.*;
import src.pacman.Game;
import src.pacman.portals.Portal;

import java.awt.Color;
import java.util.*;

public abstract class Monster extends Actor {
  private Game game;
  private MonsterType type;
  private ArrayList<Location> visitedList = new ArrayList<Location>();
  private final int LIST_LENGTH = 10;
  private boolean stopMoving = false;
  private int seed = 0;
  private Random randomiser = new Random(0);

  private Location previousLocation = null;
  public Monster(Game game, MonsterType type)
  {
    super("sprites/" + type.getImageName());
    this.game = game;
    this.type = type;
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
    if (game.getPacActor() != null && game.getPacActor().gameGrid != null) {
      walkApproach();
    }
    if (getDirection() > 150 && getDirection() < 210)
      setHorzMirror(false);
    else
      setHorzMirror(true);
  }

  private void addVisitedList(Location location) {
    visitedList.add(location);

    if (visitedList.size() == LIST_LENGTH) {
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
    Location currentLocation = getLocation();
    boolean hasMoved = false;

    Location.CompassDirection compassDir =
            getLocation().get4CompassDirectionTo(pacLocation);
    Location next = getLocation().getNeighbourLocation(compassDir);
    setDirection(compassDir);

    if (steppedOnPortal() != null) {
      if (!hasJustComeFromPair(steppedOnPortal())) {
        teleport(this, steppedOnPortal());
        hasMoved = true;
      }
    }

    if (!hasMoved) {
      monsterWalk(oldDirection, next);
    }

    previousLocation = currentLocation;

    game.getGameCallback().monsterLocationChanged(this);
    this.addVisitedList(this.getLocation());
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

  public boolean hasJustComeFromPair(Portal portal) {
    return portal.getPair().getLocation().equals(this.getPreviousLocation());
  }

  public void teleport(Monster monster, Portal portal) {
    monster.setLocation(portal.getPair().getLocation());
  }

  public Portal steppedOnPortal() {
    for (Portal portal : game.getPortalFacade().getPortals()) {
      if (this.getLocation().equals(portal.getLocation())) {
        return portal;
      }
    }
    return null;
  }

  public Location getPreviousLocation() {
    return previousLocation;
  }
}


