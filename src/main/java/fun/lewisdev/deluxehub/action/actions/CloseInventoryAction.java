package fun.lewisdev.deluxehub.action.actions;

import fun.lewisdev.deluxehub.DeluxeHub;
import fun.lewisdev.deluxehub.action.Action;
import org.bukkit.entity.Player;

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
