package src;

import ch.aplu.jgamegrid.*;

import java.awt.*;
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
        Location currentLocation = getLocation();
        Location pacLocation = getGame().pacActor.getLocation();

        System.out.println("curr "+ currentLocation);
        System.out.println("pac "+ pacLocation);
        System.out.println();

        neighbours = currentLocation.getNeighbourLocations(1);
        for (Location n: neighbours) {
            System.out.println("neighbour "+n);
        }
        System.out.println();


        validNeighbours = getValidNeighbours(pacLocation);

        for (ArrayList<Location> n : validNeighbours) {
            System.out.println("valid neighbours "+n);
        }
        System.out.println();

        /* Choose next move */
        Iterator<ArrayList<Location>> iterator = validNeighbours.iterator();
        shortestNeighbours = iterator.next();
        next = getNextLocation(shortestNeighbours);
        shortestNeighbours.remove((next));
        System.out.println("orig next "+next);

        /* Check if furious state */
        if (isFuriousState()) {
            System.out.println("furious");
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
                    break;
                }
            }
            System.out.println("secondNext "+secondNext);
        } else {
            System.out.println("calm");
        }

        System.out.println("next "+next);

        setLocation(next);
        getGame().getGameCallback().monsterLocationChanged(this);
        System.out.println();
    }

    /**
     * Get valid neighbours and their distances from pacman
     * Input: location of pacman
     * Output: hashmap containing locations and distances of valid neighbours
     */
    protected ArrayList<ArrayList<Location>> getValidNeighbours(Location pacLocation) {
        Map<Location, Integer> validNeighbours = new HashMap<>();
        ArrayList<ArrayList<Location>> orderedNeighbours = new ArrayList<>();
        Set<Integer> set = new LinkedHashSet<>();
        /* Get each valid neighbour and their distances from pacman */
        for (int i = 0; i < neighbours.size(); i++) {
            Location neighbour = neighbours.get(i);
            /* Check if neighbour is valid */
            if (canMove(neighbour)) {
                System.out.println("valid neighbour " + neighbour);
                int distanceFromPac = neighbour.getDistanceTo(pacLocation);
                validNeighbours.put(neighbour, distanceFromPac);
                set.add(distanceFromPac);
            } else {
                System.out.println("invalid " + neighbour);
            }
        }

        orderedNeighbours = orderByShortest(validNeighbours, set);


        System.out.println("ArrayList with duplicates removed: "
                + orderedNeighbours);

        return orderedNeighbours;
    }

    /**
     * Gets neighbours with the shortest distance from pacman
     * Input: none
     * Output: arraylist of shortest neighbours
     */
    protected ArrayList<ArrayList<Location>> orderByShortest(Map<Location, Integer> validNeighbours, Set<Integer> set) {
        ArrayList<ArrayList<Location>> orderedNeighbours = new ArrayList<>();
        ArrayList<Integer> sortedValues = new ArrayList<>();

        /* Order values */
        sortedValues.addAll(set);
        Collections.sort(sortedValues);

        /* Separate hash map into an arraylist based on ordered values */
        for (int i = 0; i < sortedValues.size(); i++) {
            int value = sortedValues.get(i);
            System.out.println("value "+value);
            ArrayList<Location> list = new ArrayList<>();
            for (Map.Entry<Location, Integer> neighbour : validNeighbours.entrySet()) {
                if (neighbour.getValue() == value) {
                    list.add(neighbour.getKey());
                    System.out.println("key "+neighbour.getKey());
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
    protected Location getNextLocation(ArrayList<Location> shortestNeighbours) {
        /* Check if there is more than one possible location */

        if (shortestNeighbours.size() > 1) {
            System.out.println("random");
            /* Randomly select location from the list */
            Random random = new Random();
            int chosenIndex = random.nextInt(shortestNeighbours.size());
            System.out.println("chosen index" + chosenIndex + "\n");
            return shortestNeighbours.get(chosenIndex);
        } else {
            System.out.println("not random");
            return shortestNeighbours.get(0);
        }
    }

    /**
     * Get move to two cells if possible
     * Input:
     * Output: next move if possible, (-1, -1) otherwise
     */
    protected Location checkNextFuriousLocation(Location currentLocation, Location next) {
        Location.CompassDirection direction = currentLocation.getCompassDirectionTo(next);
        Location secondNext = next.getNeighbourLocation(direction);
        if (canMove(secondNext)) {
            return secondNext;
        }
        secondNext.x = secondNext.y = -1;
        return secondNext;
    }

}
