
package src.pacman;

import ch.aplu.jgamegrid.*;
import src.mapeditor.editor.Controller;
import src.pacman.levelchecking.LevelChecker;
import src.pacman.monsters.Monster;
import src.pacman.monsters.MonsterFacade;
import src.pacman.portals.PortalFacade;
import src.pacman.utility.GameCallback;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

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

  protected PacManGameGrid grid;

  private PacActor pacActor = new PacActor(this);
  private MonsterFacade monsterFacade;
  private PortalFacade portalFacade;
  private ItemHandler itemHandler;
  private GGBackground bg = getBg();

  private boolean hasPacmanBeenHit;
  private boolean hasPacmanEatAllPills;

  private GameCallback gameCallback;
  private Properties properties;
  private int seed = 30006;

  private String levelsFolder;
  private String currentLevelPath;
  private int currentLevel;
  private int maxLevel = 0;
  private HashMap<Integer, String> levelFiles = new HashMap<>();
  private boolean passedGameChecking = true;
  private boolean passedLevelChecking = true;

  public Game(GameCallback gameCallback, Properties properties, String folderPath) {
    // Setup game
    super(nbHorzCells, nbVertCells, CELL_SIZE, false);
    this.gameCallback = gameCallback;
    this.properties = properties;
    this.levelsFolder = folderPath;

    gameCheckingAndSetup();
    doLevelChecking();
    setFirstLevel();

    // Only play game in test mode if all levels in folder pass level checking and game checking
    if (passedGameChecking && passedLevelChecking) {
      play(currentLevel);
    }
  }

  private void play(int currentLevel) {

    if (levelFiles.containsKey(currentLevel)) {
      currentLevelPath = levelFiles.get(currentLevel);
      grid = new PacManGameGrid(nbHorzCells, nbVertCells, currentLevelPath);
      playLevel();
    } else if (currentLevel < maxLevel) {

      for (int i = currentLevel; i <= maxLevel; i++) {
        if (levelFiles.containsKey(i)) {
          currentLevel = i;
          currentLevelPath = levelFiles.get(currentLevel);
          grid = new PacManGameGrid(nbHorzCells, nbVertCells, currentLevelPath);
          playLevel();
        }
      }
    } else { // Finished all levels - win and end of the game
      bg.setPaintColor(Color.yellow);
      String title = WIN_MSG;
      setTitle(WIN_MSG);
      gameCallback.endOfGame(title);
      // Open edit mode with no current map
      new Controller();
    }
  }

  private void playLevel() {
    gameSetUp();

    setupPacActorStart();
    setupMonsterLocations();

    // Run the game
    doRun();
    show();

    // Look for collisions and if game has ended
    lookForCollisions();
    checkGameEnd();

  }

  private void gameCheckingAndSetup() {

    File gameFolder = new File(levelsFolder);
    File[] allLevels = gameFolder.listFiles();
    ArrayList<Integer> duplicateLevelNums = new ArrayList<>();

    for (File levelFile : allLevels) {
      if (levelFile.isFile()) {
        String fileName = levelFile.getName();
        String filePath = gameFolder + "/" + fileName;

        if (isXmlFile(filePath)) {
          int levelNumber = getLevelFromFile(fileName);
          if (levelNumber != 0) { // adds file only if valid
            if (!levelFiles.keySet().contains(levelNumber)) {
              levelFiles.put(levelNumber, filePath);
            }
            else {
              duplicateLevelNums.add(levelNumber);
            }
          }
        }
      }
    }

    duplicateLevelsError(duplicateLevelNums, allLevels);

    if (levelFiles.isEmpty()) {
      passedGameChecking=false;
      gameCallback.writeError("[" + levelsFolder + " folder - no maps found]");
      new Controller();
    }

  }

  private void doLevelChecking() {

    ArrayList<Integer> erroredLevels = new ArrayList<>();

    // Do level checking for every valid file in the game folder
    for (Integer level : levelFiles.keySet()) {
      String levelFile = levelFiles.get(level);
      PacManGameGrid levelGrid = new PacManGameGrid(nbHorzCells, nbVertCells, levelFile);
      LevelChecker levelChecker = new LevelChecker(levelGrid);

      // If check fails, log it in the ErrorLog.txt file
      if (levelChecker.doLevelCheck()) {
        ArrayList<String> errorMessages = levelChecker.getErrorMessages();
        erroredLevels.add(level);
        gameCallback.logErrorMessages(errorMessages);
      }
    }

    // If there are errors in any of the maps, open the editor on the lowest errorred level and do not start game
    if (erroredLevels.size() != 0) {
      Collections.sort(erroredLevels);
      passedLevelChecking = false;

    // Only open editor on specific file if there is no game checking fails
      if (passedGameChecking) {
        new Controller(levelFiles.get(erroredLevels.get(0)));
      }
    }
  }

  private void duplicateLevelsError(ArrayList<Integer> duplicateLevelNums, File[] allLevels) {

    ArrayList<String> duplicateLevelPaths = new ArrayList<>();

    if (duplicateLevelNums.size() > 0) {
      for (int level : duplicateLevelNums) {
        for (File levelFile : allLevels) {
          if (getLevelFromFile(levelFile.getName()) == level && isXmlFile(levelFile.getPath())) {
            duplicateLevelPaths.add(levelFile.getName());
          }
        }
      }

      String errorMessage = "[" + levelsFolder + " folder - multiple maps at same level: ";
      for (int i = 0; i < duplicateLevelPaths.size(); i++) {
        errorMessage += duplicateLevelPaths.get(i) + "; ";
      }
      passedGameChecking = false;
      gameCallback.writeError(errorMessage.substring(0, errorMessage.length() - 2) + "]");
      new Controller();
    }

  }

  private void setFirstLevel() {

    ArrayList<Integer> levelNumbers = new ArrayList<>(levelFiles.keySet());
    Collections.sort(levelNumbers);
    currentLevel = levelNumbers.get(0);

  }


  private int getLevelFromFile(String fileName) {

    int levelNumber = 0;
    char[] chars = fileName.toCharArray();

    if (Character.isDigit(chars[0])) {
      // First digit
      levelNumber = Character.getNumericValue(chars[0]);

      // Check and read level in for if level number is more than one digit
      for (int i=1; i<chars.length; i++) {
        char c = chars[i];
        if (Character.isDigit(chars[i])) {
          levelNumber = levelNumber * 10 + Character.getNumericValue(c);

        } else {
          break;
        }
      }
    }
    if (levelNumber > maxLevel) {
      maxLevel = levelNumber;
    }
    return levelNumber;
  }

  private void gameSetUp() {
    setSimulationPeriod(STIMULATION_PERIOD);
    setTitle(GAME_TITLE);

    // Setup for auto test
    pacActor.setPropertyMoves(properties.getProperty("PacMan.move"));
    pacActor.setAuto(Boolean.parseBoolean(properties.getProperty("PacMan.isAuto")));

    monsterFacade = new MonsterFacade();
    monsterFacade.setUpMonsters(this);

    itemHandler = new ItemHandler(this);
    itemHandler.setupPillAndItemsLocations();

    portalFacade = new PortalFacade(this);
    portalFacade.setUpPortals();

    drawGrid(bg);

    setCharacterSeeds();

    addKeyRepeatListener(pacActor);
    setKeyRepeatPeriod(KEY_REPEAT_READER);

    setGameSlowDown();
    monsterFacade.delayTx5(TX5_DELAY);

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

    if (hasPacmanBeenHit) {
      gameOver(loc);

    } else if (hasPacmanEatAllPills) {
      pacActor.setNbPills(0);
      pacActor.setScore(0);
      currentLevel++;
      portalFacade.removePortals();
      pacActor.setDestination(null);
      play(currentLevel);
    }
  }

  private void gameOver(Location pacManLocation) {
    bg.setPaintColor(Color.red);
    String title = GAME_OVER;
    addActor(new Actor("sprites/explosion3.gif"), pacManLocation);
    setTitle(title);
    gameCallback.endOfGame(title);
    new Controller(); // open blank editor
  }


  private boolean hasPacmanBeenHit() {
    for (Monster monster : monsterFacade.getMonsters()) {
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
    monsterFacade.setMonstersSlowDown(SLOW_DOWN_FACTOR);
  }

  private void setCharacterSeeds() {
    seed = Integer.parseInt(properties.getProperty("seed"));
    pacActor.setSeed(seed);
    monsterFacade.setMonsterSeeds(seed);
  }

  private void setupPacActorStart() {
    Location pacActorInitialPosition = grid.getPacActorLocation();
    addActor(pacActor, pacActorInitialPosition);
  }

  private void setupMonsterLocations() {
    Map<Monster, Location> monsterInitialPositions = monsterFacade.getMonsterLocations();

    for (Map.Entry<Monster, Location> entry : monsterInitialPositions.entrySet()) {

      Monster monster = entry.getKey();
      Location position = entry.getValue();

      addActor(monster, position, Location.NORTH);
    }
  }

  private void drawGrid(GGBackground bg) {
    bg.clear(Color.gray);
    bg.setPaintColor(Color.white);
    for (int y = 0; y < nbVertCells; y++) {
      for (int x = 0; x < nbHorzCells; x++) {
        bg.setPaintColor(Color.white);
        Location location = new Location(x, y);
        int a = grid.getCell(location);
        if (a > 0) {
          bg.fillCell(location, Color.lightGray);
        }
      }
    }
    itemHandler.putItems();
    portalFacade.putPortals();
  }


  public static boolean isXmlFile(String filePath) {
    try {
      File file = new File(filePath);
      BufferedReader reader = new BufferedReader(new FileReader(file));
      String firstLine = reader.readLine();
      reader.close();
      return firstLine.startsWith("<?xml");
    } catch (IOException e) {
      return false;
    }
  }

  public GameCallback getGameCallback() {
    return gameCallback;
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

  public PortalFacade getPortalFacade() {
    return portalFacade;
  }

}