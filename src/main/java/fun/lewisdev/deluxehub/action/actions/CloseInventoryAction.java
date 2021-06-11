package fun.lewisdev.deluxehub.action.actions;

import fun.lewisdev.deluxehub.DeluxeHubPlugin;
import fun.lewisdev.deluxehub.action.Action;
import org.bukkit.entity.Player;

public class CloseInventoryAction implements Action {

    @Override
    public String getIdentifier() {
        return "CLOSE";
    }

    @Override
    public void execute(DeluxeHubPlugin plugin, Player player, String data) {
        player.closeInventory();
    }
}
