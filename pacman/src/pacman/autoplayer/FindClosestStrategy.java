package src.pacman.autoplayer;

import ch.aplu.jgamegrid.Location;
import src.pacman.PacActor;

import java.util.ArrayList;
import java.util.Arrays;

public class FindClosestStrategy implements AutoplayerStrategy {
    @Override
    public void playAutoplayer(PacActor pacActor) {
        ArrayList<Location> targetLocations = new ArrayList<>();
        if (pacActor.getDestination() == null || (pacActor.getDestination()!=null && pacActor.getLocation().equals(pacActor.getDestination()))) {

            for (Location location : pacActor.getItemHandler().getAllPillandGoldLocations()) {
                ArrayList<Integer> tileTypes = new ArrayList<>(Arrays.asList(pacActor.getGame().getGrid().PILL_TILE, pacActor.getGame().getGrid().GOLD_TILE));
                ArrayList<Location> needPortalToAccess = pacActor.needPortal(tileTypes, true);
                if (pacActor.isAccessible(location, needPortalToAccess)) {
                    targetLocations.add(location);
                }
            }

            Location closestItemOrGold = pacActor.findClosest(targetLocations, pacActor.getLocation());
            pacActor.setDestination(closestItemOrGold);
        }

        Location next = pacActor.findNextWithoutPortal();
        if (next != null) {
            pacActor.setLocation(next);
            pacActor.eatPill(next);
            pacActor.addVisitedList(next);
        }
    }


}
