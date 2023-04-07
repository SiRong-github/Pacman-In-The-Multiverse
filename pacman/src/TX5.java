package src;

import ch.aplu.jgamegrid.Location;

public class TX5 extends Monster{
    public TX5(Game game) {
        super(game, MonsterType.TX5);
    }

    public void walkApproach() {
        Location pacLocation = getGame().pacActor.getLocation();
        double oldDirection = getDirection();

        // Walking approach:
        // TX5: Determine direction to pacActor and try to move in that direction. Otherwise, random walk.
        // Troll: Random walk.
        Location.CompassDirection compassDir =
                getLocation().get4CompassDirectionTo(pacLocation);
        Location next = getLocation().getNeighbourLocation(compassDir);
        setDirection(compassDir);
        if (getType() == MonsterType.TX5 &&
                !isVisited(next) && canMove(next))
        {
            setLocation(next);
            getGame().getGameCallback().monsterLocationChanged(this);
            addVisitedList(next);
        }
        else
        {
            randomWalk(next);
        }
    }
}
