package fun.lewisdev.deluxehub.action.actions;

import fun.lewisdev.deluxehub.DeluxeHubPlugin;
import fun.lewisdev.deluxehub.action.Action;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ConsoleCommandAction implements Action {

    @Override
    public String getIdentifier() {
        return "CONSOLE";
    }

    @Override
    public void execute(DeluxeHubPlugin plugin, Player player, String data) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), data);
    }
}
