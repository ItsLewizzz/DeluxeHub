package fun.lewisdev.deluxehub.module.modules.player;

import fun.lewisdev.deluxehub.DeluxeHubPlugin;
import fun.lewisdev.deluxehub.module.Module;
import fun.lewisdev.deluxehub.module.ModuleType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

public class PlayerOffHandSwap extends Module {

    public PlayerOffHandSwap(DeluxeHubPlugin plugin) {
        super(plugin, ModuleType.PLAYER_OFFHAND_LISTENER);
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
    }

    @EventHandler
    public void onPlayerSwapItem(PlayerSwapHandItemsEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getRawSlot() != event.getSlot() && event.getCursor() != null && event.getSlot() == 40) {
            event.setCancelled(true);
        }
    }
}
