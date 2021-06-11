package fun.lewisdev.deluxehub.action.actions;

import fun.lewisdev.deluxehub.DeluxeHubPlugin;
import fun.lewisdev.deluxehub.action.Action;
import fun.lewisdev.deluxehub.inventory.AbstractInventory;
import org.bukkit.entity.Player;

public class MenuAction implements Action {

    @Override
    public String getIdentifier() {
        return "MENU";
    }

    @Override
    public void execute(DeluxeHubPlugin plugin, Player player, String data) {
        AbstractInventory inventory = plugin.getInventoryManager().getInventory(data);

        if (inventory != null) {
            inventory.openInventory(player);
        } else {
            plugin.getLogger().warning("[MENU] Action Failed: Menu '" + data + "' not found.");
        }
    }
}
