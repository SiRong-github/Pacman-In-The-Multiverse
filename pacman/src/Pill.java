package src;

import ch.aplu.jgamegrid.Location;

public class Pill extends Item {
    private static ItemType itemType = ItemType.PILL;

    public Pill(Location location) {
        super(location, itemType);
    }
}
