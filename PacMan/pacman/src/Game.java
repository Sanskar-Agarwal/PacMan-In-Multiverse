// PacMan.java
// Simple PacMan implementation
package src;

import ch.aplu.jgamegrid.*;
import src.utility.GameCallback;

import java.awt.*;
import java.util.Properties;

public class Game extends GameGrid {

  private final static int SLOW_DOWN_FACTOR = 3;
  private final static int CELL_SIZE = 20;
  private final static int KEY_REPEAT_READER = 150;
  private final static int STIMULATION_PERIOD = 100;
  private final static int TX5_DELAY = 5;
  private final static int nbHorzCells = 20;
  private final static int nbVertCells = 11;
  private final static String WIN_MSG = "YOU WIN";
  private final static String GAME_OVER = "GAME OVER";
  private final static String GAME_TITLE = "[PacMan in the Multiverse]";

  private PacManGameGrid grid = new PacManGameGrid(nbHorzCells, nbVertCells);
  private PacActor pacActor = new PacActor(this);
  private MonsterHandler monsterHandler;
  private ItemHandler itemHandler;
  private GGBackground bg = getBg();

  private boolean hasPacmanBeenHit;
  private boolean hasPacmanEatAllPills;

  private GameCallback gameCallback;
  private Properties properties;
  private int seed = 30006;

  private boolean isMultiverse;

  public Game(GameCallback gameCallback, Properties properties) {
    // Setup game
    super(nbHorzCells, nbVertCells, CELL_SIZE, false);
    this.gameCallback = gameCallback;
    this.properties = properties;
    gameSetUp();

    // Run the game
    doRun();
    show();

    // Look for collisions and if game has ended
    lookForCollisions();
    checkGameEnd();

  }

  private void gameSetUp() {
    setSimulationPeriod(STIMULATION_PERIOD);
    setTitle(GAME_TITLE);

    // Setup for auto test
    pacActor.setPropertyMoves(properties.getProperty("PacMan.move"));
    pacActor.setAuto(Boolean.parseBoolean(properties.getProperty("PacMan.isAuto")));

    setMultiverse(properties.getProperty("version").equals("simple") ? false : true);

    monsterHandler = new MonsterHandler(this);
    monsterHandler.setUpMonsters(this);

    itemHandler = new ItemHandler(this);
    itemHandler.setupPillAndItemsLocations();

    loadPillAndItemsLocations();
    drawGrid(bg);

    setCharacterSeeds();

    addKeyRepeatListener(pacActor);
    setKeyRepeatPeriod(KEY_REPEAT_READER);

    setGameSlowDown();
    monsterHandler.delayTx5(TX5_DELAY);

    setupActorLocations();

  }

  private void lookForCollisions() {

    // Loop to look for collision in the application thread
    // This makes it improbable that we miss a hit
    do {
      hasPacmanBeenHit = hasPacmanBeenHit();
      hasPacmanEatAllPills = hasPacmanEatAllPills();
      delay(10);
    } while(!hasPacmanBeenHit && !hasPacmanEatAllPills);
    delay(120);
  }

  private void checkGameEnd() {
    Location loc = pacActor.getLocation();
    pacActor.removeSelf();

    String title = "";
    if (hasPacmanBeenHit) {
      bg.setPaintColor(Color.red);
      title = GAME_OVER;
      addActor(new Actor("sprites/explosion3.gif"), loc);
    } else if (hasPacmanEatAllPills) {
      bg.setPaintColor(Color.yellow);
      title = WIN_MSG;
    }
    setTitle(title);
    gameCallback.endOfGame(title);
  }

  private boolean hasPacmanBeenHit() {
    for (Monster monster : monsterHandler.getMonsters()) {
      if (monster.getLocation().equals(pacActor.getLocation())) {
        return true;
      }
    }
    return false;
  }

  private boolean hasPacmanEatAllPills() {
    return pacActor.getNbPills() >= itemHandler.countPillsAndItems();
  }

  private void setGameSlowDown() {
    pacActor.setSlowDown(SLOW_DOWN_FACTOR);
    monsterHandler.setMonstersSlowDown(SLOW_DOWN_FACTOR);
  }

  private void setCharacterSeeds() {
    seed = Integer.parseInt(properties.getProperty("seed"));
    pacActor.setSeed(seed);
    monsterHandler.setMonsterSeeds(seed);
  }

  private void setupActorLocations() {
    monsterHandler.getActorMap().put("PacMan", pacActor);
    for (String key : monsterHandler.getActorMap().keySet()) {
      String[] locations = this.properties.getProperty(key + ".location").split(",");
      int x = Integer.parseInt(locations[0]);
      int y = Integer.parseInt(locations[1]);

      Actor actor = monsterHandler.getActorMap().get(key);
      Location location = new Location(x, y);
      if (actor instanceof PacActor) {
        addActor(actor, location);
      } else {
        addActor(actor, location, Location.NORTH);
      }
    }
  }

  private void loadPillAndItemsLocations() {
    String pillsLocationString = properties.getProperty("Pills.location");
    if (pillsLocationString != null) {
      String[] singlePillLocationStrings = pillsLocationString.split(";");
      for (String singlePillLocationString: singlePillLocationStrings) {
        String[] locationStrings = singlePillLocationString.split(",");
        itemHandler.getPropertyPillLocations().add(new Location(Integer.parseInt(locationStrings[0]),
                Integer.parseInt(locationStrings[1])));
      }
    }

    String goldLocationString = properties.getProperty("Gold.location");
    if (goldLocationString != null) {
      String[] singleGoldLocationStrings = goldLocationString.split(";");
      for (String singleGoldLocationString: singleGoldLocationStrings) {
        String[] locationStrings = singleGoldLocationString.split(",");
        itemHandler.getPropertyGoldLocations().add(new Location(Integer.parseInt(locationStrings[0]),
                Integer.parseInt(locationStrings[1])));
      }
    }
  }

  private void drawGrid(GGBackground bg) {
    bg.clear(Color.gray);
    bg.setPaintColor(Color.white);
    for (int y = 0; y < nbVertCells; y++)
    {
      for (int x = 0; x < nbHorzCells; x++)
      {
        bg.setPaintColor(Color.white);
        Location location = new Location(x, y);
        int a = grid.getCell(location);
        if (a > 0)
          bg.fillCell(location, Color.lightGray);
        if (a == 1 && itemHandler.getPropertyPillLocations().size() == 0) { // Pill
          itemHandler.putPill(bg, location);
        } else if (a == 3 && itemHandler.getPropertyGoldLocations().size() == 0) { // Gold
          itemHandler.putGold(bg, location);
        } else if (a == 4) {
          itemHandler.putIce(bg, location);
        }
      }
    }

    for (Location location : itemHandler.getPropertyPillLocations()) {
      itemHandler.putPill(bg, location);
    }

    for (Location location : itemHandler.getPropertyGoldLocations()) {
      itemHandler.putGold(bg, location);
    }
  }

  public MonsterHandler getMonsterHandler() {
    return monsterHandler;
  }
  public GameCallback getGameCallback() {
    return gameCallback;
  }
  private void setMultiverse(boolean isMultiverse) {
    this.isMultiverse = isMultiverse;
  }
  public boolean getIsMultiverse() {
    return this.isMultiverse;
  }
  public int getNumHorzCells(){
    return this.nbHorzCells;
  }
  public int getNumVertCells(){
    return this.nbVertCells;
  }
  public ItemHandler getItemHandler() {
    return itemHandler;
  }
  public PacActor getPacActor() {
    return pacActor;
  }
  public PacManGameGrid getGrid() {
    return grid;
  }
}