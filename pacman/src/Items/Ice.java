package src.Items;

import ch.aplu.jgamegrid.Location;

public class Ice extends Item {
    private static String image = "sprites/ice.png";
    private static ItemType itemType = ItemType.ICE;

    public Ice(Location location) {
        super(itemType, image, location);
    }
}
