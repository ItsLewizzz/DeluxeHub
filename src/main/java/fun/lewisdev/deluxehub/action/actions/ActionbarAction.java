package fun.lewisdev.deluxehub.action.actions;

import org.bukkit.entity.Player;

import fun.lewisdev.deluxehub.DeluxeHub;
import fun.lewisdev.deluxehub.action.Action;
import fun.lewisdev.deluxehub.utility.TextUtil;
import fun.lewisdev.deluxehub.utility.reflection.ActionBar;

public class ActionbarAction implements Action {

    @Override
    public String getIdentifier() {
        return "ACTIONBAR";
    }

    @Override
    public void execute(DeluxeHub plugin, Player player, String data) {
        ActionBar.sendActionBar(player, TextUtil.color(data));
    }
}
