package net.eve0415.spigot.WebsocketIntegration;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class WebsocketEventHandler {
    private WebsocketManager instance;
    private Socket socket;

    public WebsocketEventHandler(WebsocketManager instance, Socket socket) {
        this.instance = instance;
        this.socket = socket;
        initWebsocketEvents();
    }

    private void initWebsocketEvents() {
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                instance.getWSILogger().info("Connected");
                instance.isConnected(true);
            }
        });

        socket.on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                instance.getWSILogger().info("Disconnected: " + args[0].toString());
                instance.isConnected(false);
            }
        });

        socket.on(Socket.EVENT_RECONNECT, new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                instance.getWSILogger().info("Reconnected");
                instance.isConnected(true);
            }
        });
    }
}
