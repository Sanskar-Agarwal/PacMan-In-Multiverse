package src.pacman.monsters;

import ch.aplu.jgamegrid.*;
import src.pacman.Game;
import src.pacman.PacManGameGrid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MonsterFacade{

    private ArrayList<Monster> monsters;
    private Map<String, Actor> actorMap;
    private Map<Monster,Location> monsterLocations;

    public MonsterFacade() {
        monsters = new ArrayList<Monster>();
        actorMap = new HashMap<String, Actor>();
        monsterLocations = new HashMap<Monster, Location>();
    }

    public void setUpMonsters(Game game) {

        PacManGameGrid grid = game.getGrid();

        for (int y = 0; y < grid.getNbVertCells(); y++) {
            for (int x = 0; x < grid.getNbHorzCells(); x++) {
                Location location = new Location(x, y);
                int a = grid.getCell(location);
                if (a == grid.TX5_TILE) {
                    Monster monster = new TX5(game);
                    monsters.add(monster);
                    actorMap.put("TX5", monster);
                    monsterLocations.put(monster, location);

                } else if (a == grid.TROLL_TILE) {
                    Monster monster = new Troll(game);
                    monsters.add(monster);
                    actorMap.put("Troll", monster);
                    monsterLocations.put(monster, location);
                }
            }
        }

    }

    public void delayTx5(int seconds) {
        for (Monster monster : monsters) {
            if (monster.getType().equals(MonsterType.TX5)) {
                monster.setStopMoving(seconds);
            }
        }
    }

    public void setMonsterSeeds(int seed) {
        for (Monster monster : monsters) {
            monster.setSeed(seed);
        }
    }

    public void setMonstersSlowDown(int factor) {
        for (Monster monster : monsters) {
            monster.setSlowDown(factor);
        }
    }

    public Map<Monster, Location> getMonsterLocations() {
        return monsterLocations;
    }


    public ArrayList<Monster> getMonsters() {
        return monsters;
    }

}
