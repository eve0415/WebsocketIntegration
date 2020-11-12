package net.eve0415.spigot.WebsocketIntegration.Util;

public enum LogEventType {
        AUTH("AUTH"),
        LOGIN("LOGIN"),
        KICK("KICK"),
        DISCONNECT("DISCONNECT"),
        PRECONNECT("PRECONNECT"),
        POSTCONNECT("POSTCONNECT");

        private final String value;

        private LogEventType(final String value) {
                this.value = value;
        }

        public String getValue() {
                return this.value;
        }
}
