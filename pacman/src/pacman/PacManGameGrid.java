// PacGrid.java
package src.pacman;

import ch.aplu.jgamegrid.*;

import org.jdom.JDOMException;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import java.io.File;
import java.io.IOException;
import java.util.List;


public class PacManGameGrid {


  private int pillAndGoldCount = 0;
  private int nbHorzCells;
  private int nbVertCells;
  public int[][] mazeArray;
  public Game game;
  public String filePath;
  public String fileName;
  private Location pacActorLocation;
  private int numPacActors = 0;
  public static final int WALL_TILE = 0;
  public static final int PILL_TILE = 1;
  public static final int PATH_TILE = 2;
  public static final int GOLD_TILE = 3;
  public static final int ICE_TILE = 4;
  public static final int PORTAL_WHITE_TILE = 5;
  public static final int PORTAL_YELLOW_TILE = 6;
  public static final int PORTAL_DARK_GRAY_TILE = 7;
  public static final int PORTAL_DARK_GOLD_TILE = 8;
  public static final int TX5_TILE = 9;
  public static final int TROLL_TILE = 10;
  public static final int PAC_TILE = 11;

  public PacManGameGrid(int nbHorzCells, int nbVertCells, String filePath) {
    this.nbHorzCells = nbHorzCells;
    this.nbVertCells = nbVertCells;
    this.filePath = filePath;
    mazeArray = new int[nbVertCells][nbHorzCells];
    loadMap();
  }

  private void loadMap() {

    try {
      File mapFile = new File(filePath);
      fileName = mapFile.getName();
      SAXBuilder saxBuilder = new SAXBuilder();
      Document document = saxBuilder.build(mapFile);
      Element rootElement = document.getRootElement();

        // Fill in the map dimensions
        Element sizeElement = rootElement.getChild("size");
        setNbVertCells(Integer.parseInt(sizeElement.getChildText("height")));
        setNbHorzCells(Integer.parseInt(sizeElement.getChildText("width")));

        mazeArray = new int[nbVertCells][nbHorzCells];

        List rowElements = rootElement.getChildren("row");
        for (int yCoord = 0; yCoord < rowElements.size(); yCoord++) {

        Element rowElement = (Element) rowElements.get(yCoord);
        List cellElements = rowElement.getChildren("cell");

        for (int xCoord = 0; xCoord < cellElements.size(); xCoord++) {
          Element cellElement = (Element) cellElements.get(xCoord);
          String cellValue = cellElement.getText();

          // Put into array accordingly

            if (cellValue.equals("WallTile")) {
              mazeArray[yCoord][xCoord] = WALL_TILE;

            } else if (cellValue.equals("PillTile")) {
              mazeArray[yCoord][xCoord] = PILL_TILE;
              pillAndGoldCount++;

            } else if (cellValue.equals("PathTile")) {
              mazeArray[yCoord][xCoord] = PATH_TILE;

            } else if (cellValue.equals("GoldTile")) {
              mazeArray[yCoord][xCoord] = GOLD_TILE;
              pillAndGoldCount++;

            } else if (cellValue.equals("IceTile")) {
              mazeArray[yCoord][xCoord] = ICE_TILE;

            } else if (cellValue.equals("PortalWhiteTile")) {
              mazeArray[yCoord][xCoord]  = PORTAL_WHITE_TILE;

            } else if (cellValue.equals("PortalYellowTile")) {
              mazeArray[yCoord][xCoord]  = PORTAL_YELLOW_TILE;

            } else if (cellValue.equals("PortalDarkGrayTile")) {
              mazeArray[yCoord][xCoord]  = PORTAL_DARK_GRAY_TILE;

            } else if (cellValue.equals("PortalDarkGoldTile")) {
              mazeArray[yCoord][xCoord]  = PORTAL_DARK_GOLD_TILE;

            } else if (cellValue.equals("TX5Tile")) {
              mazeArray[yCoord][xCoord]  = TX5_TILE;

            } else if (cellValue.equals("TrollTile")) {
              mazeArray[yCoord][xCoord] = TROLL_TILE;

            } else if (cellValue.equals("PacTile")) {
              mazeArray[yCoord][xCoord] = PAC_TILE;
              pacActorLocation = new Location(xCoord, yCoord);
              numPacActors++;

            }
          }
        }

      } catch (JDOMException e) {
        e.printStackTrace();

      } catch (IOException e) {
        e.printStackTrace();
      }

      setMazeArray(mazeArray);

  }

  public int getNumPacActors() {
    return numPacActors;
  }

  public Location getPacActorLocation() {
    return pacActorLocation;
  }

  public int getNbHorzCells() {
    return nbHorzCells;
  }

  public void setNbHorzCells(int nbHorzCells) {
    this.nbHorzCells = nbHorzCells;
  }

  public void setNbVertCells(int nbVertCells) {
    this.nbVertCells = nbVertCells;
  }

  public int getNbVertCells() {
    return nbVertCells;
  }

  public int[][] getMazeArray() {
    return mazeArray;
  }

  public void setMazeArray(int[][] mazeArray) {
    this.mazeArray = mazeArray;
  }

  public int getCell(Location location) {
    return mazeArray[location.y][location.x];
  }

  public String getFilePath() {
    return filePath;
  }
  public String getFileName() {
    return fileName;
  }
}
