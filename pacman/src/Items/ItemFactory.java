package src.Items;

import ch.aplu.jgamegrid.Location;
import src.Items.*;

public class ItemFactory {
    public ItemFactory() {

    }

    public Item createItem(int index, Location location) {
        Item item = switch (index) {
            case 1 -> new Gold(location);
            case 2 -> new Ice(location);
            default -> null;
        };
        return item;
    }
}
