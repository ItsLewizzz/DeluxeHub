package fun.lewisdev.deluxehub.action.actions;

import fun.lewisdev.deluxehub.DeluxeHubPlugin;
import fun.lewisdev.deluxehub.action.Action;
import fun.lewisdev.deluxehub.utility.universal.XPotion;
import org.bukkit.entity.Player;

public class PotionEffectAction implements Action {

    @Override
    public String getIdentifier() {
        return "EFFECT";
    }

    @Override
    public void execute(DeluxeHubPlugin plugin, Player player, String data) {
        String[] args = data.split(";");
        player.addPotionEffect(XPotion.matchXPotion(args[0]).get().parsePotion(1000000, Integer.parseInt(args[1]) - 1));
    }
}
