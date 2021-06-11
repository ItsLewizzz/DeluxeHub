package fun.lewisdev.deluxehub.action;

import fun.lewisdev.deluxehub.DeluxeHubPlugin;
import org.bukkit.entity.Player;

public interface Action {

    String getIdentifier();

    void execute(DeluxeHubPlugin plugin, Player player, String data);

}
