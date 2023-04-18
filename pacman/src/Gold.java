package src;

import ch.aplu.jgamegrid.Location;

public class Gold extends Item {
    private boolean isAvailable = false;
    private static String image = "sprites/gold.png";
    private static ItemType itemType = ItemType.GOLD;

    public Gold(Location location) {
        super(location, itemType, image);
    }
}
