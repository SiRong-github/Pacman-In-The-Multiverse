package src;
import ch.aplu.jgamegrid.*;
import java.util.ArrayList;
import java.util.List;
import java.awt.*;

public class Wizard extends Monster {
    private static String image = "sprites/m_wizard.gif";
    private static MonsterType monsterType = MonsterType.WIZARD;
    private Location.CompassDirection direction;
    private ArrayList<Location> neighbours = new ArrayList<>();
    private Location next = null;
    private Location furiousNext = null;


    /* Randomiser */

    public Wizard(Game game) {
        super(game, monsterType, image);
    }

    private Location getNeighbours() {
        Location neighbourLocation = null;
        ArrayList<Location> neighbours = new ArrayList<Location>();
        neighbours = getLocation().getNeighbourLocations(1);
        neighbourLocation = getNeighbours();
        return neighbourLocation;
    }

    protected void walkApproach() {
        Location wizardLoc = getLocation();
        neighbours = wizardLoc.getNeighbourLocations(1);
        int chosen = getRandomiser().nextInt(neighbours.size());
        next = neighbours.get(chosen);
        furiousNext = getLocation().getNeighbourLocation(chosen);

        /* Check if furious state */

        /* The neighbour is not a wall */
        if (canMove(next)) {
            if (!isFuriousState()) {
                //move to the location
                setLocation(next);
            }else {
                next = randomWalk(chosen);
                setLocation(next);
            }
        }else {
            //the neighbour is a wall
            Color c = getBackground().getColor(next);

            if (c.equals(Color.gray)) { //wall
                Location.CompassDirection direction = wizardLoc.getCompassDirectionTo(next);
                /* Get direction */
                Location adjacentLocation = next.getAdjacentLocation(direction);

                if (canMove(adjacentLocation)) {
                    if (!isFuriousState()) {
                        //not wall
                        setLocation(adjacentLocation);
                    } else {
                        next = randomWalk(chosen);
                        setLocation(next);
                    }
                } else {
                    chosen = getRandomiser().nextInt(neighbours.size());
                    for (Location n : neighbours) {
                        if (!isFuriousState()) {
                            next = neighbours.get(chosen);
                            if (canMove(next)) {
                                setLocation(next);
                                break;
                            }
                        } else {
                            next = randomWalk(chosen);
                            if (canMove(next)) {
                                setLocation(next);
                                break;
                            }

                        }
                    }
                }
            }
        }
        getGame().getGameCallback().monsterLocationChanged(this);
    }
}


