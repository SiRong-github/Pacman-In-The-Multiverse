package src.Monster;

import ch.aplu.jgamegrid.*;
import src.Game;

import java.util.*;

/**
 * Alien class
 */

public class Alien extends Monster {
    private static String image = "sprites/m_alien.gif";
    private static MonsterType monsterType = MonsterType.ALIEN;
    private ArrayList<Location> neighbours = new ArrayList<>();
    private ArrayList<ArrayList<Location>> validNeighbours = new ArrayList<>();
    private ArrayList<Location> shortestNeighbours = new ArrayList<>();
    private Location next = null;

    /**
     * Initialise alien class
     * Input: game
     * Output: none
     */
    public Alien(Game game) {
        super(game, monsterType, image);
    }

    /**
     * Walk approach
     * Input: none
     * Output: none
     */
    protected void walkApproach() {
        /* Get valid neighbours */
        Location currentLocation = getLocation();
        Location pacLocation = getGame().pacActor.getLocation();
        neighbours = currentLocation.getNeighbourLocations(1);
        validNeighbours = getValidNeighbours(pacLocation);

        /* Choose next move */
        Iterator<ArrayList<Location>> iterator = validNeighbours.iterator();
        shortestNeighbours = iterator.next();
        next = getNextLocation(shortestNeighbours);
        shortestNeighbours.remove((next));

        /* Check if furious state */
        if (isFuriousState()) {
            Location nextFurious = next;
            Location secondNext = checkNextFuriousLocation(currentLocation, nextFurious);
            for (ArrayList<Location> n : validNeighbours) {
                while(!n.isEmpty() && (secondNext.x == -1 && secondNext.y == -1)) {
                    nextFurious = getNextLocation(n);
                    secondNext = checkNextFuriousLocation(currentLocation, nextFurious);
                    n.remove((nextFurious));
                }
                if (secondNext.x != -1 && secondNext.y != -1) {
                    next = secondNext;
                    setLocation(next);
                    break;
                }
            }
            /* Check if secondNext is null */
            if (secondNext.x == -1 && secondNext.y == -1) {
                setLocation(currentLocation);
            }
        } else {
            /* Set new location */
            setLocation(next);
        }

        getGame().getGameCallback().monsterLocationChanged(this);
    }

    /**
     * Get valid neighbours and their distances from pacman
     * Input: location of pacman
     * Output: hashmap containing locations and distances of valid neighbours
     */
    private ArrayList<ArrayList<Location>> getValidNeighbours(Location pacLocation) {
        Map<Location, Integer> validNeighbours = new HashMap<>();
        Set<Integer> set = new LinkedHashSet<>();
        /* Get each valid neighbour and their distances from pacman */
        for (int i = 0; i < neighbours.size(); i++) {
            Location neighbour = neighbours.get(i);
            /* Check if neighbour is valid */
            if (canMove(neighbour)) {
                int distanceFromPac = neighbour.getDistanceTo(pacLocation);
                validNeighbours.put(neighbour, distanceFromPac);
                set.add(distanceFromPac);
            }
        }
        ArrayList<ArrayList<Location>> orderedNeighbours = orderByShortest(validNeighbours, set);
        return orderedNeighbours;
    }

    /**
     * Gets neighbours with the shortest distance from pacman
     * Input: none
     * Output: arraylist of shortest neighbours
     */
    private ArrayList<ArrayList<Location>> orderByShortest(Map<Location, Integer> validNeighbours, Set<Integer> set) {
        ArrayList<ArrayList<Location>> orderedNeighbours = new ArrayList<>();
        ArrayList<Integer> sortedValues = new ArrayList<>();

        /* Order values */
        sortedValues.addAll(set);
        Collections.sort(sortedValues);

        /* Separate hash map into an arraylist based on ordered values */
        for (int i = 0; i < sortedValues.size(); i++) {
            int value = sortedValues.get(i);
            ArrayList<Location> list = new ArrayList<>();
            for (Map.Entry<Location, Integer> neighbour : validNeighbours.entrySet()) {
                if (neighbour.getValue() == value) {
                    list.add(neighbour.getKey());
                }
            }
            orderedNeighbours.add(list);
        }

        return orderedNeighbours;

    }

    /**
     * Gets next location
     * Input: none
     * Output: location
     */
    private Location getNextLocation(ArrayList<Location> shortestNeighbours) {
        /* Check if there is more than one possible location */
        if (shortestNeighbours.size() > 1) {
            /* Randomly select location from the list */
            int chosenIndex = getRandomiser().nextInt(shortestNeighbours.size());
            return shortestNeighbours.get(chosenIndex);
        } else {
            return shortestNeighbours.get(0);
        }
    }

    /**
     * Get move to two cells if possible
     * Input:
     * Output: next move if possible, (-1, -1) otherwise
     */
    private Location checkNextFuriousLocation(Location currentLocation, Location next) {
        Location.CompassDirection direction = currentLocation.getCompassDirectionTo(next);
        Location secondNext = next.getNeighbourLocation(direction);
        if (canMove(secondNext)) {
            return secondNext;
        }
        secondNext.x = secondNext.y = -1;
        return secondNext;
    }

}
