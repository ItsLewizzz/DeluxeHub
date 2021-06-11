package fun.lewisdev.deluxehub.inventory;

import fun.lewisdev.deluxehub.DeluxeHubPlugin;
import fun.lewisdev.deluxehub.utility.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class AbstractInventory implements Listener {

    private DeluxeHubPlugin plugin;
    private boolean refreshEnabled = false;
    private List<UUID> openInventories;

    public AbstractInventory(DeluxeHubPlugin plugin) {
        this.plugin = plugin;
        openInventories = new ArrayList<>();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void setInventoryRefresh(long value) {
        if (value <= 0) return;
        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, new InventoryTask(this), 0L, value);
        refreshEnabled = true;
    }

    public abstract void onEnable();

    protected abstract Inventory getInventory();

    protected DeluxeHubPlugin getPlugin() {
        return plugin;
    }

    public Inventory refreshInventory(Player player, Inventory inventory) {
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack item = getInventory().getItem(i);
            if (item == null || item.getType() == Material.AIR || !item.hasItemMeta()) continue;

            ItemStackBuilder newItem = new ItemStackBuilder(item.clone());
            if (item.getItemMeta().hasDisplayName()) newItem.withName(item.getItemMeta().getDisplayName(), player);
            if (item.getItemMeta().hasLore()) newItem.withLore(item.getItemMeta().getLore(), player);
            inventory.setItem(i, newItem.build());
        }
        return inventory;
    }

    public void openInventory(Player player) {
        if (getInventory() == null) return;

        player.openInventory(refreshInventory(player, getInventory()));
        if (refreshEnabled && !openInventories.contains(player.getUniqueId())) {
            openInventories.add(player.getUniqueId());
        }
    }

    public List<UUID> getOpenInventories() {
        return openInventories;
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getView().getTopInventory().getHolder() instanceof InventoryBuilder && refreshEnabled) {
            openInventories.remove(event.getPlayer().getUniqueId());
            //System.out.println("removed " + event.getPlayer().getName());
        }
    }

}
