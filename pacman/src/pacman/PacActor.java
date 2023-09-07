
package src.pacman;

import ch.aplu.jgamegrid.*;
import src.pacman.autoplayer.AutoplayerStrategy;
import src.pacman.autoplayer.FindClosestStrategy;
import src.pacman.autoplayer.PortalStrategy;
import src.pacman.portals.Portal;

import java.awt.event.KeyEvent;
import java.awt.Color;
import java.util.*;

public class PacActor extends Actor implements GGKeyRepeatListener {
  private static final int NB_SPRITES = 4;
  private static final int LIST_LENGTH = 10;
  private int idSprite = 0;
  private int nbPills = 0;
  private int score = 0;
  private Game game;
  private ItemHandler itemHandler;
  private ArrayList<Location> visitedList = new ArrayList<Location>();
  private List<String> propertyMoves = new ArrayList<>();
  private int propertyMoveIndex = 0;
  private final int listLength = LIST_LENGTH;
  private int seed;
  private Random randomiser = new Random();
  private boolean isAuto = false;
  private Location previousLocation = null;
  private Location destination;
  public boolean hasTeleported = false;
  private boolean useFindClosestStrategy;

  public PacActor(Game game) {
    super(true, "sprites/pacpix.gif", NB_SPRITES);  // Rotatable
    this.game = game;
  }

  public void setAuto(boolean auto) {
    isAuto = auto;
  }

  public void setSeed(int seed) {
    this.seed = seed;
    randomiser.setSeed(seed);
  }

  public void setPropertyMoves(String propertyMoveString) {
    if (propertyMoveString != null) {
      this.propertyMoves = Arrays.asList(propertyMoveString.split(","));
    }
  }

  public void keyRepeated(int keyCode) {
    if (isAuto || hasTeleported) {
      return;
    }
    if (isRemoved())  // Already removed
      return;
    Location next = null;
    switch (keyCode) {
      case KeyEvent.VK_LEFT:
        next = getLocation().getNeighbourLocation(Location.WEST);
        setDirection(Location.WEST);
        break;
      case KeyEvent.VK_UP:
        next = getLocation().getNeighbourLocation(Location.NORTH);
        setDirection(Location.NORTH);
        break;
      case KeyEvent.VK_RIGHT:
        next = getLocation().getNeighbourLocation(Location.EAST);
        setDirection(Location.EAST);
        break;
      case KeyEvent.VK_DOWN:
        next = getLocation().getNeighbourLocation(Location.SOUTH);
        setDirection(Location.SOUTH);
        break;
    }
    if (next != null && canMove(next)) {
      setLocation(next);
      eatPill(next);
    }
  }

  public void act() {
    ArrayList<Integer> tileTypes = new ArrayList<>(Arrays.asList(game.grid.PILL_TILE, game.grid.GOLD_TILE));
    ArrayList<Location> needPortalToAccess = needPortal(tileTypes, true);

    int numCanAccess = game.getItemHandler().getAllPillandGoldLocations().size() - needPortalToAccess.size();
    if (numCanAccess == 0 || (destination != null && !isAccessible(destination, needPortalToAccess))) {
      useFindClosestStrategy = false;
    } else {
      useFindClosestStrategy = true;
    }

    hasTeleported = false;
    Location currentLocation = getLocation();
    if (previousLocation != null && !previousLocation.equals(currentLocation)) {
      if (steppedOnPortal() != null) {
        if (!hasJustComeFromPair(steppedOnPortal())) {
          teleport(this, steppedOnPortal());
          hasTeleported = true;
        }
      }
    }

    show(idSprite);
    idSprite++;
    if (idSprite == NB_SPRITES)
      idSprite = 0;
    itemHandler = game.getItemHandler();

    if (isAuto && !hasTeleported) {
      if (useFindClosestStrategy) {
        AutoplayerStrategy strategy = new FindClosestStrategy();
        strategy.playAutoplayer(this);
      }
      else {
        AutoplayerStrategy strategy = new PortalStrategy();
        strategy.playAutoplayer(this);
      }
    }

    previousLocation = currentLocation;
    this.game.getGameCallback().pacManLocationChanged(getLocation(), score, nbPills);
    hasTeleported = false;
  }

