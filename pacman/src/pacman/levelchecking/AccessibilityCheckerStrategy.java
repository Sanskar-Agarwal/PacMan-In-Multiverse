package src.pacman.levelchecking;

import ch.aplu.jgamegrid.Location;
import src.pacman.PacManGameGrid;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.HashSet;
import java.util.Set;


//Strategy has been adapted from FoodFill given in ED Discussion redirecting https://www.geeksforgeeks.org/flood-fill-algorithm-implement-fill-paint/
public class AccessibilityCheckerStrategy implements LevelCheckStrategy {
    private ArrayList<String> errorMessages = new ArrayList<>();
    private boolean[][] visited;

    @Override
    public String checkLevel(PacManGameGrid grid) {
        visited = new boolean[grid.getNbVertCells()][grid.getNbHorzCells()];

        List<Location> inaccessiblePills = new ArrayList<>();
        List<Location> inaccessibleGold = new ArrayList<>();

        // Find Pacman's location
        Location pacActorLocation = grid.getPacActorLocation();

        // Perform flood fill starting from Pacman's location
        floodFill(grid, pacActorLocation.getX(), pacActorLocation.getY());

        // find Tile starting locations
        List<Location> portalLocations = findPortalLocations(grid);

        for (Location portalLocation : portalLocations) {
            floodFill(grid, portalLocation.getX(), portalLocation.getY());
        }

        // Iterate over the maze array
        for (int y = 0; y < grid.getNbVertCells(); y++) {
            for (int x = 0; x < grid.getNbHorzCells(); x++) {
                Location location = new Location(x, y);
                int a = grid.getCell(location);
                if (a == grid.PILL_TILE && !visited[y][x]) {
                    // Pill is inaccessible
                    inaccessiblePills.add(location);
                } else if (a == grid.GOLD_TILE && !visited[y][x]) {
                    // Gold is inaccessible
                    inaccessibleGold.add(location);
                }
            }
        }

        if (inaccessibleGold.size() > 0) {
            String errorMessage = "[Level " + grid.getFileName() + " - Gold not accessible: ";
            for (Location location : inaccessibleGold) {
                Location adjLoc = new Location(location.x+1, location.y+1);
                errorMessage += (adjLoc + "; ");
            }
            errorMessage = errorMessage.substring(0, errorMessage.length()-2) + "]";
            errorMessages.add(errorMessage);
        }

        if(inaccessiblePills.size() > 0) {
            String errorMessage = "[Level " + grid.getFileName() + " - Pill not accessible: ";
            for (Location location : inaccessiblePills) {
                Location adjLoc = new Location(location.x+1, location.y+1);
                errorMessage += (adjLoc + "; ");
            }
            errorMessage = errorMessage.substring(0, errorMessage.length()-2) + "]";
            errorMessages.add(errorMessage);
        }

        if (errorMessages.size() == 0) {
            return "";
        }
        else {
            return String.join("\n", errorMessages);
        }
    }

    private void floodFill(PacManGameGrid grid, int startX, int startY) {
        Queue<Location> queue = new LinkedList<>();

        // Mark the starting position as visited and enqueue it
        visited[startY][startX] = true;
        queue.add(new Location(startX, startY));

        // Define the valid neighbors for BFS
        int[] x_ne = {0, 0, -1, 1};
        int[] y_ne = {-1, 1, 0, 0};

        while (!queue.isEmpty()) {
            Location currentLocation = queue.poll();
            int currentX = currentLocation.getX();
            int currentY = currentLocation.getY();

            // Check the neighbors
            for (int i = 0; i < 4; i++) {
                int neighbourX = currentX + x_ne[i];
                int neighbourY = currentY + y_ne[i];
                if (isValidLocation(grid, neighbourX, neighbourY)) {
                    Location location = new Location(neighbourX, neighbourY);
                    int a = grid.getCell(location);

                    // Check if the neighbor is within the grid bounds and is a valid tile to visit
                    if (a != grid.WALL_TILE && !visited[neighbourY][neighbourX]) {
                        // Mark the neighbor as visited and enqueue it
                        visited[neighbourY][neighbourX] = true;
                        queue.add(location);
                    }
                }
            }
        }
    }

    private boolean isValidLocation(PacManGameGrid grid, int x, int y) {
        int gridWidth = grid.getNbHorzCells();
        int gridHeight = grid.getNbVertCells();
        return x >= 0 && x < gridWidth && y >= 0 && y < gridHeight;
    }

    private List<Location> findPortalLocations(PacManGameGrid grid) {
        List<Location> portalLocations = new ArrayList<>();

        for (int y = 0; y < grid.getNbVertCells(); y++) {
            for (int x = 0; x < grid.getNbHorzCells(); x++) {
                Location location = new Location(x, y);
                int a = grid.getCell(location);
                if (a >= grid.PORTAL_WHITE_TILE && a <= grid.PORTAL_DARK_GOLD_TILE) {
                    portalLocations.add(location);
                }
            }
        }
        return portalLocations;
    }


}