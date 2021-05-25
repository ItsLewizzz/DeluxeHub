package fun.lewisdev.deluxehub.utility.reflection;

import org.bukkit.entity.ArmorStand;

import fun.lewisdev.deluxehub.DeluxeHub;

public class ArmorStandName {

    public static String getName(ArmorStand stand) {
        if (DeluxeHub.SERVER_VERSION > 8)
            return stand.getCustomName();

        String name = null;
        try {
            name = (String) ArmorStand.class.getMethod("getCustomName").invoke(stand);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return name;
    }

}
