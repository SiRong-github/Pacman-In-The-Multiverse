package src.Items;

import ch.aplu.jgamegrid.Location;

public class Gold extends Item {
    private boolean isOrionVisited = false;
    private static String image = "sprites/gold.png";
    private static ItemType itemType = ItemType.GOLD;

    public Gold(Location location) {
        super(location, itemType, image);
    }

    public boolean isOrionVisited() {
        return isOrionVisited;
    }

    public void setOrionVisited(boolean orionVisited) {
        isOrionVisited = orionVisited;
    }
}
