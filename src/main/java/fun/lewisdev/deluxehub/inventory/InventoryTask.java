package fun.lewisdev.deluxehub.inventory;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class InventoryTask implements Runnable {

    private AbstractInventory inventory;

    InventoryTask(AbstractInventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public void run() {
        for (UUID uuid : inventory.getOpenInventories()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                inventory.refreshInventory(player, player.getOpenInventory().getTopInventory());
            }
        }
    }
}
