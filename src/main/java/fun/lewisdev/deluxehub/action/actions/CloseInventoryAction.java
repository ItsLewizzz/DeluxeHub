package fun.lewisdev.deluxehub.action.actions;

import org.bukkit.entity.Player;

import fun.lewisdev.deluxehub.DeluxeHub;
import fun.lewisdev.deluxehub.action.Action;

public class CloseInventoryAction implements Action {

    @Override
    public String getIdentifier() {
        return "CLOSE";
    }

    @Override
    public void execute(DeluxeHub plugin, Player player, String data) {
        player.closeInventory();
    }
}
