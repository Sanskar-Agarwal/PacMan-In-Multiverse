package src.pacman.portals;
import ch.aplu.jgamegrid.*;

public class Portal {
    private PortalColour colour;
    private Location location;
    private Portal pair;

    public Portal(PortalColour colour, Location location) {
        this.colour = colour;
        this.location = location;
    }

    public Portal getPair() {
        return pair;
    }

    public void setPair(Portal pair) {
        this.pair = pair;
    }

    public Location getLocation() {
        return location;
    }

    public PortalColour getColour() {
        return colour;
    }


}
