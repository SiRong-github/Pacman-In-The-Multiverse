package src;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.Location;

public class Item extends Actor {
    private Location location;
    private ItemType itemType;

    public Item(Location location, ItemType item) {
        this.location = location;
        this.itemType = item;
    }

    public Item(Location location, ItemType item, String image) {
        super(image);
        this.location = location;
        this.itemType = item;
    }

    public ItemType getItemType() {
        return itemType;
    }

    @Override
    public Location getLocation() {
        return location;
    }
}
