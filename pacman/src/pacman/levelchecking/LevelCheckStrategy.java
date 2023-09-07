package src.pacman.levelchecking;

import src.pacman.PacManGameGrid;

public interface LevelCheckStrategy {

    String checkLevel(PacManGameGrid grid);

}
