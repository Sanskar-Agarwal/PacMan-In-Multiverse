package src.pacman.utility;

import ch.aplu.jgamegrid.Location;
import src.pacman.monsters.Monster;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Please do not change this class. This class is used for testing and your code needs to print the correct output to pass the test
 */
public class GameCallback {
    private String logFilePath = "pacman/Log.txt";
    private String errorLogFilePath = "pacman/ErrorLog.txt";
    private FileWriter fileWriter = null;
    private FileWriter errorFileWriter = null;

    public GameCallback() {
        try {
            fileWriter = new FileWriter(new File(logFilePath));
            errorFileWriter = new FileWriter(new File(errorLogFilePath));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void writeString(String str) {
        try {
            fileWriter.write(str);
            fileWriter.write("\n");
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeError(String str) {
        try {
            errorFileWriter.write(str);
            errorFileWriter.write("\n");
            errorFileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void logErrorMessages(ArrayList<String> errorMessages) {
        for (String message : errorMessages) {
            writeError(message);
        }
    }

    public void endOfGame(String gameResult) {
        writeString(gameResult);
    }

    public void pacManLocationChanged(Location pacmanLocation, int score, int nbPills) {
        String pacmanLocationString = String.format("[PacMan] Location: %d-%d. Score: %d. Pills: %d", pacmanLocation.getX(),
                pacmanLocation.getY(), score, nbPills);
        writeString(pacmanLocationString);
    }

    public void monsterLocationChanged(Monster monster) {
        String monsterLocationString = String.format("[%s] Location: %d-%d", monster.getType(),
                monster.getLocation().getX(), monster.getLocation().getY());
        writeString(monsterLocationString);
    }

    public void pacManEatPillsAndItems(Location pacmanLocation, String type) {
        String pillOrItemLocationString = String.format("[PacMan] Location: %d-%d. Eat Pill/Item: %s", pacmanLocation.getY(),
                pacmanLocation.getY(), type);
        writeString(pillOrItemLocationString);
    }
}
