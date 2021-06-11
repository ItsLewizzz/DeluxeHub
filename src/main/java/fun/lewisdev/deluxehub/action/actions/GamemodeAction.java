package fun.lewisdev.deluxehub.action.actions;

import fun.lewisdev.deluxehub.DeluxeHubPlugin;
import fun.lewisdev.deluxehub.action.Action;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class GamemodeAction implements Action {

    @Override
    public String getIdentifier() {
        return "GAMEMODE";
    }

    @Override
    public void execute(DeluxeHubPlugin plugin, Player player, String data) {
        try {
            player.setGameMode(GameMode.valueOf(data.toUpperCase()));
        } catch (IllegalArgumentException ex) {
            Bukkit.getLogger().warning("[DeluxeHub Action] Invalid gamemode name: " + data.toUpperCase());
        }
    }
}
