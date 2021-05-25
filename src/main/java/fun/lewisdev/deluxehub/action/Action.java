package fun.lewisdev.deluxehub.action;

import org.bukkit.entity.Player;

import fun.lewisdev.deluxehub.DeluxeHub;

public interface Action {

    String getIdentifier();

    void execute(DeluxeHub plugin, Player player, String data);

}
