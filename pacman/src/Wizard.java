package src;
import ch.aplu.jgamegrid.*;
import java.util.ArrayList;
import java.util.List;
import java.awt.*;

public class Wizard extends Monster {
    private static String image = "sprites/m_wizard.gif";
    private static MonsterType monsterType = MonsterType.WIZARD;
    private final double direction = 0;
    private ArrayList<Location> neighbours = new ArrayList<>();
    private Location next = null;
    private Game game;

    Location wizardLoc = getLocation();
    /* Randomiser */
    int chosen = getRandomiser().nextInt() < 0 ? 0 : 1;
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
        for (Location n: neighbours) {
            next = n.getNeighbourLocation(chosen);
        }

        /* The neighbour is not a wall */
        if(canMove(next)){
            //move to the location
            setLocation(next);
        //the neighbour is a wall
        }else{
            Color c = getBackground().getColor(next);
            if (c.equals(Color.gray)) { //wall
                /* Check if not outside of grid */
                if (next.getX() < game.getNumHorzCells() && next.getY() < game.getNumVertCells()) {
                    /* Get direction */
                    Location adjacentLocation = next.getAdjacentLocation(direction);
                    if (canMove(adjacentLocation)) {
                        //not wall
                        setLocation(adjacentLocation);
                    } else {
                        for (Location n: neighbours) {
                            next = n.getNeighbourLocation(chosen);
                        }
                    }

                }
            }
        }
    }
}


