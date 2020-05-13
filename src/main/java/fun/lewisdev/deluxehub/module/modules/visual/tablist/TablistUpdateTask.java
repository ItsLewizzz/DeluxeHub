package fun.lewisdev.deluxehub.module.modules.visual.tablist;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TablistUpdateTask implements Runnable {

    private TablistManager tablistManager;

    public TablistUpdateTask(TablistManager tablistManager) {
        this.tablistManager = tablistManager;
    }

    @Override
    public void run() {
        List<UUID> toRemove = new ArrayList<>();
        tablistManager.getPlayers().forEach(uuid -> {
            if (!tablistManager.updateTablist(uuid)) toRemove.add(uuid);
        });
        tablistManager.getPlayers().removeAll(toRemove);
    }

}
