package net.eve0415.spigot.WebsocketIntegration;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.event.connection.PreLoginEvent;
import com.velocitypowered.api.event.player.KickedFromServerEvent;
import com.velocitypowered.api.event.player.KickedFromServerEvent.DisconnectPlayer;
import com.velocitypowered.api.event.player.KickedFromServerEvent.Notify;
import com.velocitypowered.api.event.player.KickedFromServerEvent.RedirectPlayer;
import com.velocitypowered.api.event.player.ServerPostConnectEvent;
import com.velocitypowered.api.event.player.ServerPreConnectEvent;
import com.velocitypowered.api.proxy.Player;

import org.json.JSONException;

import net.eve0415.spigot.WebsocketIntegration.Util.LogEventType;
import net.eve0415.spigot.WebsocketIntegration.Util.WSIEventState;
import net.eve0415.spigot.WebsocketIntegration.WebsocketSender.WebsocketBuilder;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.serializer.plain.PlainComponentSerializer;

public class VelocityEventListner {
    private static final PlainComponentSerializer ComponentToPlain = new PlainComponentSerializer(c -> "",
            TranslatableComponent::key);

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
            final WebsocketBuilder message = WebsocketManager.builder().log(LogEventType.KICKEDFROM,
                    profile.getUsername(),
                    profile.getUniqueId(),
                    profile.getRemoteAddress().toString())
                    .setAddress(profile.getVirtualHost().get().toString())
                    .previousServer(event.getServer().getServerInfo().getName());

            if (event.getResult() instanceof DisconnectPlayer) {
                final DisconnectPlayer res = (DisconnectPlayer) event.getResult();
                final String plain = res.getReasonComponent() == null
                        ? ComponentToPlain.serialize(event.getServerKickReason().get())
                        : ComponentToPlain.serialize(res.getReasonComponent());
                WebsocketManager.getInstance().getWSILogger().info(plain);
                message.kick(plain).fulfill("DisconnectPlayer");
            } else if (event.getResult() instanceof RedirectPlayer) {
                final RedirectPlayer res = (RedirectPlayer) event.getResult();
                final String plain = res.getMessageComponent() == null
                        ? ComponentToPlain.serialize(event.getServerKickReason().get())
                        : ComponentToPlain.serialize(res.getMessageComponent());
                WebsocketManager.getInstance().getWSILogger().info(plain);
                message.kick(plain).fulfill("RedirectPlayer");
            } else if (event.getResult() instanceof Notify) {
                final Notify res = (Notify) event.getResult();
                final String plain = res.getMessageComponent() == null
                        ? ComponentToPlain.serialize(event.getServerKickReason().get())
                        : ComponentToPlain.serialize(res.getMessageComponent());
                WebsocketManager.getInstance().getWSILogger().info(plain);
                message.kick(plain).fulfill("Notify");
            }

            WebsocketManager.getInstance().send(WSIEventState.LOG, message.toJSON());
        } catch (final JSONException e) {
            e.printStackTrace();
        }
    }
}
