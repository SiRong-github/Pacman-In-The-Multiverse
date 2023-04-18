package src;

import ch.aplu.jgamegrid.Location;

public class TX5 extends Monster{

    private static String image = "sprites/m_tx5.gif";
    private static MonsterType monsterType = MonsterType.TX5;

    public TX5(Game game) {
        super(game, monsterType, image);
    }

    // TX-5 walking approach: Determine direction to pacActor and try to move in that direction. Otherwise, random walk.
    protected void walkApproach()
    {
//        Location pacLocation = getGame().pacActor.getLocation();
//        double oldDirection = getDirection();
//        Location.CompassDirection compassDir = getLocation().get4CompassDirectionTo(pacLocation);
//        Location next = getLocation().getNeighbourLocation(compassDir);
//        if (isFuriousState() == true) {
//            next = next.getNeighbourLocation(compassDir);
//        }
//        setDirection(compassDir);
//        if (!isVisited(next) && canMove(next))
//        {
//            setLocation(next);
//        }
//        else
//        {
//            next = randomWalk(oldDirection);
//        }
//        getGame().getGameCallback().monsterLocationChanged(this);
//        addVisitedList(next);
//        for (Location loc : getVisitedList()) {
//            System.out.println("TX5 visitedList:" + loc);
//        }
//        System.out.println("---------------------------");


        // implement furious movement of TX5
        Location pacLocation = getGame().pacActor.getLocation();
        double oldDirection = getDirection();
        Location.CompassDirection compassDir = getLocation().get4CompassDirectionTo(pacLocation);
        Location next = getLocation().getNeighbourLocation(compassDir);
        setDirection(compassDir);
        if (isFuriousState() == true && getGame().getCurrentGameVersion().equals(getGame().getGameVersion()[1])) {
            if (canMove(next)) {
                next = next.getNeighbourLocation(compassDir);
                if (!isVisited(next) && canMove(next)) {
                    setLocation(next);
                } else {
                    next = randomWalk(oldDirection);
                }
            } else {
                next = randomWalk(oldDirection);
            }
        } else {
            if (!isVisited(next) && canMove(next)) {
                setLocation(next);
            } else {
                System.out.println("TX5: entered randomWalk");
                next = randomWalk(oldDirection);
            }
        }
        getGame().getGameCallback().monsterLocationChanged(this);
        addVisitedList(next);
    }
}
