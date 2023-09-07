package src.pacman.levelchecking;

import src.pacman.PacManGameGrid;

import java.util.ArrayList;

public class LevelChecker {

    private ArrayList<LevelCheckStrategy> checkingStrategies;
    private PacManGameGrid grid;
    private ArrayList<String> errorMessages = new ArrayList<>();
    private String fileName;
    private boolean isError=false;
    LevelCheckStrategy accessibilityChecker;

    public LevelChecker(PacManGameGrid grid) {
        this.grid = grid;
        fileName = grid.getFilePath();
        checkingStrategies = new ArrayList<>();

        // Add default strategies
        checkingStrategies.add(new PacManCheckStrategy());
        checkingStrategies.add(new PortalCheckStrategy());
        checkingStrategies.add(new GoldAndPillCountCheckStrategy());
    }

    public boolean doLevelCheck() {

        for (LevelCheckStrategy checkStrategy : checkingStrategies) {
            String errorMessage = checkStrategy.checkLevel(grid);

            if (errorMessage != "") {
                errorMessages.add(errorMessage);
                isError = true;
            }
        }

        if (!isError) {
            accessibilityChecker = new AccessibilityCheckerStrategy();
            String accessibilityError = accessibilityChecker.checkLevel(grid);

            if (accessibilityError != "") {
                errorMessages.add(accessibilityError);
                isError = true;
            }
        }

        return isError;

    }

    public ArrayList<String> getErrorMessages() {
        return errorMessages;
    }

}
