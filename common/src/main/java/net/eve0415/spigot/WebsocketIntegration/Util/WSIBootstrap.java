package net.eve0415.spigot.WebsocketIntegration.Util;

import java.nio.file.Path;

public interface WSIBootstrap {
    /**
     * Called when the GeyserBootstrap is enabled
     */
    void onEnable();

    /**
     * Called when the GeyserBootstrap is disabled
     */
    void onDisable();

    /**
     * Returns the current GeyserConfiguration
     *
     * @return The current GeyserConfiguration
     */
    WSIConfiguration getConfig();

    /**
     * Returns the current GeyserLogger
     *
     * @return The current GeyserLogger
     */
    WSILogger getLogger();

    /**
     * Return the data folder where files get stored
     *
     * @return Path location of data folder
     */
    Path getConfigFolder();
}
