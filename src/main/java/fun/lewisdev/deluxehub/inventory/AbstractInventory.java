package fun.lewisdev.deluxehub.inventory;

import fun.lewisdev.deluxehub.DeluxeHubPlugin;
import fun.lewisdev.deluxehub.utility.ItemStackBuilder;
import fun.lewisdev.deluxehub.utility.universal.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;

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
            ItemStack itemStack = newItem.build();
            if (item.getType() == XMaterial.PLAYER_HEAD.parseMaterial()) {
                SkullMeta meta = (SkullMeta) item.getItemMeta();
                // This is a mess
                // But it works
                if (meta.hasOwner()) {
                    if (meta.getOwner().equalsIgnoreCase("%player%")) {
                        if (XMaterial.supports(18)) {
                            PlayerProfile profile = player.getPlayerProfile();
                            meta.setOwnerProfile(profile);
                        } else if (XMaterial.supports(12)) {
                            meta.setOwningPlayer(player);
                        } else {
                            meta.setOwner(player.getName());
                        }
                    } else {
                        OfflinePlayer player1 = Bukkit.getOfflinePlayer(meta.getOwner());
                        if (XMaterial.supports(18)) {
                            PlayerProfile profile = player1.getPlayerProfile();
                            meta.setOwnerProfile(profile);
                        } else if (XMaterial.supports(12)) {
                            meta.setOwningPlayer(player1);
                        } else {
                            meta.setOwner(meta.getOwner());
                        }
                    }
                }
                itemStack.setItemMeta(meta);
            }

            inventory.setItem(i, itemStack);
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
