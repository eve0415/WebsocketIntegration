package net.eve0415.spigot.WebsocketIntegration;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.event.connection.PreLoginEvent;
import com.velocitypowered.api.event.player.KickedFromServerEvent;
import com.velocitypowered.api.event.player.ServerPostConnectEvent;
import com.velocitypowered.api.event.player.ServerPreConnectEvent;
import com.velocitypowered.api.proxy.Player;

import org.json.JSONException;

import net.eve0415.spigot.WebsocketIntegration.Util.LogEventType;
import net.eve0415.spigot.WebsocketIntegration.Util.WSIEventState;

public class VelocityEventListner {
    public VelocityEventListner(final WSIVelocityPlugin instance) {
        instance.getProxy().getEventManager().register(instance, this);
    }

    @Subscribe
    public void onPreLogin(final PreLoginEvent event) {
        try {
            WebsocketManager.getInstance().send(WSIEventState.LOG,
                    WebsocketManager.builder()
                            .log(LogEventType.AUTH,
                                    event.getUsername(),
                                    null,
                                    event.getConnection().getRemoteAddress().toString())
                            .setAddress(event.getConnection().getVirtualHost().get().toString())
                            .toJSON());
        } catch (final JSONException e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    public void onLogin(final LoginEvent event) {
        final Player profile = event.getPlayer();
        try {
            WebsocketManager.getInstance().send(WSIEventState.LOG,
                    WebsocketManager.builder()
                            .log(LogEventType.LOGIN,
                                    profile.getUsername(),
                                    profile.getUniqueId(),
                                    profile.getRemoteAddress().toString())
                            .setAddress(profile.getVirtualHost().get().toString())
                            .toJSON());
        } catch (final JSONException e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    public void onPreConnect(final ServerPreConnectEvent event) {
        final Player profile = event.getPlayer();
        try {
            WebsocketManager.getInstance().send(WSIEventState.LOG,
                    WebsocketManager.builder()
                            .log(LogEventType.PRECONNECT,
                                    profile.getUsername(),
                                    profile.getUniqueId(),
                                    profile.getRemoteAddress().toString())
                            .setAddress(profile.getVirtualHost().get().toString())
                            .connectingServer(event.getOriginalServer().getServerInfo().getName())
                            .toJSON());
        } catch (final JSONException e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    public void onPostConnect(final ServerPostConnectEvent event) {
        final Player profile = event.getPlayer();
        try {
            WebsocketManager.getInstance().send(WSIEventState.LOG,
                    WebsocketManager.builder()
                            .log(LogEventType.POSTCONNECT,
                                    profile.getUsername(),
                                    profile.getUniqueId(),
                                    profile.getRemoteAddress().toString())
                            .setAddress(profile.getVirtualHost().get().toString())
                            .connectedServer(profile.getCurrentServer().get().getServerInfo().getName())
                            .previousServer(event.getPreviousServer() == null
                                    ? null
                                    : event.getPreviousServer().getServerInfo().getName())
                            .toJSON());
        } catch (final JSONException e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    public void onDisconnect(final DisconnectEvent event) {
        final Player profile = event.getPlayer();
        try {
            WebsocketManager.getInstance().send(WSIEventState.LOG,
                    WebsocketManager.builder()
                            .log(LogEventType.DISCONNECT,
                                    profile.getUsername(),
                                    profile.getUniqueId(),
                                    profile.getRemoteAddress().toString())
                            .setAddress(profile.getVirtualHost().get().toString())
                            .toJSON());
        } catch (final JSONException e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    public void onKicked(final KickedFromServerEvent event) {
        final Player profile = event.getPlayer();
        try {
            WebsocketManager.getInstance().send(WSIEventState.LOG,
                    WebsocketManager.builder()
                            .log(LogEventType.KICK,
                                    profile.getUsername(),
                                    profile.getUniqueId(),
                                    profile.getRemoteAddress().toString())
                            .setAddress(profile.getVirtualHost().get().toString())
                            .kick(event.getServerKickReason().get().toString())
                            .toJSON());
        } catch (final JSONException e) {
            e.printStackTrace();
        }
    }
}
