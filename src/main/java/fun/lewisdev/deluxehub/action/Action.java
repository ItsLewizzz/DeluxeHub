package fun.lewisdev.deluxehub.action;

import fun.lewisdev.deluxehub.DeluxeHub;
import org.bukkit.entity.Player;

public interface Action {

    String getIdentifier();

    void execute(DeluxeHub plugin, Player player, String data);

}
