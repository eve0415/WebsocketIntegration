package net.eve0415.spigot.WebsocketIntegration;

import java.util.concurrent.TimeUnit;

import org.json.JSONException;

import net.eve0415.spigot.WebsocketIntegration.Util.WSIEventState;
import net.eve0415.spigot.WebsocketIntegration.Util.WSITask;

public class WSIVelocityTaskScheduler implements WSITask {
    private final WSIVelocityPlugin instance;

    public WSIVelocityTaskScheduler(final WSIVelocityPlugin instance) {
        this.instance = instance;
        serverIsReady();
    }

    @Override
    public int getOnlinePlayers() {
        return instance.getProxy().getAllPlayers().size();
    }

    @Override
    public int getMaxPlayers() {
        return instance.getProxy().getConfiguration().getShowMaxPlayers();
    }

    @Override
    public double getTPS() {
        return 20; // Obviously, proxy server doesn't have Tick Per Second.
    }

    @Override
    public void serverIsReady() {
        instance.getProxy().getScheduler().buildTask(instance, new Runnable() {
            public void run() {
                instance.getWebsocketManager().isStarting(false);
                updateStatus();
            }
        }).delay(0, TimeUnit.SECONDS).schedule();
    }

    @Override
    public void updateStatus() {
        instance.getProxy().getScheduler().buildTask(instance, new Runnable() {
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
        }).repeat(20, TimeUnit.SECONDS).schedule();
    }
}
