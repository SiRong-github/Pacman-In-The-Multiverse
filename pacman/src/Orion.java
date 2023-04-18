package src;

import ch.aplu.jgamegrid.Location;

public class Orion extends Monster{
    private static String image = "sprites/m_orion.gif";
    private static MonsterType monsterType = MonsterType.ORION;

    public Orion(Game game) {
        super(game, monsterType, image);
    }

    /* Orion walking approach:
     * Gold Surveillance: Instead of chasing PacMan, Orion wants to protect gold pieces.
     * Orion will walk through every gold location by selecting one gold piece at random, walking to it, and so on.
     * When all gold pieces have been visited, Orion will start over again by randomly selecting any gold locations.
     * Even though the gold pieces are eaten by PacMan, Orion still will visit those locations. However, Orion will favour
     * locations which still have gold pieces, that is, it will select from locations with gold pieces before other
     * locations where the gold pieces have been eaten by PacMan. Once a gold location has been selected, Orion will visit it,
     * even if the gold piece in that location is eaten while Orion is on the way there.
     */
    protected void walkApproach()
    {
        Location pacLocation = getGame().pacActor.getLocation();
        double oldDirection = getDirection();
        Location.CompassDirection compassDir = getLocation().get4CompassDirectionTo(pacLocation);
        Location next = getLocation().getNeighbourLocation(compassDir);
        setDirection(compassDir);
        if (isFuriousState() == true) {
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
