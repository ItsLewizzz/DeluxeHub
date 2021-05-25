package fun.lewisdev.deluxehub.utility;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import me.clip.placeholderapi.PlaceholderAPI;

public class PlaceholderUtil {

    public static boolean PAPI;

    public static String setPlaceholders(String text, Player player) {

        if (text.contains("%player%") && player != null)
            text = text.replace("%player%", player.getName());

        if (text.contains("%online%"))
            text = text.replace("%online%", String.valueOf(Bukkit.getServer().getOnlinePlayers().size()));

        if (text.contains("%online_max%"))
            text = text.replace("%online_max%", String.valueOf(Bukkit.getServer().getMaxPlayers()));

        if (text.contains("%location%") && player != null) {
            Location l = player.getLocation();
            text = text.replace("%location%", l.getBlockX() + ", " + l.getBlockY() + ", " + l.getBlockZ());
        }

        /*
         * try { final String BUNGEE_PATTERN = "%bungeecord_(\\w+)%"; Pattern pattern =
         * Pattern.compile(BUNGEE_PATTERN); Matcher matcher = pattern.matcher(text);
         * while(matcher.find()) { text =
         * matcher.replaceAll(String.valueOf(BungeeCord.getServerCount(player,
         * matcher.group(1)))); } }catch (Exception ex) { ex.printStackTrace(); }
         */

        if (PAPI && player != null)
            text = PlaceholderAPI.setPlaceholders(player, text);

        return text;

    }

}
