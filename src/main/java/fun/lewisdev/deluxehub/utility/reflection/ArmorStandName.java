package fun.lewisdev.deluxehub.utility.reflection;

import fun.lewisdev.deluxehub.utility.universal.XMaterial;
import org.bukkit.entity.ArmorStand;

public class ArmorStandName {

    public static String getName(ArmorStand stand) {
        if (XMaterial.supports(8)) return stand.getCustomName();

        String name = null;
        try {
            name = (String) ArmorStand.class.getMethod("getCustomName").invoke(stand);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return name;
    }

}
