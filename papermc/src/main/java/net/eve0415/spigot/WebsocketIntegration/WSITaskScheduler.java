package net.eve0415.spigot.WebsocketIntegration;

import org.bukkit.scheduler.BukkitRunnable;
import org.json.JSONException;

import net.eve0415.spigot.WebsocketIntegration.Util.WSIEventState;
import net.eve0415.spigot.WebsocketIntegration.Util.WSITask;

public class WSITaskScheduler implements WSITask {
    private final WSIPaperPlugin instance;

    public WSITaskScheduler(final WSIPaperPlugin instance) {
        this.instance = instance;
        serverIsReady();
    }

    @Override
    public int getOnlinePlayers() {
        return instance.getServer().getOnlinePlayers().size();
    }

    @Override
    public int getMaxPlayers() {
        return instance.getServer().getMaxPlayers();
    }

    @Override
    public double getTPS() {
        return (Math.round(instance.getServer().getTPS()[0] * 100.0D) / 100.0D);
    }

    // Task runs when the server is ready to serve the player which will send status
    // info after sever had done starting up.
    @Override
    public void serverIsReady() {
        new BukkitRunnable() {
            @Override
            public void run() {
                instance.getWebsocketManager().isStarting(false);
                updateStatus();
            }
        }.runTaskAsynchronously(instance);
    }

    @Override
    public void updateStatus() {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    instance.getWebsocketManager().send(WSIEventState.STATUS,
                            WebsocketManager.builder().status(getOnlinePlayers(), getMaxPlayers(), getTPS()));
                } catch (final JSONException e) {
                    instance.getWSILogger().error("There was an error trying to send websocket", e);
                }
            }
        }.runTaskTimerAsynchronously(instance, 0, 200);
    }
}
