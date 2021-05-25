package fun.lewisdev.deluxehub.module.modules.visual.tablist;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Objects;

import com.google.common.base.Strings;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import fun.lewisdev.deluxehub.utility.reflection.ReflectionUtils;

public class TablistHelper {

    public static void sendTabList(Player player, String header, String footer) {
        Objects.requireNonNull(player, "Cannot update tab for null player");
        header = Strings.isNullOrEmpty(header) ? ""
                : ChatColor.translateAlternateColorCodes('&', header).replace("%player%", player.getDisplayName());
        footer = Strings.isNullOrEmpty(footer) ? ""
                : ChatColor.translateAlternateColorCodes('&', footer).replace("%player%", player.getDisplayName());

        try {
            Method chatComponentBuilderMethod = ReflectionUtils.getNMSClass("IChatBaseComponent")
                    .getDeclaredClasses()[0].getMethod("a", String.class);
            Object tabHeader = chatComponentBuilderMethod.invoke(null, "{\"text\":\"" + header + "\"}");
            Object tabFooter = chatComponentBuilderMethod.invoke(null, "{\"text\":\"" + footer + "\"}");
            Object packet = ReflectionUtils.getNMSClass("PacketPlayOutPlayerListHeaderFooter").getConstructor()
                    .newInstance();

            Field aField;
            Field bField;
            try {
                aField = packet.getClass().getDeclaredField("a");
                bField = packet.getClass().getDeclaredField("b");
            } catch (Exception ex) {
                aField = packet.getClass().getDeclaredField("header");
                bField = packet.getClass().getDeclaredField("footer");
            }

            aField.setAccessible(true);
            aField.set(packet, tabHeader);

            bField.setAccessible(true);
            bField.set(packet, tabFooter);

            ReflectionUtils.sendPacket(player, packet);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
