package src.Items;

import ch.aplu.jgamegrid.Location;
import src.Items.Item;
import src.Items.ItemType;

public class Pill extends Item {
    private static ItemType itemType = ItemType.PILL;

    public Pill(Location location) {
        super(location, itemType);
    }
}
