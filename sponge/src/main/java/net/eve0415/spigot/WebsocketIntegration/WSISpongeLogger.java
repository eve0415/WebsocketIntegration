package net.eve0415.spigot.WebsocketIntegration;

import org.slf4j.Logger;

import net.eve0415.spigot.WebsocketIntegration.Util.WSILogger;

public class WSISpongeLogger implements WSILogger {
    private final Logger logger;

    public WSISpongeLogger(final Logger logger) {
        this.logger = logger;
    }

    @Override
    public void error(final String message, final Throwable error) {
        logger.error(message, error);
    }

    @Override
    public void error(final String message) {
        logger.error(message);
    }

    @Override
    public void warning(final String message) {
        logger.warn(message);
    }

    @Override
    public void info(final String message) {
        logger.info(message);
    }

}
