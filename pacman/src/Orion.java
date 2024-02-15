package src;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.Location;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class Orion extends Monster {
    private Location destination;
    private boolean isDestination = false;
    private ArrayList<Actor> allGoldPieces;
    private ArrayList<Actor> notEatenGold = new ArrayList<>();
    private ArrayList<Actor> eatenGold = new ArrayList<>();

    private ArrayList<Location> possibleNeighbourLocations = new ArrayList<Location>();

    public Orion(Game game) {
        super(game, MonsterType.Orion);
    }

    public void monsterWalk(double oldDirection, Location next) {
        if (getIsDestination() && this.getLocation().equals(getDestination())) {
            setIsDestination(false);
        }

        if (getIsDestination()) {
            findPossibleNeighbourLocations(this.getLocation(), possibleNeighbourLocations);
            findNext(next, getDestination(), possibleNeighbourLocations);
            possibleNeighbourLocations.removeAll(possibleNeighbourLocations);
        }
        else {
            allGoldPieces = getGame().getItemHandler().getGoldPieces();

            if (notEatenGold.size() == 0 && eatenGold.size() == 0) {

                for (Actor gold : allGoldPieces) {
                    if (gold.isVisible()) {
                        notEatenGold.add(gold);
                    }
                    else {
                        eatenGold.add(gold);
                    }
                }
            }

            int notEatenArray = 1; // 1 when using the notEatenGold array
            if (notEatenGold.size() == 0) {
                notEatenArray = 0;
            }

            int arrayPosition;
            if (notEatenArray == 1) {
                arrayPosition = findRandomDestination(notEatenGold, next, this.getDestination());
                notEatenGold.remove(arrayPosition);
            }
            else {
                arrayPosition = findRandomDestination(eatenGold, next, this.getDestination());
                eatenGold.remove(arrayPosition);
            }
            possibleNeighbourLocations.removeAll(possibleNeighbourLocations);

        }
    }
    @Override
    public boolean isPossible(Location next) {
        if (canMove(next) && !isVisited(next)) {
            return true;
        }
        return false;
    }

    private int findRandomDestination(ArrayList<Actor> goldArray, Location next, Location destination) {
        Random randomiser = new Random();
        int arrayPosition = randomiser.nextInt(goldArray.size());

        setDestination(goldArray.get(arrayPosition).getLocation());
        setIsDestination(true);
        findPossibleNeighbourLocations(this.getLocation(), possibleNeighbourLocations);
        findNext(next, destination, possibleNeighbourLocations);
        return arrayPosition;
    }

    private Location getDestination() {
        return destination;
    }
    private void setDestination(Location destination) {
        this.destination = destination;
    }


    private boolean getIsDestination() {
        return isDestination;
    }

    private void setIsDestination(boolean destination) {
        isDestination = destination;
    }
}