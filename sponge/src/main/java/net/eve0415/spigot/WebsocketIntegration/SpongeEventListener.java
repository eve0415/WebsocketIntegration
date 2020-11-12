package net.eve0415.spigot.WebsocketIntegration;

import java.util.Optional;
import java.util.UUID;

import org.json.JSONException;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.entity.DestructEntityEvent;
import org.spongepowered.api.event.entity.living.humanoid.player.KickPlayerEvent;
import org.spongepowered.api.event.filter.IsCancelled;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent.Auth;
import org.spongepowered.api.event.network.ClientConnectionEvent.Disconnect;
import org.spongepowered.api.event.network.ClientConnectionEvent.Join;
import org.spongepowered.api.event.network.ClientConnectionEvent.Login;
import org.spongepowered.api.network.RemoteConnection;
import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.util.Tristate;

import net.eve0415.spigot.WebsocketIntegration.Util.LogEventType;
import net.eve0415.spigot.WebsocketIntegration.Util.WSIEventState;

public class SpongeEventListener {
    public SpongeEventListener(final WSISpongePlugin instance) {
        Sponge.getEventManager().registerListeners(instance, this);
    }

    private void sendChatMessage(final String name, final UUID uuid, final String message) {
        try {
            WebsocketManager.getInstance().send(WSIEventState.CHAT,
                    WebsocketManager.builder().message(name, uuid, message).toJSON());
        } catch (final JSONException e) {
            WebsocketManager.getInstance().getWSILogger().error("There was an error trying to send chat message", e);
        }
    }

    @Listener
    public void onJoin(final Join event) {
        sendChatMessage(event.getTargetEntity().getName(), event.getTargetEntity().getUniqueId(),
                event.getMessage().toPlain());
    }

    @Listener
    public void onQuit(final Disconnect event) {
        Player profile = event.getTargetEntity();

        sendChatMessage(event.getTargetEntity().getName(), event.getTargetEntity().getUniqueId(),
                event.getMessage().toPlain());
        try {
            WebsocketManager.getInstance().send(WSIEventState.LOG,
                    WebsocketManager.builder()
                            .log(LogEventType.DISCONNECT, profile.getName(), profile.getUniqueId(),
                                    profile.getConnection().getAddress()
                                            .getHostString())
                            .toJSON());
        } catch (final JSONException e) {
            e.printStackTrace();
        }
    }

    @Listener
    public void onDeath(final DestructEntityEvent.Death event) {
        if (!(event.getTargetEntity() instanceof Player)) return;
        final Player player = (Player) event.getTargetEntity();
        sendChatMessage(player.getName(), player.getUniqueId(),
                event.getMessage().toPlain());
    }

    @Listener
    public void onMessage(final MessageChannelEvent.Chat event) {
        final Optional<Player> optPlayer = event.getCause().first(Player.class);
        final String pl = "<" + optPlayer.get().getName() + "> ";
        sendChatMessage(optPlayer.get().getName(), optPlayer.get().getUniqueId(), event.getMessage().toPlain()
                .substring(pl.length()));
    }

    @Listener(order = Order.PRE)
    public void onAuth(final Auth event) {
        RemoteConnection connection = event.getConnection();
        GameProfile profile = event.getProfile();
        try {
            WebsocketManager.getInstance().send(WSIEventState.LOG,
                    WebsocketManager.builder()
                            .log(LogEventType.AUTH, profile.getName().get(), profile.getUniqueId(),
                                    connection.getAddress().getHostString())
                            .toJSON());
        } catch (final JSONException e) {
            e.printStackTrace();
        }
    }

    @Listener(order = Order.BEFORE_POST)
    @IsCancelled(Tristate.UNDEFINED)
    public void onLogin(final Login event) {
        RemoteConnection connection = event.getConnection();
        GameProfile profile = event.getProfile();
        try {
            if (event.isCancelled()) {
                WebsocketManager.getInstance().send(WSIEventState.LOG,
                        WebsocketManager.builder()
                                .log(LogEventType.LOGIN, profile.getName().get(), profile.getUniqueId(),
                                        connection.getAddress().getHostString())
                                .kick(event.getMessage().toPlain()).toJSON());
            } else {
                WebsocketManager.getInstance().send(WSIEventState.LOG,
                        WebsocketManager.builder()
                                .log(LogEventType.LOGIN, profile.getName().get(), profile.getUniqueId(),
                                        connection.getAddress().getHostString())
                                .setAddress(connection.getVirtualHost().toString()).toJSON());
            }
        } catch (final JSONException e) {
            e.printStackTrace();
        }
    }

    @Listener
    public void onKick(final KickPlayerEvent event) {
        Player profile = event.getTargetEntity();
        try {
            WebsocketManager.getInstance().send(WSIEventState.LOG,
                    WebsocketManager.builder()
                            .log(LogEventType.KICK, profile.getName(), profile.getUniqueId(),
                                    profile.getConnection().getAddress().getHostString())
                            .kick(event.getMessage().toPlain()).toJSON());
        } catch (final JSONException e) {
            e.printStackTrace();
        }
    }
}
