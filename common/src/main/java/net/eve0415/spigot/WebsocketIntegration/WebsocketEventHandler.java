package net.eve0415.spigot.WebsocketIntegration;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import net.eve0415.spigot.WebsocketIntegration.Util.WSIEventState;

public class WebsocketEventHandler {
    private Socket socket;

    public WebsocketEventHandler(WebsocketManager instance, Socket socket) {
        this.socket = socket;
        initWebsocketEvents();
    }

    private void initWebsocketEvents() {
        socket.once(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                WebsocketManager.getInstance().getWSILogger().info("Connected");
                WebsocketManager.getInstance().isConnected(true);
                WebsocketManager.getInstance().send(WSIEventState.STARTING, null);
            }
        });

        socket.on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                WebsocketManager.getInstance().getWSILogger().info("Disconnected: " + args[0].toString());
                WebsocketManager.getInstance().isConnected(false);
            }
        });

        socket.on(Socket.EVENT_RECONNECT, new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                WebsocketManager.getInstance().getWSILogger().info("Reconnected");
                WebsocketManager.getInstance().isConnected(true);
            }
        });
    }
}
