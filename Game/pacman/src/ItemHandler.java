package src;

import ch.aplu.jgamegrid.*;
import src.utility.GameCallback;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

public  class ItemHandler extends GameGrid {

    private Game game;
    private PacManGameGrid grid;
    private ArrayList<Actor> iceCubes = new ArrayList<Actor>();
    private ArrayList<Actor> goldPieces = new ArrayList<Actor>();

    private final static int nbHorzCells = 20;
    private final static int nbVertCells = 11;
    private ArrayList<Location> pillAndItemLocations = new ArrayList<>();
    private ArrayList<Location> propertyPillLocations = new ArrayList<>();
    private ArrayList<Location> propertyGoldLocations = new ArrayList<>();

    public ItemHandler(Game game) {
        this.game = game;
        this.grid = game.getGrid();
    }

    public int countPillsAndItems() {
        int pillsAndItemsCount = 0;
        for (int y = 0; y < nbVertCells; y++)
        {
            for (int x = 0; x < nbHorzCells; x++)
            {
                Location location = new Location(x, y);
                int a = grid.getCell(location);
                if (a == 1 && propertyPillLocations.size() == 0) { // Pill
                    pillsAndItemsCount++;
                } else if (a == 3 && propertyGoldLocations.size() == 0) { // Gold
                    pillsAndItemsCount++;
                }
            }
        }
        if (propertyPillLocations.size() != 0) {
            pillsAndItemsCount += propertyPillLocations.size();
        }

        if (propertyGoldLocations.size() != 0) {
            pillsAndItemsCount += propertyGoldLocations.size();
        }

        return pillsAndItemsCount;
    }

    public void setupPillAndItemsLocations() {

        for (int y = 0; y < nbVertCells; y++) {
            for (int x = 0; x < nbHorzCells; x++) {
                Location location = new Location(x, y);
                int a = grid.getCell(location);
                if (a == 1 && propertyPillLocations.size() == 0) {
                    pillAndItemLocations.add(location);
                }
                if (a == 3 &&  propertyGoldLocations.size() == 0) {
                    pillAndItemLocations.add(location);
                }
                if (a == 4) {
                    pillAndItemLocations.add(location);
                }
            }
        }

        if (propertyPillLocations.size() > 0) {
            for (Location location : propertyPillLocations) {
                pillAndItemLocations.add(location);
            }
        }
        if (propertyGoldLocations.size() > 0) {
            for (Location location : propertyGoldLocations) {
                pillAndItemLocations.add(location);
            }
        }
    }

    public void putPill(GGBackground bg, Location location) {
        bg.fillCircle(toPoint(location), 5);
    }

    public void putGold(GGBackground bg, Location location) {
        bg.setPaintColor(Color.yellow);
        bg.fillCircle(toPoint(location), 5);
        Actor gold = new Actor("sprites/gold.png");
        goldPieces.add(gold);
        game.addActor(gold, location);
    }

    public void putIce(GGBackground bg, Location location) {
        bg.setPaintColor(Color.blue);
        bg.fillCircle(toPoint(location), 5);
        Actor ice = new Actor("sprites/ice.png");
        iceCubes.add(ice);
        game.addActor(ice, location);
    }

    public void removeItem(String type,Location location) {
        if (type.equals("gold")) {
            for (Actor item : goldPieces) {
                if (location.getX() == item.getLocation().getX() && location.getY() == item.getLocation().getY()) {
                    item.hide();
                }
            }
        } else if (type.equals("ice")) {
            for (Actor item : iceCubes) {
                if (location.getX() == item.getLocation().getX() && location.getY() == item.getLocation().getY()) {
                    item.hide();
                }
            }
        }
    }

    public ArrayList<Actor> getGoldPieces() {
        return goldPieces;
    }

    public ArrayList<Location> getPillAndItemLocations() {
        return pillAndItemLocations;
    }

    public ArrayList<Location> getPropertyPillLocations() {
        return propertyPillLocations;
    }

    public ArrayList<Location> getPropertyGoldLocations() {
        return propertyGoldLocations;
    }
}