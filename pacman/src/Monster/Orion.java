package src.Monster;

import ch.aplu.jgamegrid.Location;
import src.Game;
import src.Items.Gold;

import java.util.ArrayList;

public class Orion extends Monster {
    private Gold targetGold = null;
    private ArrayList<Gold> goldList = new ArrayList<Gold>();
    private static int listLength = 20;
    private static String image = "sprites/m_orion.gif";
    private static MonsterType monsterType = MonsterType.ORION;

    public Orion(Game game) {
        super(game, monsterType, image, listLength);
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
        // randomly pick targetGold for Orion to visit, if don't have one
        if (targetGold == null) {
            // check if all gold is visited or not, if YES start over the search again (reset orionVisitedFlag)
            int orionVisitedGoldTotal = 0;

            for (Gold gold : getGame().getGoldList()) {
                if (gold.isOrionVisited()) {
                    orionVisitedGoldTotal++;
                }
            }
            if (orionVisitedGoldTotal == getGame().getGoldList().size()) {
                for (Gold gold : getGame().getGoldList()) {
                    gold.setOrionVisited(false);
                }
            }

            // create high priority gold that Orion need to visit
            ArrayList<Gold> priorityGoldList = new ArrayList<Gold>();
            for (Gold gold : getGame().getGoldList()) {
                if (gold.isAvailable() && !gold.isOrionVisited()) {
                    priorityGoldList.add(gold);
                }
            }

            // if there is no available gold, Orion will randomly gold that already eaten instead
            if (priorityGoldList.size() == 0) {
                for (Gold gold : getGame().getGoldList()) {
                    if (!gold.isAvailable() && !gold.isOrionVisited()) {
                        priorityGoldList.add(gold);
                    }
                }
            }

            int goldVisitedIndex = getRandomiser().nextInt(priorityGoldList.size());
            targetGold = priorityGoldList.remove(goldVisitedIndex);
        }

        Location targetGoldLocation = targetGold.getInitialLocation();
        double oldDirection = getDirection();
        Location.CompassDirection goldCompassDir = getLocation().get4CompassDirectionTo(targetGoldLocation);
        Location next = getLocation().getNeighbourLocation(goldCompassDir);
        setDirection(goldCompassDir);
        if (isFuriousState() == true) {
            if (canMove(next)) {
                next = next.getNeighbourLocation(goldCompassDir);
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
                next = randomWalk(oldDirection);
            }
        }
        getGame().getGameCallback().monsterLocationChanged(this);
        addVisitedList(next);

        if (getLocation().equals(targetGoldLocation)) {
            targetGold.setOrionVisited(true);
            targetGold = null;
        }
    }
}

