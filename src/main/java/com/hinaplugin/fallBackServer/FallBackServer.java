package com.hinaplugin.fallBackServer;

import net.kyori.adventure.platform.bungeecord.BungeeAudiences;
import net.md_5.bungee.api.plugin.Plugin;

public final class FallBackServer extends Plugin {

    public static FallBackServer plugin;
    public static Config config;
    public BungeeAudiences audiences = BungeeAudiences.create(this);

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;

        config = new Config(this);
        config.load();

        this.getProxy().getPluginManager().registerCommand(this, new LobbyCommands(this));
        this.getProxy().getPluginManager().registerCommand(this, new ReloadCommands(this));
        this.getProxy().getPluginManager().registerListener(this, new ReconnectListener(this.getProxy()));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public BungeeAudiences getAudiences(){ return  audiences; }
}
