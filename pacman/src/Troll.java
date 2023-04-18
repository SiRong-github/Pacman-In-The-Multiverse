package src;

import ch.aplu.jgamegrid.Location;

public class Troll extends Monster{
    private static String image = "sprites/m_troll.gif";
    private static MonsterType monsterType = MonsterType.Troll;

    public Troll(Game game) {
        super(game, monsterType, image);
    }

    // TX-5 walking approach: Determine direction to pacActor and try to move in that direction. Otherwise, random walk.
    protected void walkApproach()
    {
        double oldDirection = getDirection();
        Location next = randomWalk(oldDirection);
        getGame().getGameCallback().monsterLocationChanged(this);
        addVisitedList(next);
    }
}
