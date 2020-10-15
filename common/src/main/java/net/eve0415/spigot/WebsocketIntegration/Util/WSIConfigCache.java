package net.eve0415.spigot.WebsocketIntegration.Util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.CustomClassLoaderConstructor;

public class WSIConfigCache {
    public static WSIConfiguration readConfig(Path path) {
        Yaml yaml = new Yaml(new CustomClassLoaderConstructor(WSIConfiguration.class.getClassLoader()));
        try {
            InputStream in = Files.newInputStream(path);
            WSIConfiguration configData = yaml.loadAs(in, WSIConfiguration.class);
            return configData;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
