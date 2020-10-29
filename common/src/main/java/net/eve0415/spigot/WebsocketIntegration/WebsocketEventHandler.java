package net.eve0415.spigot.WebsocketIntegration;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import net.eve0415.spigot.WebsocketIntegration.Util.WSIEventState;
import net.eve0415.spigot.WebsocketIntegration.Util.WSIPlatformType;

public class WebsocketEventHandler {
    private final Socket socket;

    public WebsocketEventHandler(final WebsocketManager instance, final Socket socket) {
        this.socket = socket;
        initWebsocketEvents();
    }

    private void initWebsocketEvents() {
        socket.once(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                WebsocketManager.getInstance().getWSILogger().info("Connected");
                WebsocketManager.getInstance().isConnected(true);
                loadReconnectEvent();
                configureRoom();
                if (WebsocketManager.getInstance().isStarting()) {
                    try {
                        WebsocketManager.getInstance().send(WSIEventState.STARTING,
                                WebsocketManager.builder().basic(WebsocketManager.getInstance().getPlatformType())
                                        .toJSON());
                    } catch (final JSONException e) {
                        WebsocketManager.getInstance().getWSILogger()
                                .error("There was an error trying to send starting status.", e);
                    }
                }
                if (WebsocketManager.getInstance().getPlatformType() == WSIPlatformType.Velocity)
                    WebsocketManager.getInstance().sendServerInfo();
            }
        });

        socket.on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                WebsocketManager.getInstance().getWSILogger().info("Disconnected: " + args[0].toString());
                WebsocketManager.getInstance().isConnected(false);
            }
        });

        socket.on("chat", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                try {
                    final JSONObject json = new JSONObject((args[0].toString()));
                    WebsocketManager.getInstance().handleWebsocketMessage(json.getString("name"),
                            json.getString("UUID"), json.getString("URL"), json.getString("message"));
                } catch (final JSONException e) {
                    WebsocketManager.getInstance().getWSILogger().error("There was an error trying to parse message",
                            e);
                }
            }
        });
    }

    private void loadReconnectEvent() {
        socket.on(Socket.EVENT_RECONNECT, new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                WebsocketManager.getInstance().getWSILogger().info("Reconnected");
                WebsocketManager.getInstance().isConnected(true);
                configureRoom();
                if (WebsocketManager.getInstance().getPlatformType() == WSIPlatformType.Velocity)
                    WebsocketManager.getInstance().sendServerInfo();
            }
        });
    }

    private void configureRoom() {
        socket.emit("ROOM", WebsocketManager.getInstance().getServerPort());
    }
}
