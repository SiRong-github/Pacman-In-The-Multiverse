package src;

import ch.aplu.jgamegrid.Location;

public class Troll extends Monster{
    public Troll(Game game) {
        super(game, MonsterType.Troll);
    }

    protected void walkApproach() {
        Location next = null;
        randomWalk(next);
    }
}
