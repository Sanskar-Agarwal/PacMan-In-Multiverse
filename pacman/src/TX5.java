
package src;
import ch.aplu.jgamegrid.Location;
public class TX5 extends Monster {

    public TX5(Game game) {
        super(game, MonsterType.TX5);
    }

    public void monsterWalk(double oldDirection, Location next) {
        if (!isVisited(next) && canMove(next)) {
            setLocation(next);
        } else {
            randomWalk(oldDirection, next);
        }

    }
}