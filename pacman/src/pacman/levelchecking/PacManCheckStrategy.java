package src.pacman.levelchecking;

import ch.aplu.jgamegrid.Location;
import src.pacman.PacManGameGrid;

import java.util.ArrayList;

public class PacManCheckStrategy implements LevelCheckStrategy {

    private PacManGameGrid grid;
    private int numPacman;
    private String errorMessage="";
    private ArrayList<Location> pacActorLocations = new ArrayList<>();

    @Override
    public String checkLevel(PacManGameGrid grid) {
        this.grid = grid;
        numPacman = grid.getNumPacActors();

        if (numPacman < 1) {
            errorMessage = "[Level " + grid.getFileName() + " - no start for Pacman]";
        }

        else if (numPacman > 1) {
            findAllPacmanLocations();
            errorMessage = "[Level " + grid.getFileName() + " - more than one start for Pacman: ";
            for (int i = 0; i < numPacman; i++) {
                Location adjLoc = new Location(pacActorLocations.get(i).x+1, pacActorLocations.get(i).y+1);
                errorMessage += adjLoc + "; ";
            }
            errorMessage = errorMessage.substring(0, errorMessage.length()-2) + "]";
        }
        return errorMessage;
    }

    private ArrayList<Location> findAllPacmanLocations() {
        for (int y = 0; y < grid.getNbVertCells(); y++) {
            for (int x = 0; x < grid.getNbHorzCells(); x++) {
                Location location = new Location(x, y);
                int a = grid.getCell(location);
                if (a == grid.PAC_TILE) {
                    pacActorLocations.add(location);
                }
            }
        }
        return pacActorLocations;
    }
}
