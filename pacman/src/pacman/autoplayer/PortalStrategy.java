package src.pacman.autoplayer;

import src.pacman.PacActor;
import src.pacman.autoplayer.AutoplayerStrategy;
import src.pacman.portals.Portal;

import java.util.ArrayList;
import java.util.Arrays;
import ch.aplu.jgamegrid.*;

public class PortalStrategy implements AutoplayerStrategy {
    private ArrayList<Location> inaccessiblePortalsPairs = new ArrayList<>();
    @Override
    public void playAutoplayer(PacActor pacActor) {
        ArrayList<Integer> portalTypes = new ArrayList<>(Arrays.asList(pacActor.getGame().getGrid().PORTAL_WHITE_TILE,
                pacActor.getGame().getGrid().PORTAL_YELLOW_TILE, pacActor.getGame().getGrid().PORTAL_DARK_GRAY_TILE, pacActor.getGame().getGrid().PORTAL_DARK_GOLD_TILE));

        ArrayList<Location> inaccessiblePortals = pacActor.needPortal(portalTypes, false);
        for (Portal portal : pacActor.getGame().getPortalFacade().getPortals()) {
            for (Location location : inaccessiblePortals) {
                if (portal.getLocation().equals(location)) {
                    inaccessiblePortalsPairs.add(portal.getPair().getLocation());
                }
            }
        }

        if (pacActor.getDestination() == null || (pacActor.getDestination() != null && pacActor.getLocation().equals(pacActor.getDestination()))) {
            inaccessiblePortalsPairs.remove(pacActor.getDestination());
            Location closestPortal = pacActor.findClosest(inaccessiblePortalsPairs, pacActor.getLocation());
            pacActor.setDestination(closestPortal);
        }

        Location next = pacActor.findNext();
        if (next != null) {
            pacActor.setLocation(next);
            pacActor.eatPill(next);
            pacActor.addVisitedList(next);
        }
    }
}
