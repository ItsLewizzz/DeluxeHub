package fun.lewisdev.deluxehub.module.modules.visual.scoreboard;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ScoreUpdateTask implements Runnable {

    private ScoreboardManager scoreboardManager;

    public ScoreUpdateTask(ScoreboardManager scoreboardManager) {
        this.scoreboardManager = scoreboardManager;
    }

    @Override
    public void run() {
        List<UUID> toRemove = new ArrayList<>();
        scoreboardManager.getPlayers().forEach(uuid -> {
            if (scoreboardManager.updateScoreboard(uuid) == null) toRemove.add(uuid);
        });
        scoreboardManager.getPlayers().removeAll(toRemove);
    }

}
