package src.pacman.portals;
import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.*;
import src.pacman.Game;
import src.pacman.PacManGameGrid;

import java.awt.*;
import java.util.*;

public class PortalFacade extends GameGrid {
    private ArrayList<Portal> portals;
    private HashMap<PortalColour, ArrayList<Portal>> portalPairs;
    private Game game;
    private PacManGameGrid grid;
    private final static int nbHorzCells = 20;
    private final static int nbVertCells = 11;
    private ArrayList<Actor> allPortalActors = new ArrayList<>();

    public PortalFacade(Game game) {
        this.game = game;
        this.grid = game.getGrid();
        portals = new ArrayList<Portal>();
        portalPairs = new HashMap<PortalColour, ArrayList<Portal>>();
    }

    public void putPortals() {
        GGBackground bg = game.getBg();

        for (Portal portal : portals) {
                bg.setPaintColor(Color.white);
                Location location = portal.getLocation();
                int a = grid.getCell(location);

                if (a == grid.PORTAL_WHITE_TILE) {
                    putWhitePortal(bg, location);
                } else if (a == grid.PORTAL_YELLOW_TILE) {
                    putYellowPortal(bg, location);
                } else if (a == grid.PORTAL_DARK_GRAY_TILE) {
                    putDarkGrayPortal(bg, location);
                } else if (a == grid.PORTAL_DARK_GOLD_TILE) {
                    putDarkGoldPortal(bg, location);
                }

        }
    }

    public void setUpPortals() {
        for (int y = 0; y < nbVertCells; y++) {
            for (int x = 0; x < nbHorzCells; x++) {
                Location location = new Location(x, y);
                int a = grid.getCell(location);
                if (a == grid.PORTAL_WHITE_TILE) {
                    Portal portal = new Portal(PortalColour.White, location);
                    portals.add(portal);
                }
                if (a == grid.PORTAL_YELLOW_TILE) {
                    Portal portal = new Portal(PortalColour.Yellow, location);
                    portals.add(portal);
                }
                if (a == grid.PORTAL_DARK_GRAY_TILE) {
                    Portal portal = new Portal(PortalColour.DarkGray, location);
                    portals.add(portal);
                }
                if (a == grid.PORTAL_DARK_GOLD_TILE) {
                    Portal portal = new Portal(PortalColour.DarkGold, location);
                    portals.add(portal);
                }
            }
        }
        findPortalPairs();

        for (Portal portal : portals) {
            findColourPair(portal);
        }
    }

    public void putWhitePortal(GGBackground bg, Location location) {
        bg.setPaintColor(Color.magenta);
        bg.fillCircle(toPoint(location), 5);
        Actor whitePortal = new Actor(PortalColour.White.getImageName());
        game.addActor(whitePortal, location);
        allPortalActors.add(whitePortal);
    }

    public void putYellowPortal(GGBackground bg, Location location) {
        bg.setPaintColor(Color.pink);
        bg.fillCircle(toPoint(location), 5);
        Actor yellowPortal = new Actor(PortalColour.Yellow.getImageName());
        game.addActor(yellowPortal, location);
        allPortalActors.add(yellowPortal);
    }

    public void putDarkGoldPortal(GGBackground bg, Location location) {
        bg.setPaintColor(Color.cyan);
        bg.fillCircle(toPoint(location), 5);
        Actor darkGoldPortal = new Actor(PortalColour.DarkGold.getImageName());
        game.addActor(darkGoldPortal, location);
        allPortalActors.add(darkGoldPortal);
    }

    public void putDarkGrayPortal(GGBackground bg, Location location) {
        bg.setPaintColor(Color.red);
        bg.fillCircle(toPoint(location), 5);
        Actor darkGrayPortal = new Actor(PortalColour.DarkGray.getImageName());
        game.addActor(darkGrayPortal, location);
        allPortalActors.add(darkGrayPortal);
    }

    public void findPortalPairs() {
        for (Portal portal : this.portals) {
            PortalColour colour = portal.getColour();

            if (portalPairs.containsKey(colour)) {
                portalPairs.get(colour).add(portal);
            } else {
                ArrayList<Portal> tempList = new ArrayList<>();
                tempList.add(portal);
                portalPairs.put(colour, tempList);
            }
        }
    }

    public void findColourPair(Portal portal) {
        for (HashMap.Entry<PortalColour, ArrayList<Portal>> entry : portalPairs.entrySet()) {
            PortalColour colour = entry.getKey();
            ArrayList<Portal> pairs = entry.getValue();

            if (colour == portal.getColour()) {
                for (Portal tempPortal : pairs) {
                    if (!tempPortal.equals(portal)) {
                        portal.setPair(tempPortal);
                        return;
                    }
                }
            }
        }
    }

    public ArrayList<Portal> getPortals() {
        return portals;
    }

    public void removePortals() {
        for (Actor portal : allPortalActors) {
            portal.hide();
        }
    }
}
