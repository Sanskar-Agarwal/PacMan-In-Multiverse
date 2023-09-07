package src;

import src.mapeditor.editor.Controller;
import src.pacman.Game;
import src.pacman.utility.GameCallback;
import src.pacman.utility.PropertiesLoader;

import java.io.File;
import java.util.Properties;

public class Driver {

    public static final String DEFAULT_PROPERTIES_PATH = "pacman/properties/test2.properties";

    /**
     * Starting point
     * @param args the command line arguments
     */

    // driver

    public static void main(String args[]) {
        String propertiesPath = DEFAULT_PROPERTIES_PATH;

        // edit mode
        if (args.length == 0) {
            new Controller();
        }

        else {

            String path = args[0];
            String extension = path.substring(path.length() - 4);
            File file = new File(path);

            if (extension.equalsIgnoreCase(".xml")) {
                new Controller(path);
            }

            else if (file.isDirectory()) {
                final Properties properties = PropertiesLoader.loadPropertiesFile(propertiesPath);
                GameCallback gameCallback = new GameCallback();
                new Game(gameCallback, properties, path);
            }

        }

    }

}