package net.eve0415.spigot.WebsocketIntegration.Util;

public interface WSILogger {
    /**
     * Logs an error message and an exception to console
     *
     * @param message the message to log
     * @param error   the error to throw
     */
    void error(String message, Throwable error);

    /**
     * Logs an error message to console
     *
     * @param message the message to log
     */
    void error(String message);

    /**
     * Logs a warning message to console
     *
     * @param message the message to log
     */
    void warning(String message);

    /**
     * Logs an info message to console
     *
     * @param message the message to log
     */
    void info(String message);
}
