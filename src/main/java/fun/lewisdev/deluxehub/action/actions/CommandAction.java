package fun.lewisdev.deluxehub.action.actions;

import fun.lewisdev.deluxehub.DeluxeHub;
import fun.lewisdev.deluxehub.action.Action;
import org.bukkit.entity.Player;

public class CommandAction implements Action {

    @Override
    public String getIdentifier() {
        return "COMMAND";
    }

    @Override
    public void execute(DeluxeHub plugin, Player player, String data) {
        player.chat(data.contains("/") ? data : "/" + data);
    }
}
