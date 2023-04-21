package src.Monster;
import src.Game;
import ch.aplu.jgamegrid.*;
import java.util.ArrayList;

import java.awt.*;

public class Wizard extends Monster {
    private static String image = "sprites/m_wizard.gif";
    private static MonsterType monsterType = MonsterType.WIZARD;
    private Location.CompassDirection direction;
    private ArrayList<Location> neighbours = new ArrayList<>();
    private Location next = null;
    private Location furiousNext = null;

    public Wizard(Game game) {
        super(game, monsterType, image);
    }

    protected void walkApproach() {
        Location wizardLoc = getLocation();
        neighbours = wizardLoc.getNeighbourLocations(1);
        int chosen = getRandomiser().nextInt(neighbours.size());
        next = neighbours.get(chosen);
        direction = wizardLoc.getCompassDirectionTo(next);

        /* Check if furious state */
        if (!isFuriousState()) {
            if (canMove(next)) {
                setLocation(next);
            } else {
                /* The neighbour is not a wall */
                Color c = getBackground().getColor(next);
                if (c.equals(Color.gray)) { //wall
                    Location adjacentLocation = getAdjacentLocation(wizardLoc, next);
                    if (canMove(adjacentLocation)) {
                        setLocation(adjacentLocation);
                    } else {
                        Location nextNeighbour = getAnotherNeighbour(wizardLoc);
                        setLocation(nextNeighbour);
                    }
                }
            }
        } else {
            furiousNext = next.getAdjacentLocation(direction, 1);
            if (canMove(furiousNext)) {
                setLocation(furiousNext);
            } else {
                Color c = getBackground().getColor(furiousNext);
                if (c.equals(Color.gray)) { //wall
                    Location adjacentLocation = getAdjacentLocation(wizardLoc, furiousNext);
                    if (canMove(adjacentLocation)) {
                        setLocation(adjacentLocation);
                    } else {
                        Location nextNeighbour = getAnotherNeighbour(wizardLoc);
                        setLocation(nextNeighbour);
                    }
                }
            }
        }

        getGame().getGameCallback().monsterLocationChanged(this);
    }


    private Location getAdjacentLocation(Location initialLocation, Location nextLocation) {
        /* Get direction */
        Location.CompassDirection direction = initialLocation.getCompassDirectionTo(nextLocation);
        Location adjacentLocation = next.getAdjacentLocation(direction, 1);
        return adjacentLocation;

    }

    private Location getAnotherNeighbour(Location location) {
        neighbours = location.getNeighbourLocations(1);
        Location next = null;
        for (Location n : neighbours) {
            int chosen = getRandomiser().nextInt(neighbours.size());
            next = neighbours.get(chosen);
            if (canMove(next)) {
                return next;
            }
        }
        return next;
    }

}





