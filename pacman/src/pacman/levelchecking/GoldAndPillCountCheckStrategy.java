package src.pacman.levelchecking;

import ch.aplu.jgamegrid.Location;
import src.pacman.PacManGameGrid;

public class GoldAndPillCountCheckStrategy implements LevelCheckStrategy {

    private PacManGameGrid grid;
    private int numGoldAndPills;
    private String errorMessage="";
    @Override
    public String checkLevel(PacManGameGrid grid) {

        this.grid = grid;
        numGoldAndPills = countGoldAndPills();

        if (numGoldAndPills < 2) {
            errorMessage = "[Level " + grid.getFileName() + " - less than 2 Gold and Pill]";
        }

        return errorMessage;
    }

    private int countGoldAndPills() {

        int count = 0;
        for (int y = 0; y < grid.getNbVertCells(); y++) {
            for (int x = 0; x < grid.getNbHorzCells(); x++) {

                Location location = new Location(x, y);
                int a = grid.getCell(location);

                if (a == grid.PILL_TILE || a == grid.GOLD_TILE) {
                    count++;
                }
            }
        }
        return count;
    }

}
