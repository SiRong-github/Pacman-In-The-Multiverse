package src.Monster;

import ch.aplu.jgamegrid.Location;
import src.Game;
import src.Monster.Monster;
import src.Monster.MonsterType;

public class Troll extends Monster {
    private static String image = "sprites/m_troll.gif";
    private static MonsterType monsterType = MonsterType.Troll;

    public Troll(Game game) {
        super(game, monsterType, image);
    }

    // Troll walking approach: random walk.
    protected void walkApproach()
    {
        double oldDirection = getDirection();
        Location next = randomWalk(oldDirection);
        getGame().getGameCallback().monsterLocationChanged(this);
        addVisitedList(next);
    }
}
