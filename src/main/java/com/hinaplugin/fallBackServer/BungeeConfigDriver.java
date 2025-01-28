package com.hinaplugin.fallBackServer;

import com.google.common.base.Charsets;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.*;
import java.nio.file.Files;
import java.util.Objects;
import java.util.logging.Logger;

public class BungeeConfigDriver {
    protected Configuration config;
    private final Plugin plugin;
    private String fileName = "config.yml";
    private String resourceFileName = "bungee-config.yml";
    private int version = 1;


    public BungeeConfigDriver(Plugin plugin, String fileName, String resourceFileName, int version) {
        this.plugin = plugin;
        this.fileName = fileName;
        this.resourceFileName = resourceFileName;
        this.version = version;
    }

    public BungeeConfigDriver(Plugin plugin, String fileName, String resourceFileName) {
        this.plugin = plugin;
        this.fileName = fileName;
        this.resourceFileName = resourceFileName;
    }

    public BungeeConfigDriver(Plugin plugin) {
        this.plugin = plugin;
    }

    private Logger getLogger() {
        return plugin.getLogger();
    }

    public boolean load() {
        try {
            if (!this.plugin.getDataFolder().exists()) {
                if (!this.plugin.getDataFolder().mkdir()){
                    return false;
                }
            }

            final File configFile = new File(plugin.getDataFolder(), fileName);
            if (!configFile.exists()) {
                if (configFile.createNewFile()) {
                    try (final InputStream inputStream = plugin.getResourceAsStream(resourceFileName); final OutputStream outputStream = Files.newOutputStream(configFile.toPath())){
                        ByteStreams.copy(Objects.requireNonNull(inputStream), outputStream);
                    }catch (Exception exception){
                        this.logTrace(exception);
                    }
                }
            }

            Configuration config;
            try (InputStreamReader stream = new InputStreamReader(new FileInputStream(configFile), Charsets.UTF_8)) {
                config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(stream);
            }

            this.config = config;
            if (config != null) {
                return onLoaded(config);
            } else {
                return false;
            }

        } catch (Exception e) {
            this.getLogger().severe("Unable to load \"" + fileName + "\". An error has occurred.");
            this.getLogger().severe(e.getLocalizedMessage());
            return false;
        }
    }

    public boolean save() {
        if (!this.plugin.getDataFolder().exists()) {
            //noinspection ResultOfMethodCallIgnored
            this.plugin.getDataFolder().mkdir();
        }

        boolean var2;
        try {
            final File configFile = new File(plugin.getDataFolder(), fileName);
            try (OutputStreamWriter outputStreamWriter = new OutputStreamWriter(Files.newOutputStream(configFile.toPath()), Charsets.UTF_8)) {
                ConfigurationProvider.getProvider(YamlConfiguration.class).save(this.config, outputStreamWriter);
            }
            var2 = true;

        } catch (Exception e) {
            this.getLogger().severe("Unable to save \"" + fileName + "\". An error has occurred.");
            this.getLogger().severe(e.getLocalizedMessage());
            var2 = false;
        }

        this.load();
        return var2;
    }

    public boolean update() {
        try {
            if (!plugin.getDataFolder().exists()){
                return false;
            }

            final File configFile = new File(plugin.getDataFolder(), fileName);
            if (!configFile.exists()){
                return false;
            }

            if (this.version == this.config.getInt("version", version)){
                return true;
            }

            final String fileNameReplace = fileName.replace(".yml", "_old.yml");

            final File configOldFile = new File(plugin.getDataFolder(), fileNameReplace);
            if (configOldFile.exists()){
                if (!configOldFile.delete()){
                    return false;
                }
            }

            if (!configFile.renameTo(new File(plugin.getDataFolder(), fileNameReplace))){
                return false;
            }

            this.plugin.getLogger().warning(fileName + "がアップデートされました．");
            this.plugin.getLogger().warning("そのためこれまでの" + fileName + "は" + fileNameReplace + "に変更されました．");
            this.load();
            return true;
        }catch (Exception exception){
            this.logTrace(exception);
            return false;
        }
    }

    public boolean onLoaded(Configuration config) {
        return true;
    }

    private void logTrace(Exception exception){
        plugin.getLogger().severe(exception.getClass().getName() + ": " + exception.getLocalizedMessage());
    }

}