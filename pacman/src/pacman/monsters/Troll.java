package src.pacman.monsters;

import ch.aplu.jgamegrid.*;
import src.pacman.Game;

public class Troll extends Monster {

    public Troll(Game game) {
        super(game, MonsterType.Troll);
    }

    public void monsterWalk(double oldDirection, Location next) {
        randomWalk(oldDirection, next);
    }
}