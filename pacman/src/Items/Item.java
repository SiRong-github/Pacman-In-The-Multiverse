package src.Items;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.Location;

public class Item extends Actor {
    private ItemType itemType;
    private Location initialLocation;
    private boolean isAvailable = true;

    public Item(ItemType item, String image, Location initialLocation) {
        super(image);
        this.itemType = item;
        this.initialLocation = initialLocation;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public Location getInitialLocation() {
        return initialLocation;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public void hideItem() {
        this.isAvailable = false;
        this.hide();
    }
}
