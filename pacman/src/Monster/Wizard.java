package src.Monster;
import src.Game;
import ch.aplu.jgamegrid.*;
import java.util.ArrayList;
import java.awt.*;

/**
 *Wizard class
 */

public class Wizard extends Monster {
    private static String image = "sprites/m_wizard.gif";
    private static MonsterType monsterType = MonsterType.WIZARD;
    private Location.CompassDirection direction;
    private ArrayList<Location> neighbours = new ArrayList<>();
    private Location next = null;
    private Location furiousNext = null;

    public Wizard(Game game) {super(game, monsterType, image);}

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
        furiousNext = next.getAdjacentLocation(chosen);

        /* Check if furious state */

        /* The neighbour is not a wall */
        if (canMove(next)) {
            if (!isFuriousState()) {
                //move to the location
                setLocation(next);
            }else {
                setLocation(furiousNext);
            }
        }else {
            //the neighbour is a wall
            Color c = getBackground().getColor(next);

            if (c.equals(Color.gray)) { //wall
                Location.CompassDirection direction = next.getCompassDirectionTo(wizardLoc);
                /* Get direction */
                Location adjacentLocation = next.getAdjacentLocation(direction);
                Location furiousAdjacent = adjacentLocation.getAdjacentLocation(direction);

                if (canMove(adjacentLocation)) {
                    if (!isFuriousState()) {
                        //not wall
                        setLocation(adjacentLocation);
                    } else {
                        if(canMove(furiousAdjacent)){
                            setLocation(furiousAdjacent);
                        }else{
                            chosen = getRandomiser().nextInt(neighbours.size());
                            for (Location n : neighbours) {
                                furiousNext = neighbours.get(chosen);
                                if (canMove(furiousNext)) {
                                    setLocation(furiousNext);
                                    break;
                                }
                            }
                        }
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
                            furiousNext = neighbours.get(chosen);
                            if (canMove(furiousNext)) {
                                setLocation(furiousNext);
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


