package src;

import ch.aplu.jgamegrid.*;
import java.util.ArrayList;
import java.util.*;

public class Wizard extends Monster {
    private Location destination;
    private boolean isDestination = false;

    private ArrayList<Location> allNeighbourLocations = new ArrayList<Location>();

    public Wizard(Game game) {
        super(game, MonsterType.Wizard);
    }
    public void monsterWalk(double oldDirection, Location next) {
        // needs to continue in old direction (i.e. currently in the wall) = 1; if needs to be set to a new random direction = 0
        if (getIsDestination()) {
            setLocation(destination);
            setIsDestination(false);
        }
        else {
            findPossibleNeighbourLocations(this.getLocation(), allNeighbourLocations);

            for (int i = 0; i < allNeighbourLocations.size(); i++) {
                Random randomiser = new Random();
                int arrayPosition = randomiser.nextInt(allNeighbourLocations.size()); // this needs to be the length of the list
                next = allNeighbourLocations.get(arrayPosition);

                Location.CompassDirection newDirection = this.getLocation().getCompassDirectionTo(next);
                setDestination(next.getNeighbourLocation(newDirection));
                if (canMove(next)) {
                    setLocation(next);
                    setIsDestination(false);
                    break;
                }
                else if (canMove(destination)) {
                    setLocation(next);
                    setIsDestination(true);
                    break;
                }
                else {
                    allNeighbourLocations.remove(i);
                }
            }
            allNeighbourLocations.removeAll(allNeighbourLocations);
        }
    }

    private boolean getIsDestination() {
        return isDestination;
    }

    private void setIsDestination(boolean destination) {
        isDestination = destination;
    }

    private void setDestination(Location destination) {
        this.destination = destination;
    }
}