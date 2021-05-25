package fun.lewisdev.deluxehub.hook.hooks.head;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import fun.lewisdev.deluxehub.DeluxeHub;
import fun.lewisdev.deluxehub.hook.PluginHook;
import me.arcaniax.hdb.api.DatabaseLoadEvent;
import me.arcaniax.hdb.api.HeadDatabaseAPI;

public class DatabaseHead implements PluginHook, HeadHook, Listener {

    private DeluxeHub plugin;
    private HeadDatabaseAPI api;

    @Override
    public void onEnable(DeluxeHub plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        api = new HeadDatabaseAPI();
    }

    @Override
    public ItemStack getHead(String data) {
        return api.getItemHead(data);
    }

    @EventHandler
    public void onDatabaseLoad(DatabaseLoadEvent event) {
        plugin.getInventoryManager().onEnable(plugin);
    }

}
