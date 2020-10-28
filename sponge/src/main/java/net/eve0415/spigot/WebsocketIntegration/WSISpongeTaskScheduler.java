package net.eve0415.spigot.WebsocketIntegration;

import java.util.concurrent.TimeUnit;

import org.json.JSONException;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.scheduler.Task;

import net.eve0415.spigot.WebsocketIntegration.Util.WSIEventState;
import net.eve0415.spigot.WebsocketIntegration.Util.WSITask;

public class WSISpongeTaskScheduler implements WSITask {
    private final WSISpongePlugin instance;
    private final Task.Builder taskBuilder = Task.builder();

    public WSISpongeTaskScheduler(final WSISpongePlugin instance) {
        this.instance = instance;
        serverIsReady();
    }

    @Override
    public int getOnlinePlayers() {
        return Sponge.getServer().getOnlinePlayers().size();
    }

    @Override
    public int getMaxPlayers() {
        return Sponge.getServer().getMaxPlayers();
    }

    @Override
    public double getTPS() {
        return Sponge.getServer().getTicksPerSecond();
    }

    @Override
    public void serverIsReady() {
        taskBuilder.execute(new Runnable() {
            public void run() {
                instance.getWebsocketManager().isStarting(false);
                updateStatus();
            }
        }).submit(instance);
    }

    @Override
    public void updateStatus() {
        taskBuilder.execute(new Runnable() {
            public void run() {
                try {
                    instance.getWebsocketManager().send(WSIEventState.STATUS,
                            WebsocketManager.builder().basic(instance.getPlatformType())
                                    .status(instance.getPlatformType(), getOnlinePlayers(),
                                            getMaxPlayers(), getTPS())
                                    .toJSON());
                } catch (final JSONException e) {
                    instance.getWSILogger().error("There was an error trying to send websocket", e);
                }
            }
        }).async().interval(20, TimeUnit.SECONDS).submit(instance);
    }
}