  public void addVisitedList(Location location) {
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

  private boolean canMove(Location location) {
    Color c = getBackground().getColor(location);
    if (c.equals(Color.gray) || location.getX() >= game.grid.getNbHorzCells()
            || location.getX() < 0 || location.getY() >= game.grid.getNbVertCells() || location.getY() < 0)
      return false;
    else
      return true;
  }

  public void eatPill(Location location) {
    Color c = getBackground().getColor(location);
    if (c.equals(Color.white)) {
      nbPills++;
      score++;
      getBackground().fillCell(location, Color.lightGray);
      game.getGameCallback().pacManEatPillsAndItems(location, "pills");
      itemHandler.removePillandGoldLocation(location);

    } else if (c.equals(Color.yellow)) {
      nbPills++;
      score+= 5;
      getBackground().fillCell(location, Color.lightGray);
      game.getGameCallback().pacManEatPillsAndItems(location, "gold");
      itemHandler.removeItem("gold", location);
      itemHandler.removePillandGoldLocation(location);

    } else if (c.equals(Color.blue)) {
      getBackground().fillCell(location, Color.lightGray);
      game.getGameCallback().pacManEatPillsAndItems(location, "ice");
      itemHandler.removeItem("ice", location);
    }
    String title = "[PacMan in the Multiverse] Current score: " + score;
    gameGrid.setTitle(title);
  }

  public int getNbPills() {
    return nbPills;
  }
  public Portal steppedOnPortal() {
    for (Portal portal: game.getPortalFacade().getPortals()) {
      if (this.getLocation().equals(portal.getLocation())) {
        return portal;
      }
    }
    return null;
  }

  public boolean hasJustComeFromPair(Portal portal) {
    return portal.getPair().getLocation().equals(this.getPreviousLocation());
  }

  public void teleport(PacActor pacman, Portal portal) {
    setLocation(portal.getPair().getLocation());
  }

  public Location getPreviousLocation() {
    return previousLocation;
  }

  public Location findNext() {
    ArrayList<Location> possibleNeighbourLocations = new ArrayList<Location>();

    for (Location.CompassDirection direction : Location.CompassDirection.values()) {
      if (direction.toString().length() <= 5) {
        Location neighbour = this.getLocation().getNeighbourLocation(direction);
        if (canMove(neighbour) && !isVisited(neighbour)) {
          possibleNeighbourLocations.add(neighbour);
        }
      }
    }

    // Stuck in a deadend
    if (possibleNeighbourLocations.size() == 0) {
      for (Location.CompassDirection direction : Location.CompassDirection.values()) {
        if (direction.toString().length() <= 5) {
          Location neighbour = this.getLocation().getNeighbourLocation(direction);
          if (canMove(neighbour)) {
            possibleNeighbourLocations.add(neighbour);
          }
        }
      }
    }
    return findClosest(possibleNeighbourLocations, destination);
  }

  public Location findNextWithoutPortal() {
    ArrayList<Location> possibleNeighbourLocations = new ArrayList<Location>();

    for (Location.CompassDirection direction : Location.CompassDirection.values()) {
      if (direction.toString().length() <= 5) {
        Location neighbour = this.getLocation().getNeighbourLocation(direction);
        if (canMove(neighbour) && !isVisited(neighbour) && !isOnPortal(neighbour)) {
          possibleNeighbourLocations.add(neighbour);
        }
      }
    }
    // Stuck in a deadend
    if (possibleNeighbourLocations.size() == 0) {
      for (Location.CompassDirection direction : Location.CompassDirection.values()) {
        if (direction.toString().length() <= 5) {
          Location neighbour = this.getLocation().getNeighbourLocation(direction);
          if (canMove(neighbour)) {
            possibleNeighbourLocations.add(neighbour);
          }
        }
      }
    }
    return findClosest(possibleNeighbourLocations, destination);
  }

  public Location findClosest(ArrayList<Location> possibleLocations, Location destination) {
    Location locationFound = null;
    int minDist = Integer.MAX_VALUE;
    ArrayList<Location> sameDistDestination = new ArrayList<>();
    for (Location location : possibleLocations) {
      int dist = location.getDistanceTo(destination);
      if (dist < minDist) {
        minDist = dist;
        locationFound = location;
      }
    }

    for (Location location : possibleLocations) {
      int dist = location.getDistanceTo(destination);
      if (dist == minDist) {
        sameDistDestination.add(location);
      }
    }

    if (sameDistDestination.size() > 0) {
      Random randomiser = new Random();
      int arrayPositon = randomiser.nextInt(sameDistDestination.size());
      locationFound = sameDistDestination.get(arrayPositon);
    }
    return locationFound;
  }

  public void setNbPills(int nbPills) {
    this.nbPills = nbPills;
  }

  public void setScore(int score) {
    this.score = score;
  }

  public ItemHandler getItemHandler() {
    return itemHandler;
  }

  public void setDestination(Location destination) {
    this.destination = destination;
  }

  public Location getDestination() {
    return destination;
  }

  public ArrayList<Location> needPortal(ArrayList<Integer> tileTypes, boolean alreadyEatenCheck) {
    boolean[][] visited = new boolean[game.grid.getNbVertCells()][game.grid.getNbHorzCells()];
    ArrayList<Location> locationsNeedPortal = new ArrayList<Location>();

    floodFill(visited);

    for (int y = 0; y < game.grid.getNbVertCells(); y++) {
      for (int x = 0; x < game.grid.getNbHorzCells(); x++) {
        Location location = new Location(x, y);
        int a = game.grid.getCell(location);
        for (int tileInt : tileTypes) {
          if (a == tileInt && !visited[y][x]) {
            if (alreadyEatenCheck && !alreadyEaten(location)) {
              locationsNeedPortal.add(location);
            }
            if (!alreadyEatenCheck) {
              locationsNeedPortal.add(location);
            }
          }
        }
      }
    }
    return locationsNeedPortal;
  }

  public void floodFill(boolean[][] visited) {
    Queue<Location> queue = new LinkedList<>();

    visited[this.getY()][this.getX()] = true;
    queue.add(this.getLocation());

    // Define the valid neighbors for BFS
    int[] x_ne = {0, 0, -1, 1};
    int[] y_ne = {-1, 1, 0, 0};

    while (!queue.isEmpty()) {
      Location currentLocation = queue.poll();
      int currentX = currentLocation.getX();
      int currentY = currentLocation.getY();

      for (int i = 0; i < 4; i++) {
        int neighbourX = currentX + x_ne[i];
        int neighbourY = currentY + y_ne[i];

        if (isValidLocation(neighbourX, neighbourY)) {
          Location location = new Location(neighbourX, neighbourY);
          int a = game.grid.getCell(location);

          if (a != game.grid.WALL_TILE && !visited[neighbourY][neighbourX]) {
            visited[neighbourY][neighbourX] = true;
            queue.add(location);
          }
        }
      }
    }
  }

  private boolean isValidLocation(int x, int y) {
    return x >= 0 && x < game.grid.getNbHorzCells() && y >= 0 && y < game.grid.getNbVertCells();
  }

  public Game getGame() {
    return game;
  }

  public boolean alreadyEaten(Location location) {
    ArrayList<Location> allPillandGoldLocations = getGame().getItemHandler().getAllPillandGoldLocations();
    for (int i = 0; i < allPillandGoldLocations.size(); i++) {
      if (allPillandGoldLocations.get(i).equals(location)) {
        return false;
      }
    }
    return true;
  }

  public boolean isOnPortal(Location neighbour) {
    for (Portal portal : game.getPortalFacade().getPortals()) {
      if (portal.getLocation().equals(neighbour)){
        return true;
      }
    }
    return false;
  }

  public boolean isAccessible(Location location, ArrayList<Location> removeLocations) {
    for (Location unaccessibleLocation : removeLocations) {
      if (unaccessibleLocation.equals(location)) {
        return false;
      }
    }
    return true;
  }
}