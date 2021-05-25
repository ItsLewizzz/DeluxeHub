package fun.lewisdev.deluxehub.action.actions;

import org.bukkit.entity.Player;

import fun.lewisdev.deluxehub.DeluxeHub;
import fun.lewisdev.deluxehub.action.Action;
import fun.lewisdev.deluxehub.utility.universal.XPotion;

public class PotionEffectAction implements Action {

    @Override
    public String getIdentifier() {
        return "EFFECT";
    }

    @Override
    public void execute(DeluxeHub plugin, Player player, String data) {
        String[] args = data.split(";");
        player.addPotionEffect(XPotion.matchXPotion(args[0]).get().parsePotion(1000000, Integer.parseInt(args[1]) - 1));
    }
}
