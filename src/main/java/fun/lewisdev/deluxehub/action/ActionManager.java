package fun.lewisdev.deluxehub.action;

import fun.lewisdev.deluxehub.DeluxeHubPlugin;
import fun.lewisdev.deluxehub.action.actions.*;
import fun.lewisdev.deluxehub.utility.PlaceholderUtil;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActionManager {

    private final DeluxeHubPlugin plugin;
    private final Map<String, Action> actions;

    public ActionManager(DeluxeHubPlugin plugin) {
        this.plugin = plugin;
        actions = new HashMap<>();
        load();
    }

    private void load() {
        registerAction(
                new MessageAction(),
                new BroadcastMessageAction(),
                new CommandAction(),
                new ConsoleCommandAction(),
                new SoundAction(),
                new PotionEffectAction(),
                new GamemodeAction(),
                new BungeeAction(),
                new CloseInventoryAction(),
                new ActionbarAction(),
                new TitleAction(),
                new MenuAction()
        );
    }

    public void registerAction(Action... actions) {
        Arrays.asList(actions).forEach(action -> this.actions.put(action.getIdentifier(), action));
    }

    public void executeActions(Player player, List<String> items) {
        items.forEach(item -> {
            String actionName = StringUtils.substringBetween(item, "[", "]");
            Action action = actionName == null ? null : actions.get(actionName.toUpperCase());

            if (action != null) {
                item = item.contains(" ") ? item.split(" ", 2)[1] : "";
                item = PlaceholderUtil.setPlaceholders(item, player);

                action.execute(plugin, player, item);
            }else{
                plugin.getLogger().warning("There was a problem attempting to process action: '" + item + "'");
            }
        });
    }
}
