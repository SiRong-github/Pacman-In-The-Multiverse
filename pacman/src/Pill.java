package src;

import ch.aplu.jgamegrid.Location;
import src.Items.Item;
import src.Items.ItemType;

public class Pill {
    private Location location;

    public Pill(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }
}
