package src.Items;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.Location;

public class Item extends Actor {
    private ItemType itemType;
    private boolean isAvailable = true;

    public Item(ItemType item) {
        this.itemType = item;
    }

    public Item(ItemType item, String image) {
        super(image);
        this.itemType = item;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}
