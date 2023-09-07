package src.pacman.levelchecking;

import ch.aplu.jgamegrid.Location;
import src.pacman.PacManGameGrid;

import java.util.ArrayList;
import java.util.HashMap;

public class PortalCheckStrategy implements LevelCheckStrategy {

    private PacManGameGrid grid;
    private ArrayList<String> errorMessages = new ArrayList<>();
    private String errorMessage = "";
    private HashMap<String, ArrayList<Location>> portalMap;


    @Override
    public String checkLevel(PacManGameGrid grid) {
        this.grid = grid;
        setUpPortalMap();
        checkPortalCounts();

        if (errorMessages.size()==0) {
            return "";
        }
        else {
            return String.join("\n", errorMessages);
        }
    }

    private void checkPortalCounts() {

        for (String portalColour : portalMap.keySet()) {
            ArrayList<Location> portalLocations = portalMap.get(portalColour);
            if (portalLocations.size() != 2) {
                String errorMessage =  "[Level " + grid.getFileName() + " - portal " + portalColour + " count is not 2: ";
                for (Location loc : portalLocations) {
                    Location adjLoc = new Location(loc.x+1, loc.y+1);
                    errorMessage += (adjLoc + "; ");
                }
                errorMessage = errorMessage.substring(0,errorMessage.length()-2) + "]";
                errorMessages.add(errorMessage);
            }
        }
    }

    private void setUpPortalMap() {

        portalMap = new HashMap<>();

        for (int y = 0; y < grid.getNbVertCells(); y++) {
            for (int x = 0; x < grid.getNbHorzCells(); x++) {

                Location location = new Location(x, y);
                int a = grid.getCell(location);

                if (a == grid.PORTAL_WHITE_TILE) {
                    addToPortalMap("White", location);
                }
                else if (a == grid.PORTAL_YELLOW_TILE) {
                    addToPortalMap("Yellow", location);
                }
                else if (a == grid.PORTAL_DARK_GRAY_TILE) {
                    addToPortalMap("Dark Gray", location);
                }
                else if (a == grid.PORTAL_DARK_GOLD_TILE) {
                    addToPortalMap("Dark Gold", location);
                }
            }
        }
    }

    private void addToPortalMap(String portalColour, Location location) {
        if (portalMap.containsKey(portalColour)) {
            portalMap.get(portalColour).add(location);
        } else {
            ArrayList<Location> portalLocs = new ArrayList<>();
            portalLocs.add(location);
            portalMap.put(portalColour, portalLocs);
        }
    }
}

