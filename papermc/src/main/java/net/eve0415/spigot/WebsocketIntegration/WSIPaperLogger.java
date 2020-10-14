package net.eve0415.spigot.WebsocketIntegration;

import java.util.logging.Level;
import java.util.logging.Logger;

import net.eve0415.spigot.WebsocketIntegration.Util.WSILogger;

public class WSIPaperLogger implements WSILogger {
    private final Logger logger;

    public WSIPaperLogger(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void error(String message, Throwable error) {
        logger.log(Level.WARNING, message, error);
    }

    @Override
    public void error(String message) {
        logger.warning(message);
    }

    @Override
    public void warning(String message) {
        logger.warning(message);
    }

    @Override
    public void info(String message) {
        logger.info(message);
    }

}
