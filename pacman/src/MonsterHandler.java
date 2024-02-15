package src;

import ch.aplu.jgamegrid.Actor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MonsterHandler {

    private ArrayList<Monster> monsters;
    private Map<String, Actor> actorMap;

    private Game game;
    private Monster troll;
    private Monster tx5;
    private Monster wizard;
    private Monster alien;
    private Monster orion;

    public MonsterHandler(Game game) {
        this.game = game;
        monsters = new ArrayList<Monster>();
        actorMap = new HashMap<String, Actor>();
    }

    public void setUpMonsters(Game game) {
        troll = new Troll(game);
        monsters.add(troll);
        actorMap.put("Troll", troll);
        tx5 = new TX5(game);
        monsters.add(tx5);
        actorMap.put("TX5", tx5);
        wizard = new Wizard(game);
        monsters.add(wizard);
        actorMap.put("Wizard", wizard);
        alien = new Alien(game);
        monsters.add(alien);
        actorMap.put("Alien", alien);
        orion = new Orion(game);
        monsters.add(orion);
        actorMap.put("Orion", orion);
    }

    public void delayTx5(int seconds) {
        tx5.setStopMoving(seconds);
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

    public void freezeMonsters(int seconds) {
        if (game.getIsMultiverse()) {
            for (Monster monster : monsters) {
                monster.setStopMoving(seconds);
            }
        }
    }

    public void furiousMode(int seconds) {
        if (game.getIsMultiverse()) {
            for (Monster monster : monsters) {
                monster.setFuriousMode(seconds);
            }
        }
    }

    public ArrayList<Monster> getMonsters() {
        return monsters;
    }

    public Map<String, Actor> getActorMap() {
        return actorMap;
    }
}
