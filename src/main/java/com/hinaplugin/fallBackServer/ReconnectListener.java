package com.hinaplugin.fallBackServer;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.TitlePart;
import net.md_5.bungee.api.AbstractReconnectHandler;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.ServerKickEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class ReconnectListener implements Listener {

    private final ProxyServer proxyServer;

    public ReconnectListener(ProxyServer proxyServer){
        this.proxyServer = proxyServer;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDisconnect(ServerKickEvent event){
        ServerInfo from;

        if (event.getPlayer().getServer() != null){
            from = event.getPlayer().getServer().getInfo();
        }else if (this.proxyServer.getReconnectHandler() != null){
            from = this.proxyServer.getReconnectHandler().getServer(event.getPlayer());
        }else {
            from = AbstractReconnectHandler.getForcedHost(event.getPlayer().getPendingConnection());
            if (from == null){
                from = this.proxyServer.getServerInfo(event.getPlayer().getPendingConnection().getListener().getServerPriority().get(0));
            }
        }

        ServerInfo to = this.proxyServer.getServerInfo(FallBackServer.config.getFallBackServerName());

        if (from != null && from.equals(to)){
            return;
        }

        event.setCancelled(true);
        event.setCancelServer(to);

        final MiniMessage miniMessage = MiniMessage.miniMessage();

        if (!FallBackServer.config.getFallbackMessage().isEmpty()){
            FallBackServer.plugin.getAudiences().player(event.getPlayer()).sendMessage(miniMessage.deserialize(FallBackServer.config.getFallbackMessage()));
        }

        if (!FallBackServer.config.getFallbackTitle().isEmpty()){
            FallBackServer.plugin.getAudiences().player(event.getPlayer()).sendTitlePart(TitlePart.TITLE, miniMessage.deserialize(FallBackServer.config.getFallbackTitle()));
        }

        if (!FallBackServer.config.getFallbackSubtitle().isEmpty()){
            FallBackServer.plugin.getAudiences().player(event.getPlayer()).sendTitlePart(TitlePart.SUBTITLE, miniMessage.deserialize(FallBackServer.config.getFallbackSubtitle()));
        }

        if (!FallBackServer.config.getFallbackActionbar().isEmpty()){
            FallBackServer.plugin.getAudiences().player(event.getPlayer()).sendActionBar(miniMessage.deserialize(FallBackServer.config.getFallbackActionbar()));
        }
    }
}
