package fun.lewisdev.deluxehub.module.modules.hologram;

import fun.lewisdev.deluxehub.utility.TextUtil;
import fun.lewisdev.deluxehub.utility.reflection.ArmorStandName;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class Hologram {

    private List<ArmorStand> stands;
    private Location location;
    private String name;

    public Hologram(String name, Location location) {
        this.name = name;
        this.location = location;
        stands = new ArrayList<>();
    }

    public Hologram setLines(List<String> lines) {
        remove();
        for (String s : lines) addLine(s);
        return this;
    }

    public Hologram addLines(List<String> lines) {
        for (String s : lines) addLine(s);
        return this;
    }

    public Hologram addLine(String text) {
        ArmorStand stand = (ArmorStand) location.getWorld().spawnEntity(location.clone().subtract(0, getHeight(), 0), EntityType.ARMOR_STAND);
        stand.setVisible(false);
        stand.setGravity(false);
        stand.setCustomNameVisible(true);
        stand.setCustomName(TextUtil.color(text).trim());
        stand.setCanPickupItems(false);
        stands.add(stand);
        return this;
    }

    public Hologram setLine(int line, String text) {
        ArmorStand stand = stands.get(line - 1);
        stand.setCustomName(TextUtil.color(text).trim());
        return this;
    }

    public Hologram removeLine(int line) {
        ArmorStand stand = stands.get(line - 1);
        stand.remove();

        stands.remove(line - 1);

        if (!refreshLines(line - 1)) return null;
        return this;
    }

    public boolean refreshLines(int line) {
        List<ArmorStand> standsTemp = new ArrayList<>();

        int count = 0;
        for (ArmorStand entry : stands) {
            if (count >= line) standsTemp.add(entry);
            count++;
        }

        for (ArmorStand stand : standsTemp) stand.teleport(stand.getLocation().add(0, 0.25, 0));

        return count >= 1;
    }

    public Hologram setLocation(Location location) {
        this.location = location;
        setLines(stands.stream().map(ArmorStandName::getName).collect(Collectors.toList()));
        return this;
    }

    public boolean hasLine(int line) {
        return line - 1 < stands.size() && line > 0;
    }

    public void remove() {
        for (Iterator<ArmorStand> it = stands.iterator(); it.hasNext(); ) {
            ArmorStand stand = it.next();
            stand.remove();
        }
        stands.clear();
    }

    public Location getLocation() {
        return location;
    }

    public List<ArmorStand> getStands() {
        return stands;
    }

    public String getName() {
        return name;
    }

    private double getHeight() {
        return stands.size() * 0.25;
    }


}
