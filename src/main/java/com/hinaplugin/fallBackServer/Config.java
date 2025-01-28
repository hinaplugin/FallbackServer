package com.hinaplugin.fallBackServer;

import net.md_5.bungee.api.plugin.Plugin;

public class Config extends BungeeConfigDriver{
    public Config(Plugin plugin) {
        super(plugin);
    }

    public String getFallBackServerName(){
        return this.config.getString("fallback-server", "lobby");
    }

    public String getFallbackMessage(){
        return this.config.getString("fallback-message", "");
    }

    public String getFallbackTitle(){
        return this.config.getString("fallback-title", "");
    }

    public String getFallbackSubtitle(){
        return this.config.getString("fallback-subtitle", "");
    }

    public String getFallbackActionbar(){
        return this.config.getString("fallback-actionbar", "");
    }
}
