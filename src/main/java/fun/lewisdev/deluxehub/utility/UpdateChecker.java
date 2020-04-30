package fun.lewisdev.deluxehub.utility;

import fun.lewisdev.deluxehub.Permissions;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

/*
   Credits: Benz56
   https://www.spigotmc.org/threads/async-update-checker-for-premium-and-regular-plugins.327921/
 */
public class UpdateChecker {

    private final JavaPlugin plugin;
    private final String localPluginVersion;
    private String spigotPluginVersion;

    private static final int ID = 49425;
    private static final Permission UPDATE_PERM = new Permission(Permissions.UPDATE_NOTIFICATION.getPermission(), PermissionDefault.TRUE);
    private static final long CHECK_INTERVAL = 12_000;

    public UpdateChecker(JavaPlugin plugin) {
        this.plugin = plugin;
        this.localPluginVersion = plugin.getDescription().getVersion();
    }

    public void checkForUpdate() {
        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                    try {
                        final HttpsURLConnection connection = (HttpsURLConnection) new URL("https://api.spigotmc.org/legacy/update.php?resource=" + ID).openConnection();
                        connection.setRequestMethod("GET");
                        spigotPluginVersion = new BufferedReader(new InputStreamReader(connection.getInputStream())).readLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                        cancel();
                        return;
                    }

                    if (localPluginVersion.equals(spigotPluginVersion)) return;

                    plugin.getLogger().info("An update for DeluxeHub (v%VERSION%) is available at:".replace("%VERSION%", spigotPluginVersion));
                    plugin.getLogger().info("https://www.spigotmc.org/resources/" + ID);

                    Bukkit.getScheduler().runTask(plugin, () -> Bukkit.getPluginManager().registerEvents(new Listener() {
                        @EventHandler(priority = EventPriority.MONITOR)
                        public void onPlayerJoin(final PlayerJoinEvent event) {
                            final Player player = event.getPlayer();
                            if (!player.hasPermission(UPDATE_PERM)) return;
                            player.sendMessage(TextUtil.color("&7An update (v%VERSION%) for DeluxeHub is available at:".replace("%VERSION%", spigotPluginVersion)));
                            player.sendMessage(TextUtil.color("&6https://www.spigotmc.org/resources/" + ID));
                        }
                    }, plugin));

                    cancel();
                });
            }
        }.runTaskTimer(plugin, 0, CHECK_INTERVAL);
    }
}