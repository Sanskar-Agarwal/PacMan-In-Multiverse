package src;

import ch.aplu.jgamegrid.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Alien extends Monster {

    private ArrayList<Location> possibleNeighbourLocations = new ArrayList<Location>();


    public Alien(Game game) {
        super(game, MonsterType.Alien);
    }

    public void monsterWalk(double oldDirection, Location next) {

        findPossibleNeighbourLocations(this.getLocation(), possibleNeighbourLocations);
        findNext(next, getGame().getPacActor().getLocation(), possibleNeighbourLocations);
        possibleNeighbourLocations.removeAll(possibleNeighbourLocations);
    }

    @Override
    public boolean isPossible(Location next) {
        if (canMove(next)) {
            return true;
        }
        return false;
    }
}
