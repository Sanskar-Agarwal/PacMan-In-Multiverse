package src;

import src.utility.GameCallback;
import src.utility.PropertiesLoader;

import java.util.Properties;

public class Driver {
<<<<<<< HEAD
    //public static final String DEFAULT_PROPERTIES_PATH = "/Users/larastebbens/Library/Mobile Documents/com~apple~CloudDocs/unimelb/2023 Semester 1/SWEN/Project 1/CODEP1/2023-sem1-projectassignment1-wed16-15-team-04/pacman/properties/test5.properties";
    public static final String DEFAULT_PROPERTIES_PATH = "/Users/elise/Documents/UniMelb/SWEN30006/2023-sem1-projectassignment1-wed16-15-team-04/pacman/properties/test4.properties";
=======
    public static final String DEFAULT_PROPERTIES_PATH = "pacman/properties/test1.properties";
    //public static final String DEFAULT_PROPERTIES_PATH = "/Users/elise/Documents/UniMelb/SWEN30006/2023-sem1-projectassignment1-wed16-15-team-04/pacman/properties/test5.properties";
>>>>>>> abae38391bb464f2ec6c9647de3a2349e3916b5a
    /**
     * Starting point
     * @param args the command line arguments
     */

    public static void main(String args[]) {
        String propertiesPath = DEFAULT_PROPERTIES_PATH;
        if (args.length > 0) {
            propertiesPath = args[0];
            //hello
        }
        final Properties properties = PropertiesLoader.loadPropertiesFile(propertiesPath);
        GameCallback gameCallback = new GameCallback();
        new Game(gameCallback, properties);
    }
}