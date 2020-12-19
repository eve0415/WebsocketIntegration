package net.eve0415.spigot.WebsocketIntegration;

import java.util.Optional;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.*;
import com.velocitypowered.api.event.player.*;
import com.velocitypowered.api.event.player.KickedFromServerEvent.*;
import com.velocitypowered.api.proxy.InboundConnection;
import com.velocitypowered.api.proxy.Player;

import org.json.JSONException;

import net.eve0415.spigot.WebsocketIntegration.WebsocketSender.WebsocketBuilder;
import net.eve0415.spigot.WebsocketIntegration.Util.LogEventType;
import net.eve0415.spigot.WebsocketIntegration.Util.WSIEventState;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.serializer.plain.PlainComponentSerializer;

public class VelocityEventListner {
    public VelocityEventListner(final WSIVelocityPlugin instance) {
        instance.getProxy().getEventManager().register(instance, this);
    }

    @Subscribe
    public void onPreLogin(final PreLoginEvent event) {
        final InboundConnection connection = event.getConnection();
        try {
            WebsocketManager.getInstance().send(WSIEventState.LOG,
                    WebsocketManager.builder()
                            .log(LogEventType.AUTH,
                                    event.getUsername(),
                                    null,
                                    connection.getRemoteAddress().toString())
                            .setAddress(connection.getVirtualHost().get().toString())
                            .clientVersion(connection.getProtocolVersion().getName())
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
                            .clientType(getClientType(profile))
                            .clientVersion(profile.getProtocolVersion().getName())
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
                            .clientType(getClientType(profile))
                            .clientVersion(profile.getProtocolVersion().getName())
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
                            .clientType(getClientType(profile))
                            .clientVersion(profile.getProtocolVersion().getName())
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
            final WebsocketBuilder message = WebsocketManager.builder().log(LogEventType.KICKEDFROM,
                    profile.getUsername(),
                    profile.getUniqueId(),
                    profile.getRemoteAddress().toString())
                    .setAddress(profile.getVirtualHost().get().toString())
                    .previousServer(event.getServer().getServerInfo().getName());

            if (event.getResult() instanceof DisconnectPlayer) {
                final DisconnectPlayer res = (DisconnectPlayer) event.getResult();
                message.kick(getReason(event.getServerKickReason(), res.getReasonComponent()))
                        .fulfill("DisconnectPlayer");
            } else if (event.getResult() instanceof RedirectPlayer) {
                final RedirectPlayer res = (RedirectPlayer) event.getResult();
                message.kick(getReason(event.getServerKickReason(), res.getMessageComponent()))
                        .fulfill("RedirectPlayer");
            } else if (event.getResult() instanceof Notify) {
                final Notify res = (Notify) event.getResult();
                message.kick(getReason(event.getServerKickReason(), res.getMessageComponent())).fulfill("Notify");
            }

            WebsocketManager.getInstance().send(WSIEventState.LOG, message.toJSON());
        } catch (final JSONException e) {
            e.printStackTrace();
        }
    }

    private String getClientType(final Player player) {
        if (player.getGameProfileProperties().toArray().length == 2)
            return player.getGameProfileProperties().get(1).getName();
        return "Vanilla";
    }

    private String getReason(final Optional<Component> optional, final Component event) {
        final PlainComponentSerializer ComponentToPlain = new PlainComponentSerializer(c -> "",
                TranslatableComponent::key);
        if (!optional.isPresent() && event == null) return "Unknown reason";

        return event == null ? ComponentToPlain.serialize(optional.get()) : ComponentToPlain.serialize(event);
    }
}
