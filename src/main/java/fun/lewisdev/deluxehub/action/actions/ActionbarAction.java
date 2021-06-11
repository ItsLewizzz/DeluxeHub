package fun.lewisdev.deluxehub.action.actions;

import fun.lewisdev.deluxehub.DeluxeHubPlugin;
import fun.lewisdev.deluxehub.action.Action;
import fun.lewisdev.deluxehub.utility.TextUtil;
import fun.lewisdev.deluxehub.utility.reflection.ActionBar;
import org.bukkit.entity.Player;

public class ActionbarAction implements Action {

    @Override
    public String getIdentifier() {
        return "ACTIONBAR";
    }

    @Override
    public void execute(DeluxeHubPlugin plugin, Player player, String data) {
        ActionBar.sendActionBar(player, TextUtil.color(data));
    }
}
