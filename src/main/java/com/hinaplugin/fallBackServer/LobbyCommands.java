package com.hinaplugin.fallBackServer;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class LobbyCommands extends Command {
    private final FallBackServer plugin;
    public LobbyCommands(FallBackServer plugin) {
        super("lobby", "fallbackserver.commands.lobby", "hub");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (commandSender instanceof ProxiedPlayer player){
            final String hubName = FallBackServer.config.getFallBackServerName();
            final ServerInfo serverInfo = plugin.getProxy().getServerInfo(hubName);
            if (serverInfo == null){
                ComponentBuilder builder = new ComponentBuilder().append(hubName + "を取得できませんでした・・・");
                player.sendMessage(builder.create());
                return;
            }

            if (player.getServer().getInfo().getName().equalsIgnoreCase(hubName)){
                ComponentBuilder builder = new ComponentBuilder().append("既に" + hubName + "にいます・・・");
                player.sendMessage(builder.create());
                return;
            }

            serverInfo.ping(((result, error) -> {
                if (error == null){
                    ComponentBuilder builder = new ComponentBuilder().append(hubName + "に移動しました!");
                    player.sendMessage(builder.create());
                    player.connect(serverInfo);
                }else {
                    ComponentBuilder builder = new ComponentBuilder().append(hubName + "を取得できませんでした・・・");
                    player.sendMessage(builder.create());
                }
            }));
        }
    }
}
