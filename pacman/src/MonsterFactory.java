package src;

import src.Monster.*;

public class MonsterFactory {
    private final Game game;

    public MonsterFactory(Game game) {
        this.game = game;
    }

    public Monster createMonster(int index) {
        Monster monster = switch (index) {
            case 0 -> new Troll(game);
            case 1 -> new TX5(game);
            case 2 -> new Orion(game);
            case 3 -> new Alien(game);
            case 4 -> new Wizard(game);
            default -> null;
        };
        return monster;
    }
}