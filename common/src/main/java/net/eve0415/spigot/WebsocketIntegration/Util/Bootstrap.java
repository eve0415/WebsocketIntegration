package net.eve0415.spigot.WebsocketIntegration.Util;

import java.nio.file.Path;

public interface Bootstrap {
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
    Configuration getConfig();

    /**
     * Returns the current GeyserLogger
     *
     * @return The current GeyserLogger
     */
    Logger getLogger();

    /**
     * Return the data folder where files get stored
     *
     * @return Path location of data folder
     */
    Path getConfigFolder();
}
