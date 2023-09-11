package fun.lewisdev.deluxehub.module.modules.world;

import fun.lewisdev.deluxehub.DeluxeHubPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;

/**
 * @author Ture Bentzin
 * @since 11-09-2023
 */
public class WorldLoadListener implements Listener {

    private final DeluxeHubPlugin plugin;

    public WorldLoadListener(DeluxeHubPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onWorldLoad(WorldLoadEvent worldLoadEvent) {
        //only listens if automatic-world-load is true so no checks here
        plugin.getModuleManager().registerNewWorld(worldLoadEvent.getWorld().getName()); //name used because plugin uses names elsewhere
    }
}
